package bcccp.carpark;

import java.util.ArrayList;
import java.util.List;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;

public class Carpark implements ICarpark {
	
	private List<ICarparkObserver> observers;
	private String carparkId;
	private int capacity;
	private int numberOfCarsParked;
	private IAdhocTicketDAO adhocTicketDAO;
	private ISeasonTicketDAO seasonTicketDAO;
	
	
	
	public Carpark(String name, int capacity, 
			IAdhocTicketDAO adhocTicketDAO, 
			ISeasonTicketDAO seasonTicketDAO) {
            this.carparkId = name;
            this.capacity = capacity;
            this.numberOfCarsParked = 0;
            this.seasonTicketDAO = seasonTicketDAO;
            this.adhocTicketDAO = adhocTicketDAO;
            this.observers = new ArrayList<>();
	}



	@Override
	public void register(ICarparkObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
		
	}



	@Override
	public void deregister(ICarparkObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
		
	}



	@Override
	public String getName() {
		return this.carparkId;
	}



	@Override
	public boolean isFull() {
		// TODO Include logic to reserve spots for Season Ticket holders
		return (numberOfCarsParked >= capacity);
        }



	@Override
	public IAdhocTicket issueAdhocTicket() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void recordAdhocTicketEntry() { //should this have the ticket passed to it?
		// TODO Auto-generated method stub
		
	}



	@Override
	public IAdhocTicket getAdhocTicket(String barcode) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public float calculateAddHocTicketCharge(long entryDateTime) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void recordAdhocTicketExit() { // consider putting the check for empty carpark in this method or another?
		// TODO Auto-generated method stub
		
	}



	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void recordSeasonTicketExit(String ticketId) { // consider putting the check for empty carpark in this method or another?
		// TODO Auto-generated method stub
		
	}

	
	

}
