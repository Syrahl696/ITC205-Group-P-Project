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
			if (outsideEntrySensor_.carIsDetected()) {
				setState(STATE.WAITING);
			}
			if (entryGate_.isRaised()) {
				entryGate_.lower();
			}
			ui.discardTicket();
			break;
			
		case WAITING: 
			log("setState: WAITING");
			message = "Push Button";
			state_ = STATE.WAITING;
			prevState_ = state_;
			ui.display(message);
			if (!outsideEntrySensor_.carIsDetected()) {
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
			if (!outsideEntrySensor_.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case ISSUED: 
			log("setState: ISSUED");
			message = "Take Ticket";
			state_ = STATE.ISSUED;
			prevState_ = state_;
			ui.display(message);
			if (!outsideEntrySensor_.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case TAKEN: 
			log("setState: TAKEN");
			message = "Ticket Taken";
			state_ = STATE.TAKEN;
			prevState_ = state_;
			ui.display(message);
			entryGate_.raise();
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
		if (carpark.isFull()){
                    ui.display("Carpark Full");
                    return;
                    //TODO if carpark is full, halt, if it empties, resume
                }
                if (outsideSensor.carIsDetected()){
                    adhocTicket = carpark.issueAdhocTicket();
                    ui.printTicket(adhocTicket.getCarparkId(), adhocTicket.getTicketNo(),
                            adhocTicket.getEntryDateTime(), adhocTicket.getBarcode());
                    ui.display("Take Ticket");
                }
		
	}

    /**
     * Checks that season ticket is valid and not in use, 
     * then stores the ticket data in the controller object until the car has entered the carpark.
     * @param barcode
     */
    @Override
	public void ticketInserted(String barcode) {
            if (outsideSensor.carIsDetected()){
            if (carpark.isSeasonTicketValid(barcode)){
                if(!carpark.isSeasonTicketInUse(barcode)){ //Season tickets don't appear to have a barcode, but the method had 'barcode' as the input string...
                    seasonTicketId = barcode;
                    ui.display("Take Ticket");
                    }
            }
            else {
                
                ui.display("Remove Invalid Ticket");
            }
            }
            
	}

    /**
     *Raises the gate after the ticket has been taken. and displays Enter Carpark.
     */
    @Override
	public void ticketTaken() {
                if (seasonTicketId != null || adhocTicket != null){
                    entryGate.raise();
                    ui.display("Enter Carpark");
                } else {
                    ui.display("Push Button");
                }
		
		
	}

    /**
     *If a car leaves, double check that carpark is no longer full,
     * then call carEventDetected to check wheter a car is waiting and if one is,
     * display Push Button, if there is not, clear the screen.
     */
    @Override
	public void notifyCarparkEvent() {
                if (!carpark.isFull()){
                    this.carEventDetected(outsideSensor.getId(), outsideSensor.carIsDetected());
                } else {
                    ui.display("Carpark Full");
                }
		
	}

    /**
     * If the inside or outside sensor detects a car entering or leaving it's area of influence, 
     * performs an appropriate action. Accepts a sensorID and a boolean detected value as input.
     * @param detectorId
     * @param detected
     */
    @Override
	public void carEventDetected(String detectorId, boolean detected) {
                
		if ((detectorId == null ? outsideSensor.getId() == null : detectorId.equals(outsideSensor.getId())) & detected){ //checks to see if the sensor is the outside sensor and if a car is detected
                    ui.display("Push Button");
                }
                if ((detectorId == null ? outsideSensor.getId() == null : detectorId.equals(outsideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    ui.display("");
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & detected){ // car entering
                    flagEntering = true;
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    if (flagEntering){
                        flagEntering = false;
                        entryGate.lower();
                        if (adhocTicket != null){
                            carpark.recordAdhocTicketEntry(adhocTicket); //see comment on this method in Carpark, that method should probably need the ticket to be passed to it but it currently doesn't
                            adhocTicket = null;
                        }
                        else if (seasonTicketId != null){
                            carpark.recordSeasonTicketEntry(seasonTicketId); //see comment on this method in Carpark
                            seasonTicketId = null;
                        }
                        
                        else{
                            //ALARM!!!
                        }
                    }
                }
		
	}

	
	
}
