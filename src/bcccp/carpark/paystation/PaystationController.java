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
			ui.display("Idle");
			
			log("setState: IDLE");
			break;
			
		case WAITING: 
			state_ = STATE.WAITING;
			log("setState: WAITING");
			break;
			
		case REJECTED: 
			state_ = STATE.REJECTED;
			log("setState: REJECTED");
			break;
			
		case PAID: 
			state_ = STATE.PAID;
			ui.display("Paid");
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
		if (state_ == STATE.IDLE) {
			adhocTicket = carpark.getAdhocTicket(barcode);
			if (adhocTicket != null) {
				charge = carpark.calculateAdhocTicketCharge(adhocTicket.getEntryDateTime());
				ui.display("Pay " + String.format("%.2f", charge));
				setState(STATE.WAITING);
			}
			else {
				ui.beep();
				ui.display("Take Rejected Ticket");
				setState(STATE.REJECTED);
				log("ticketInserted: ticket is not current");				
			}
		}
		else {
			ui.beep();
			log("ticketInserted: called while in incorrect state");				
		}
  } 

    /**
     * Registers the ticket as paid and prints an updated physical ticket.
     */
    @Override
	public void ticketPaid() {
		if (state_ == STATE.WAITING) {
			long payTime = System.currentTimeMillis();
			
			adhocTicket.pay(payTime, charge);
			
			String carparkId = adhocTicket.getCarparkId();
			int ticketNo = adhocTicket.getTicketNo();
			long entryTime = adhocTicket.getEntryDateTime();
			long paidTime = adhocTicket.getPaidDateTime();
			float charge = adhocTicket.getCharge();
			String barcode = adhocTicket.getBarcode();
			
			ui.printTicket(carparkId, ticketNo, entryTime, paidTime, charge, barcode);
			setState(STATE.PAID);
		}
		else {
			ui.beep();
			log("ticketPaid: called while in incorrect state");				
		}
	}

    /**
     * Clears the display when the ticket is taken.
     */
    @Override
	public void ticketTaken() {
		if (state_ == STATE.IDLE) {
			ui.beep();
			log("ticketTaken: called while in incorrect state");				
		}
		else {
			setState(STATE.IDLE);
		}
	}
}
