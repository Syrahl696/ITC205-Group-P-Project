package bcccp.carpark;

import java.util.ArrayList;
import java.util.List;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import java.util.Calendar;
import java.util.Date;

public class Carpark implements ICarpark {
	
	private List<ICarparkObserver> observers;
	private String carparkId;
	private int capacity;
	private int numberOfCarsParked;
	private IAdhocTicketDAO adhocTicketDAO;
	private ISeasonTicketDAO seasonTicketDAO;
	
    /**
     * Constructs a Carpark object with the name, capacity, and the data access objects passed to it.
     *
     * @param name
     * @param capacity
     * @param adhocTicketDAO
     * @param seasonTicketDAO
     */
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

    /**
     * Registers the passed object (usually an entry controller) as an observer to this carpark.
     * @param observer
     */
    @Override
	public void register(ICarparkObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
		
	}

    /**
     * Removes the passed object (usually an entry controller) as an observer to this carpark.
     * @param observer
     */
    @Override
	public void deregister(ICarparkObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
		
	}

    /**
     *Returns the name of this carpark.
     * @return
     */
    @Override
	public String getName() {
		return this.carparkId;
	}

    /**
     * Returns true if the carpark cannot accept any more ad-hoc ticket customers.
     * @return
     */
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

    /**
     * Also notifies all observers, allowing them to take an action if the carpark is full.
     */
    @Override
	public void recordAdhocTicketEntry(IAdhocTicket ticket) { //should this have the ticket passed to it?
		// TODO Auto-generated method stub
                if (this.isFull()){ //If the carpark is full, notify all observers. Entry pillars will then display carpark full.
                    for (int i = 0; i < observers.size(); i++){
                        observers.get(i).notifyCarparkEvent();
                    }

                }
		
	}



	@Override
	public IAdhocTicket getAdhocTicket(String barcode) { //I would assume this would return null if the given ticket is not an adhoc ticket.
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public float calculateAddHocTicketCharge(long entryDateTime) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void recordAdhocTicketExit(IAdhocTicket ticket) { // consider putting the check for empty carpark in this method or another? - nevermind
		// TODO Auto-generated method stub
                for (int i = 0; i < observers.size(); i++){
                        observers.get(i).notifyCarparkEvent();
                }
		
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
	public void recordSeasonTicketExit(String ticketId) { // consider putting the check for empty carpark in this method or another? - nevermind
		// TODO Auto-generated method stub
		
	}

	
	

}
