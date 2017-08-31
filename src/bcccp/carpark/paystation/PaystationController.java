package bcccp.carpark.paystation;

import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.IAdhocTicket;

public class PaystationController 
		implements IPaystationController {
    
        private enum STATE { IDLE, WAITING, REJECTED, PAID } 
	
	private STATE state_;
	
	private IPaystationUI ui;	
	private ICarpark carpark;

	private IAdhocTicket  adhocTicket = null;
	private float charge;
	
    /**
     * Creates an Controller object for a carpark paystation, requiring a user interface.
     * @param carpark
     * @param ui
     */
    public PaystationController(ICarpark carpark, IPaystationUI ui) {
		this.carpark = carpark;
                this.ui = ui;
                entryControllerRegister(); //Registers the entry controller as a responder to those sensors, 
                                           //as the controller for that UI, and as an observer to the carpark object.
                setState(STATE.IDLE);	
                
	}
        private void entryControllerRegister() {

                ui.registerController(this);
	}
        
        private void log(String message) {
		System.out.println("PaystationController : " + message);
	}
        
        private void setState(STATE newState) {
		switch (newState) {
		
		case IDLE: 
			state_ = STATE.IDLE;
			ui_.display("Idle");
			
			log("setState: IDLE");
			break;
			
		case WAITING: 
			state_ = STATE.WAITING;
			log("setState: WAITING");
			break;
			
		case REJECTED: 
			state_ = STATE.WAITING;
			log("setState: WAITING");
			break;
			
		case PAID: 
			state_ = STATE.PAID;
			ui_.display("Paid");
			log("setState: PAID");
			break;			
			
		default: 
			break;
			
		}			
	}

    /**
     * Reads the ticket and checks whether it is an adhoc ticket that has not yet been paid. 
     * If it is, it calls a method to calculate and display the charge.
     * If it has already been paid, it immediately ejects the ticket with a message.
     * If it is invalid, displays an error.
     * @param barcode
     */
    @Override
	public void ticketInserted(String barcode) {
		if (carpark.getAdhocTicket(barcode) != null){
                    adhocTicket = carpark.getAdhocTicket(barcode);
                
                    if (adhocTicket.isPaid()){
                        ui.display("Already Paid");
                        
                    }
                    else {
                    
                    //TODO Verify ticket based on date and time in barcode
                    charge = carpark.calculateAdHocTicketCharge(adhocTicket.getEntryDateTime());
                    ui.display("Please pay: $" + String.valueOf(charge));
                    }
                } 
                else{
                    ui.display("Invalid Ticket");
                }
        } 

    /**
     * Registers the ticket as paid and prints an updated physical ticket.
     */
    @Override
	public void ticketPaid() {
		adhocTicket.pay(System.currentTimeMillis(), charge); //TODO: Sort out date format
                ui.printTicket(carpark.getName(), adhocTicket.getTicketNo(),
                        adhocTicket.getEntryDateTime(), adhocTicket.getPaidDateTime(),
                        charge, adhocTicket.getBarcode());
		ui.display("Take Ticket"/*"Take Ticket & Leave in <15 min"*/); //full version won't fit
	}

    /**
     * Clears the display when the ticket is taken.
     */
    @Override
	public void ticketTaken() {
		ui.display("");
		
	}

	
	
}
