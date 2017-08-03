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

	
	
        //still to do
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
            this.carparkId = carparkId;
            this.ticketNo = ticketNo;
            this.barcode = barcode;
            
            Date currentTime = new Date();
            this.enter(currentTime.getTime());
	}


	@Override
	public int getTicketNo() {
		return ticketNo;
	}


	@Override
	public String getBarcode() {
		return barcode;
	}


	@Override
	public String getCarparkId() {
		return carparkId;
	}

        
	@Override
	public void enter(long dateTime) {
                this.entryDateTime = dateTime;		
	}


	@Override
	public long getEntryDateTime() {
		return entryDateTime;
	}

        
	@Override
	public boolean isCurrent() {
            return entryDateTime != 0 && exitDateTime == 0;
	}


	@Override
	public void pay(long dateTime, float charge) {
            this.charge = charge;
            this.paidDateTime = dateTime;	
	}


	@Override
	public long getPaidDateTime() {
            if (isPaid()) {
		return paidDateTime;
            }
            else {
                return 0;
            }
	}


        //still to do
	@Override
	public boolean isPaid() {
	return paidDateTime != 0;
	}


	@Override
	public float getCharge() {
		return charge;
	}


        //still to do
	@Override
	public void exit(long dateTime) {
            this.exitDateTime = dateTime;
		
	}


	@Override
	public long getExitDateTime() {
		return exitDateTime;
	}


        //still to do
	@Override
	public boolean hasExited() {
            return exitDateTime != 0;
	}

	
	
}
