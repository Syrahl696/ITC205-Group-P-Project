package bcccp.carpark.exit;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExitController 
		implements ICarSensorResponder,
		           IExitController {
    
        private enum STATE { IDLE, WAITING, PROCESSED, REJECTED, TAKEN, EXITING, EXITED, BLOCKED } 
	
	private STATE state;
	private STATE prevState;
	private String message;
	
	private IGate exitGate;
	private ICarSensor insideSensor;
	private ICarSensor outsideSensor; 
	private IExitUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private long exitTime;
	private String seasonTicketId = null;
        Boolean flagLeaving = false;
        Boolean flagValid = false;
	
    /**
     * Creates an Controller object for a carpark exit point, requiring an inside sensor, an outside sensor, a gate, and a user interface. 
     * Also registers the entry controller as a responder to those sensors and as the controller for that UI.
     * @param carpark
     * @param exitGate
     * @param is
     * @param os
     * @param ui
     */
    public ExitController(Carpark carpark, IGate exitGate, 
			ICarSensor is,
			ICarSensor os, 
			IExitUI ui) {
		this.carpark = carpark;
                this.exitGate = exitGate;
                this.outsideSensor = os;
                this.insideSensor = is;
                this.ui = ui;
                exitControllerRegister();  //Registers the entry controller as a responder to those sensors
                                           //and as the controller for that UI.
                                           
                prevState = STATE.IDLE;		
		setState(STATE.IDLE);                           
	}
        
        private void exitControllerRegister() {
                outsideSensor.registerResponder(this);
                insideSensor.registerResponder(this);
                ui.registerController(this);
		
	}
        
        private void log(String message) {
		System.out.println("ExitController : " + message);
	}
        
        private void setState(STATE newState) {
		switch (newState) {
		
		case BLOCKED: 
			log("setState: BLOCKED");
			prevState = state;
			state = STATE.BLOCKED;
			message = "Blocked";
			ui.display(message);
			break;
			
		case IDLE: 
			log("setState: IDLE");
			if (prevState == STATE.EXITED) {
				if (adhocTicket != null) {
					adhocTicket.exit(exitTime);
					carpark.recordAdhocTicketExit();
                                        //Calls for the AdhocTicketDAO to return a reference to this ticket, updating the list of current tickets in the process.
                                        carpark.getAdhocTicket(adhocTicket.getBarcode()); 
					log(adhocTicket.toString() );
				}
				else if (seasonTicketId != null) {
					carpark.recordSeasonTicketExit(seasonTicketId);
				}
			}
			adhocTicket = null;
			seasonTicketId = null;
			
			message = "Idle";
			state = STATE.IDLE;
			prevState = state;
			ui.display(message);
			if (insideSensor.carIsDetected()) {
				setState(STATE.WAITING);
			}
			if (exitGate.isRaised()) {
				exitGate.lower();
			}
			exitTime = 0;
			break;
			
		case WAITING: 
			log("setState: WAITING");
			message = "Insert Ticket";
			state = STATE.WAITING;
			prevState = state;
			ui.display(message);
			if (!insideSensor.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case PROCESSED: 
			log("setState: PROCESSED");
			message = "Take Processed Ticket";
			state = STATE.PROCESSED;
			prevState = state;
			ui.display(message);
			if (!insideSensor.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case REJECTED: 
			log("setState: REJECTED");
			message = "Take Rejected Ticket";
			state = STATE.REJECTED;
			prevState = state;
			ui.display(message);
			if (!insideSensor.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case TAKEN: 
			log("setState: TAKEN");
			message = "Ticket Taken";
			state = STATE.TAKEN;
			prevState = state;
			ui.display(message);
			break;
			
		case EXITING: 
			log("setState: EXITING");
			message = "Exiting";
			state = STATE.EXITING;
			prevState = state;
			ui.display(message);
			break;
			
		case EXITED: 
			log("setState: EXITED");
			message = "Exited";
			state = STATE.EXITED;
			prevState = state;
			ui.display(message);
			break;
			
		default: 
			break;
			
		}
				
	}
        
        private boolean isAdhocTicket(String barcode) {
		return barcode.substring(0,1).equals("A");
	}

    /**
     * Reads the ticket that has been inserted, and checks whether it's an adhoc ticket that has been paid,
     * or a season ticket that is valid and in use, and allows exit in those cases. Otherwise, an error message is displayed.
     * @param ticketStr
     */
    @Override
	public void ticketInserted(String ticketStr) {          
        if (state == STATE.WAITING) {
			if (isAdhocTicket(ticketStr)) {
				adhocTicket = carpark.getAdhocTicket(ticketStr);
				exitTime = System.currentTimeMillis();
				if (adhocTicket != null && adhocTicket.isPaid() && exitTime - adhocTicket.getPaidDateTime() <= 900000) {
					setState(STATE.PROCESSED);
				}
				else {
					ui.beep();
					setState(STATE.REJECTED);						
				}
			}
			else if (carpark.isSeasonTicketValid(ticketStr) &&
					 carpark.isSeasonTicketInUse(ticketStr)){					
				seasonTicketId = ticketStr;
				setState(STATE.PROCESSED);
			}
			else {
				ui.beep();
				setState(STATE.REJECTED);						
			}
		}
		else {
			ui.beep();
			ui.discardTicket();
			log("ticketInserted: called while in incorrect state");
			setState(STATE.REJECTED);						
		}
		
		
	}

    /**
     * If the taken ticket was deemed valid, raises the gate, displays a thank you, and resets a flag.
     */
    @Override
	public void ticketTaken() {
            if (state == STATE.PROCESSED)  {
			exitGate.raise();
			setState(STATE.TAKEN);
		}
		else if (state == STATE.REJECTED) {
			setState(STATE.WAITING);
		}
		else {
			ui.beep();
			log("ticketTaken: called while in incorrect state");
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
		log("carEventDetected: " + detectorId + ", car Detected: " + detected );
		
		switch (state) {
		
		case BLOCKED: 
			if (detectorId.equals(outsideSensor.getId()) && !detected) {
				setState(prevState);
			}
			break;
			
		case IDLE: 
			log("eventDetected: IDLE");
			if (detectorId.equals(insideSensor.getId()) && detected) {
				log("eventDetected: setting state to WAITING");
				setState(STATE.WAITING);
			}
			else if (detectorId.equals(outsideSensor.getId()) && detected) {
				setState(STATE.BLOCKED);
			}
			break;
			
		case WAITING: 
		case PROCESSED: 
			if (detectorId.equals(insideSensor.getId()) && !detected) {
				setState(STATE.IDLE);
			}
			else if (detectorId.equals(outsideSensor.getId()) && detected) {
				setState(STATE.BLOCKED);
			}
			break;
			
		case TAKEN: 
			if (detectorId.equals(insideSensor.getId()) && !detected) {
				setState(STATE.IDLE);
			}
			else if (detectorId.equals(outsideSensor.getId()) && detected) {
				setState(STATE.EXITING);
			}
			break;
			
		case EXITING: 
			if (detectorId.equals(insideSensor.getId()) && !detected) {
				setState(STATE.EXITED);
			}
			else if (detectorId.equals(outsideSensor.getId()) && !detected) {
				setState(STATE.TAKEN);
			}
			break;
			
		case EXITED: 
			if (detectorId.equals(insideSensor.getId()) && detected) {
				setState(STATE.EXITING);
			}
			else if (detectorId.equals(outsideSensor.getId()) && !detected) {
				setState(STATE.IDLE);
			}
			break;
			
		default: 
			break;
			
		}
  }
}