package bcccp.carpark.exit;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

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
	
	

	public ExitController(Carpark carpark, IGate exitGate, 
			ICarSensor is,
			ICarSensor os, 
			IExitUI ui) {
		this.carpark = carpark;
                this.exitGate = exitGate;
                this.outsideSensor = os;
                this.insideSensor = is;
                this.ui = ui;
                exitControllerRegister();
	}
        
        private void exitControllerRegister() {
                outsideSensor.registerResponder(this);
                insideSensor.registerResponder(this);
                ui.registerController(this);
                
		// TODO Auto-generated method stub
		
	}



	@Override
	public void ticketInserted(String ticketStr) {
            //TODO: Season Ticket exit
        
            adhocTicket = carpark.getAdhocTicket(ticketStr);
            if (adhocTicket.isPaid()){
                ui.display("Take Ticket");
                
            }
		
	}



	@Override
	public void ticketTaken() {
		exitGate.raise();
                ui.display("Thank You");
		
	}



	@Override
	public void carEventDetected(String detectorId, boolean detected) {
            if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & detected){ //checks to see if the sensor is the outside sensor and if a car is detected
                    ui.display("Insert Ticket");
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    ui.display("");
                }
		if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & detected){ // car entering
                    Boolean flagLeaving = true;
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    if (flagLeaving){
                        Boolean flagLeaving = false;
                        exitGate.lower();
                        if (adhocTicket != null){
                            adhocTicket.exit(exitTime);
                            carpark.recordAdhocTicketExit(adhocTicket); //see comment on this method in Carpark, that method should probably need the ticket to be passed to it but it currently doesn't
                            //TODO: Logic for if carpark was full
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
