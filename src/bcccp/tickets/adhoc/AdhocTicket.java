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
        private STATE state;
        
        private enum STATE {ISSUED, CURRENT, PAID, EXITED };

	
	
        //Adhoc ticket constructor, assigning values and setting enter time to current time
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
            if (carparkId.length() == 0 || carparkId == null) {
                throw new RuntimeException("carparkId is empty");
            }
            if (ticketNo <= 0) {
                throw new RuntimeException("TicketNo is less than zero");
            }
            if (barcode.length() == 0 || barcode == null) {
                throw new RuntimeException("barcode is empty");
            }
            
            this.carparkId = carparkId;
            this.ticketNo = ticketNo;
            this.barcode = barcode;
            this.state = STATE.ISSUED;

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
            if(dateTime <= 0) {
                throw new RuntimeException("Entry Datetime is less than zero");
            }
                this.entryDateTime = dateTime;	
                this.state = STATE.CURRENT;
	}


        //returns entryTime long
	@Override
	public long getEntryDateTime() {
		return entryDateTime;
	}

        
        //returns boolean if ticket is valid, if it has an entry time and no exit time
	@Override
	public boolean isCurrent() {
            return this.state == STATE.CURRENT;
	}


        //assigns the paid charge to the Ticket from given float and exit long
	@Override
	public void pay(long dateTime, float charge) {
            if (dateTime <= this.entryDateTime) {
                throw new RuntimeException("Paid date time is less than or equal to entry date time");
            }
            
            this.charge = charge;
            this.paidDateTime = dateTime;
            this.state = STATE.PAID;
	}


        //returns the paidDateTime long
	@Override
	public long getPaidDateTime() {
            if (this.state == STATE.PAID && System.currentTimeMillis() - paidDateTime > 900000){
                this.state = STATE.CURRENT;
            }
            return paidDateTime;
	}


        //returns boolean if ticket has had charge assigned to it, if it has been paid for
        //determine whether this method checks if ticket was paid within 15 minutes. 
	@Override

	public boolean isPaid() {
            return this.state == STATE.PAID;

	}


        //returns the assigned charge of the ticket, not used for paying of ticket. 
	@Override
	public float getCharge() {
		return charge;
	}


        //assigns given long dateTime to exitDateTime
	@Override
	public void exit(long dateTime) {
            if (dateTime <= this.paidDateTime) {
            throw new RuntimeException("ExitdateTime is less than or equal to paid date time");
            }
            
            this.exitDateTime = dateTime;
            this.state = STATE.EXITED;

	}


        //returns exitDateTime long
	@Override
	public long getExitDateTime() {
		return exitDateTime;
	}


        //returns boolean, if exitDateTime has a value
	@Override
	public boolean hasExited() {
            return this.state == STATE.EXITED;
	}

        //not sure what this does yet, but was in Jim's example
        public String toString() {
		Date entryDate = new Date(entryDateTime);
		Date paidDate = new Date(paidDateTime);
		Date exitDate = new Date(exitDateTime);

		return "Carpark    : " + carparkId + "\n" +
		       "Ticket No  : " + ticketNo + "\n" +
		       "Entry Time : " + entryDate + "\n" + 
		       "Paid Time  : " + paidDate + "\n" + 
		       "Exit Time  : " + exitDate + "\n" +
		       "State      : " + state + "\n" +
		       "Barcode    : " + barcode;		
	}
        
        
	
	
}
