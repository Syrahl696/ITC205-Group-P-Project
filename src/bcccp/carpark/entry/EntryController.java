package bcccp.carpark.entry;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class EntryController 
		implements ICarSensorResponder,
				   ICarparkObserver,
		           IEntryController {
	
	private IGate entryGate;
	private ICarSensor outsideSensor; 
	private ICarSensor insideSensor;
	private IEntryUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private long entryTime;
	private String seasonTicketId = null;
        Boolean flagEntering = false;
	
	

	public EntryController(Carpark carpark, IGate entryGate, 
			ICarSensor os, 
			ICarSensor is,
			IEntryUI ui) {
		this.carpark = carpark;
                this.entryGate = entryGate;
                this.outsideSensor = os;
                this.insideSensor = is;
                this.ui = ui;
                entryControllerRegister();
                
                //TODO Implement constructor
	}
        private void entryControllerRegister() {
                outsideSensor.registerResponder(this);
                insideSensor.registerResponder(this);
                ui.registerController(this);
                carpark.register(this);
		// TODO Auto-generated method stub
		
	}


	@Override
	public void buttonPushed() {
		if (carpark.isFull()){
                    ui.display("Carpark Full");
                    //TODO if carpark is full, halt, if it empties, resume
                }
                adhocTicket = carpark.issueAdhocTicket();
                ui.printTicket(adhocTicket.getCarparkId(), adhocTicket.getTicketNo(),
                        adhocTicket.getEntryDateTime(), adhocTicket.getBarcode());
                ui.display("Take Ticket");
		
	}



	@Override
	public void ticketInserted(String barcode) {
            if (carpark.isSeasonTicketValid(barcode) & carpark.isSeasonTicketInUse(barcode)){ //Season tickets don't appear to have a barcode, but the method had 'barcode' as the input string...
                seasonTicketId = barcode;
                entryGate.raise();
            }
            
	}



	@Override
	public void ticketTaken() {
		entryGate.raise();
                ui.display("Enter Carpark");
		
	}



	@Override
	public void notifyCarparkEvent() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		if ((detectorId == null ? outsideSensor.getId() == null : detectorId.equals(outsideSensor.getId())) & detected){ //checks to see if the sensor is the outside sensor and if a car is detected
                    ui.display("Push Button");
                }
                if ((detectorId == null ? outsideSensor.getId() == null : detectorId.equals(outsideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    ui.display("");
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & detected){ // car entering
                    Boolean flagEntering = true;
                }
                if ((detectorId == null ? insideSensor.getId() == null : detectorId.equals(insideSensor.getId())) & !detected){ //checks to see if the sensor is the outside sensor and if a car is not detected
                    if (flagEntering){
                        Boolean flagEntering = false;
                        entryGate.lower();
                        if (adhocTicket != null){
                            carpark.recordAdhocTicketEntry(adhocTicket); //see comment on this method in Carpark, that method should probably need the ticket to be passed to it but it currently doesn't
                        }
                        else if (seasonTicketId != null){
                            carpark.recordSeasonTicketEntry(seasonTicketId); //see comment on this method in Carpark
                        }
                        
                        else{
                            //ALARM!!!
                        }
                    }
                }
		
	}

	
	
}
