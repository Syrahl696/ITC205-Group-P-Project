package bcccp.carpark.entry;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntryController 
		implements ICarSensorResponder,
				   ICarparkObserver,
		           IEntryController {
    
        private enum STATE { IDLE, WAITING, FULL, VALIDATED, ISSUED, TAKEN, ENTERING, ENTERED, BLOCKED }
        private STATE state_;
	private STATE prevState_;
	private String message;
	
	private IGate entryGate;
	private ICarSensor outsideSensor; 
	private ICarSensor insideSensor;
	private IEntryUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private long entryTime;
	private String seasonTicketId = null;
        Boolean flagEntering = false;
	
    /**
     * Creates an Controller object for a carpark entry point, requiring an inside sensor, an outside sensor, a gate, and a user interface. 
     * Also registers the entry controller as a responder to those sensors, as the controller for that UI, and as an observer to the carpark object.
     * @param carpark
     * @param entryGate
     * @param os
     * @param is
     * @param ui
     */
    public EntryController(Carpark carpark, IGate entryGate, 
			ICarSensor os, 
			ICarSensor is,
			IEntryUI ui) {
		this.carpark = carpark;
                this.entryGate = entryGate;
                this.outsideSensor = os;
                this.insideSensor = is;
                this.ui = ui;
                entryControllerRegister(); //Registers the entry controller as a responder to those sensors, 
                                           //as the controller for that UI, and as an observer to the carpark object.
                setState(STATE.IDLE);                           
                
	}
        private void entryControllerRegister() {
                outsideSensor.registerResponder(this);
                insideSensor.registerResponder(this);
                ui.registerController(this);
                carpark.register(this);
		
	}
        
        private void log(String message) {
		System.out.println("EntryController : " + message);
	}
        
        private void setState(STATE newState) {
		switch (newState) {
		
		case BLOCKED: 
			log("setState: BLOCKED");
			prevState_ = state_;
			state_ = STATE.BLOCKED;
			message = "Blocked";
			ui.display(message);
			break;
			
		case IDLE: 
			log("setState: IDLE");
			if (prevState_ == STATE.ENTERED) {
				if (adhocTicket != null) {
					adhocTicket.enter(entryTime);
					carpark.recordAdhocTicketEntry();
					entryTime = 0;
					log(adhocTicket.toString() );
					adhocTicket = null;
				}
				else if (seasonTicketId != null) {
					carpark.recordSeasonTicketEntry(seasonTicketId);
					seasonTicketId = null;
				}
			}
			message = "Idle";
			state_ = STATE.IDLE;
			prevState_ = state_;
			ui.display(message);
			if (outsideSensor.carIsDetected()) {
				setState(STATE.WAITING);
			}
			if (entryGate.isRaised()) {
				entryGate.lower();
			}
			ui.discardTicket();
			break;
			
		case WAITING: 
			log("setState: WAITING");
			message = "Push Button";
			state_ = STATE.WAITING;
			prevState_ = state_;
			ui.display(message);
			if (!outsideSensor.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case FULL: 
			log("setState: FULL");
			message = "Carpark Full";
			state_ = STATE.FULL;
			prevState_ = state_;
			ui.display(message);
			break;
			
		case VALIDATED: 
			log("setState: VALIDATED");
			message = "Ticket Validated";
			state_ = STATE.VALIDATED;
			prevState_ = state_;
			ui.display(message);
			if (!outsideSensor.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case ISSUED: 
			log("setState: ISSUED");
			message = "Take Ticket";
			state_ = STATE.ISSUED;
			prevState_ = state_;
			ui.display(message);
			if (!outsideSensor.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case TAKEN: 
			log("setState: TAKEN");
			message = "Ticket Taken";
			state_ = STATE.TAKEN;
			prevState_ = state_;
			ui.display(message);
			entryGate.raise();
			break;
			
		case ENTERING: 
			log("setState: ENTERING");
			message = "Entering";
			state_ = STATE.ENTERING;
			prevState_ = state_;
			ui.display(message);
			break;
			
		case ENTERED: 
			log("setState: ENTERED");
			message = "Entered";
			state_ = STATE.ENTERED;
			prevState_ = state_;
			ui.display(message);
			break;
			
		default: 
			break;
			
		}
				
	}

    /**
     * Tells the user interface to display "Carpark Full" if carpark is full, 
     * otherwise displays prompt to take their adhoc ticket.
     * 
     * Carpark Full will be not be automatically turned off - it will stay showing until a car leaves.
     */
    @Override
	public void buttonPushed() {
		if (state_ == STATE.WAITING) {
			if (!carpark.isFull()) {
				adhocTicket = carpark.issueAdhocTicket();
				
				String carparkId = adhocTicket.getCarparkId();
				int ticketNo = adhocTicket.getTicketNo();
				entryTime = System.currentTimeMillis();
				//entryTime = adhocTicket.getEntryDateTime();
				String barcode = adhocTicket.getBarcode();
				
				ui.printTicket(carparkId, ticketNo, entryTime, barcode);
				setState(STATE.ISSUED);
			}
			else {
				setState(STATE.FULL);
			}
		}
		else {
			ui.beep();
			log("ButtonPushed: called while in incorrect state");
		}
		
	}
	

    /**
     * Checks that season ticket is valid and not in use, 
     * then stores the ticket data in the controller object until the car has entered the carpark.
     * @param barcode
     */
    @Override
	public void ticketInserted(String barcode) {
        		if (state_ == STATE.WAITING) {
			try {
				if (carpark.isSeasonTicketValid(barcode) &&
					!carpark.isSeasonTicketInUse(barcode)) {
					this.seasonTicketId = barcode;
					setState(STATE.VALIDATED);
				}
				else {
					ui.beep();
					seasonTicketId = null;
					log("ticketInserted: invalid ticket id");				
				}
			}
			catch (NumberFormatException e) {
				ui.beep();
				seasonTicketId = null;
				log("ticketInserted: invalid ticket id");				
			}
		}
		else {
			ui.beep();
			log("ticketInserted: called while in incorrect state");
		}
		
	}

    /**
     *Sets the state after the ticket has been taken.
     */
    @Override
	public void ticketTaken() {
		if (state_ == STATE.ISSUED || state_ == STATE.VALIDATED ) {
			setState(STATE.TAKEN);
		}
		else {
			ui.beep();
			log("ticketTaken: called while in incorrect state");
		}
		
	}

    /**
     *If a car leaves, and the entry controller is in the FULL state,
     * check whether the carpark is no longer full and update the state accordingly.
     *
     */
    @Override
	public void notifyCarparkEvent() {
                if (state_ == STATE.FULL) {
			if (!carpark.isFull()) {
				setState(STATE.WAITING);
			}
		}
		
	}

    /**
     * If the inside or outside sensor detects a car entering or leaving it's area of influence, 
     * performs an appropriate action based on the sensor, the detection, and the state of the controller. Accepts a sensorID and a boolean detected value as input.
     * @param detectorId
     * @param detected
     */
    @Override
	public void carEventDetected(String detectorId, boolean detected) {
            
            log("carEventDetected: " + detectorId + ", car Detected: " + detected );
		
		switch (state_) {
		
		case BLOCKED: 
			if (detectorId.equals(insideSensor.getId()) && !detected) {
				setState(prevState_);
			}
			break;
			
		case IDLE: 
			log("eventDetected: IDLE");
			if (detectorId.equals(outsideSensor.getId()) && detected) {
				log("eventDetected: setting state to WAITING");
				setState(STATE.WAITING);
			}
			else if (detectorId.equals(insideSensor.getId()) && detected) {
				setState(STATE.BLOCKED);
			}
			break;
			
		case WAITING: 
		case FULL: 
		case VALIDATED: 
		case ISSUED: 
			if (detectorId.equals(outsideSensor.getId()) && !detected) {
				setState(STATE.IDLE);
			}
			else if (detectorId.equals(insideSensor.getId()) && detected) {
				setState(STATE.BLOCKED);
			}
			break;
			
		case TAKEN: 
			if (detectorId.equals(outsideSensor.getId()) && !detected) {
				setState(STATE.IDLE);
			}
			else if (detectorId.equals(insideSensor.getId()) && detected) {
				setState(STATE.ENTERING);
			}
			break;
			
		case ENTERING: 
			if (detectorId.equals(outsideSensor.getId()) && !detected) {
				setState(STATE.ENTERED);
			}
			else if (detectorId.equals(insideSensor.getId()) && !detected) {
				setState(STATE.TAKEN);
			}
			break;
			
		case ENTERED: 
			if (detectorId.equals(outsideSensor.getId()) && detected) {
				setState(STATE.ENTERING);
			}
			else if (detectorId.equals(insideSensor.getId()) && !detected) {
				setState(STATE.IDLE);
			}
			break;
			
		default: 
			break;
			
		}
	}
}
