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
	}
        
        private void exitControllerRegister() {
                outsideSensor.registerResponder(this);
                insideSensor.registerResponder(this);
                ui.registerController(this);
		
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
