package bcccp.carpark.paystation;

import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.IAdhocTicket;

public class PaystationController 
		implements IPaystationController {
	
	private IPaystationUI ui;	
	private ICarpark carpark;

	private IAdhocTicket  adhocTicket = null;
	private float charge;
	
	

	public PaystationController(ICarpark carpark, IPaystationUI ui) {
		//TODO Implement constructor
	}



	@Override
	public void ticketInserted(String barcode) {
		// TODO Auto-generated method stub
		adhocTicket = carpark.getAdhocTicket(barcode);
                adhocTicket.getEntryDateTime();
                //TODO Verify ticket based on date and time
                charge = adhocTicket.getCharge();
                ui.display("Please pay: " + String.valueOf(charge));
                
	}



	@Override
	public void ticketPaid() {
		adhocTicket.pay(currenttime, charge); //TODO: Sort out date format
                ui.printTicket(carpark.getName(), adhocTicket.getTicketNo(),
                        adhocTicket.getEntryDateTime(), adhocTicket.getPaidDateTime(),
                        charge, adhocTicket.getBarcode());
		ui.display("Take Ticket");
	}



	@Override
	public void ticketTaken() {
		ui.display("Leave in <15 min");
		
	}

	
	
}
