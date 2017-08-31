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
		System.out.println("EntryController : " + message);
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
			if (is.carIsDetected()) {
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
			if (!is.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case PROCESSED: 
			log("setState: PROCESSED");
			message = "Take Processed Ticket";
			state = STATE.PROCESSED;
			prevState = state;
			ui.display(message);
			if (!is.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case REJECTED: 
			log("setState: REJECTED");
			message = "Take Rejected Ticket";
			state = STATE.REJECTED;
			prevState = state;
			ui.display(message);
			if (!is.carIsDetected()) {
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

    /**
     * Reads the ticket that has been inserted, and checks whether it's an adhoc ticket that has been paid,
     * or a season ticket that is valid and in use, and allows exit in those cases. Otherwise, an error message is displayed.
     * @param ticketStr
     */
    @Override
	public void ticketInserted(String ticketStr) {          
        if (insideSensor.carIsDetected()){    
            if (carpark.getAdhocTicket(ticketStr) != null){
                adhocTicket = carpark.getAdhocTicket(ticketStr);
                if (adhocTicket.isPaid()){
                    flagValid = true;
                    ui.display("Take Ticket");
                }
                else {
                    flagValid = false;
                    
                    ui.display("Remove Unpaid Ticket");
                    }
            }
            else if (carpark.isSeasonTicketValid(ticketStr)){
                if (carpark.isSeasonTicketInUse(ticketStr)){                      
                    flagValid = true;
                    ui.display("Take Ticket");
                }
            }
            else{
                flagValid = false;
                
                ui.display("Remove Invalid Ticket");
            }
        }
		
	}

    /**
     * If the taken ticket was deemed valid, raises the gate, displays a thank you, and resets a flag.
     */
    @Override
	public void ticketTaken() {
            if (flagValid){
		exitGate.raise();
                ui.display("Thank You");
                flagValid = false;
            } else {
                ui.display("Insert Ticket");
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
            if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & detected){ //checks to see if the sensor is the outside sensor and if a car is detected
                    ui.display("Insert Ticket");
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    ui.display("");
                }
		if ((detectorId == null ? outsideSensor.getId() == null : detectorId.equals(outsideSensor.getId())) & detected){ // car entering
                    flagLeaving = true;
                }
                if ((detectorId == null ? outsideSensor.getId() == null : detectorId.equals(outsideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    if (flagLeaving){
                        flagLeaving = false;
                        exitGate.lower();
                        if (adhocTicket != null){
                            adhocTicket.exit(exitTime);
                            carpark.recordAdhocTicketExit(adhocTicket); //see comment on this method in Carpark, that method should probably need the ticket to be passed to it but it currently doesn't
                        }   
                            
                        }
                        else if (seasonTicketId != null){
                            carpark.recordSeasonTicketExit(seasonTicketId); //see comment on this method in Carpark
                        }
                        
                        else{
                            //Meh.
                        }
		
	}

	
	
}
}
