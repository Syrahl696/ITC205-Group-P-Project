package bcccp.tickets.adhoc;

import java.util.Date;

public class AdhocTicket implements IAdhocTicket {
	
	private String carparkId;
	private int ticketNo;
	private long entryDateTime = 0;
	private long paidDateTime = 0;
	private long exitDateTime = 0;
	private float charge = 0;
	private String barcode;

	
	
        //Adhoc ticket constructor, assigning values and setting enter time to current time
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
            this.carparkId = carparkId;
            this.ticketNo = ticketNo;
            this.barcode = barcode;

	}


        //returns ticketNo Integer
	@Override
	public int getTicketNo() {
		return ticketNo;
	}


        //returns barcode String
	@Override
	public String getBarcode() {
		return barcode;
	}


        //returns carParkId String
	@Override
	public String getCarparkId() {
		return carparkId;
	}

        
        //sets the enter time from given long value
	@Override
	public void enter(long dateTime) {
                this.entryDateTime = dateTime;		
	}


        //returns entryTime long
	@Override
	public long getEntryDateTime() {
		return entryDateTime;
	}

        
        //returns boolean if ticket is valid, if it has an entry time and no exit time
	@Override
	public boolean isCurrent() {
            return entryDateTime != 0 && exitDateTime == 0;
	}


        //assigns the paid charge to the Ticket from given float and exit long
	@Override
	public void pay(long dateTime, float charge) {
            this.charge = charge;
            this.paidDateTime = dateTime;	
	}


        //returns the paidDateTime long
	@Override
	public long getPaidDateTime() {
            return paidDateTime;
	}


        //returns boolean if ticket has had charge assigned to it, if it has been paid for
	@Override
	public boolean isPaid() {
	return paidDateTime != 0;
	}


        //returns the assigned charge of the ticket, not used for paying of ticket. 
	@Override
	public float getCharge() {
		return charge;
	}


        //assigns given long dateTime to exitDateTime
	@Override
	public void exit(long dateTime) {
            this.exitDateTime = dateTime;
		
	}


        //returns exitDateTime long
	@Override
	public long getExitDateTime() {
		return exitDateTime;
	}


        //returns boolean, if exitDateTime has a value
	@Override
	public boolean hasExited() {
            return exitDateTime != 0;
	}

	
	
}
