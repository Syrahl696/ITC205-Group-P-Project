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
        final long FIFTEEN_MINUTES = 900000;
        final float FIFTEEN_MINUTE_PRICE = 1;
	
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



        //create and return new adhoc ticket
	@Override
	public IAdhocTicket issueAdhocTicket() {

            return adhocTicketDAO.createTicket(carparkId);
	}

    /**
     * Also notifies all observers, allowing them to take an action if the carpark is full.
     */
    @Override

	public void recordAdhocTicketEntry(IAdhocTicket ticket) {
            
            adhocTicketDAO.addToCurrentList(ticket);
            numberOfCarsParked++;
            if (this.isFull()){ //If the carpark is full, notify all observers. Entry pillars will then display carpark full.

                for (int i = 0; i < observers.size(); i++){
                    observers.get(i).notifyCarparkEvent();
                }

            }
		
	}



	@Override

	public IAdhocTicket getAdhocTicket(String barcode) { 
            //return adhocTicket object, or null if not found
		return adhocTicketDAO.findTicketByBarcode(barcode);

	}



        //decided on calculating per 15 minutes, with a charge of $4 an hour
	@Override
	public float calculateAdHocTicketCharge(long entryDateTime) {
            long stayTime = System.currentTimeMillis() - entryDateTime;
            System.out.println("Current time: " + System.currentTimeMillis() + "  entryDateTime: " + entryDateTime + "  stayTime: " + stayTime);
            
            float fifteenMinuteLotsStayed = (stayTime / FIFTEEN_MINUTES) + 1;
            System.out.println("amount of fifteen minute lots: " + fifteenMinuteLotsStayed);
            
            return fifteenMinuteLotsStayed * FIFTEEN_MINUTE_PRICE;
	}



	@Override

	public void recordAdhocTicketExit(IAdhocTicket ticket) {
            numberOfCarsParked--;
            
            adhocTicketDAO.removeFromCurrentList(ticket);

                for (int i = 0; i < observers.size(); i++){
                        observers.get(i).notifyCarparkEvent();
                }

		
	}



/**
 * registers season ticket
 * @see bcccp.tickets.season.ISeasonTicketDAO#registerTicket(seasonTicket) 
 * @param seasonTicket 
 */
	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.registerTicket(seasonTicket);
		
	}


/**
 * deregisters season ticket
 * @see bcccp.tickets.season.ISeasonTicketDAO#deregisterTicket(seasonTicket) 
 * @param seasonTicket 
 */
	@Override
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.deregisterTicket(seasonTicket);
		
	}


/**
 * Finds season ticket by Id and if it exists and is still valid returns true else returns false 
 * @see bcccp.tickets.season.ISeasonTicketDAO#findTicketById(String ticketId)
 * @param ticketId
 * @return boolean
 */
	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		ISeasonTicket seasonTicket = seasonTicketDAO.findTicketById(ticketId);
		if ((seasonTicket != null) && (System.currentTimeMillis() >= seasonTicket.getEndValidPeriod())){
			return true;
		}
                return false;
	}


/**
 * Finds season ticket by Id and then returns whether or not it is in use
 * @see bcccp.tickets.season.ISeasonTicketDAO#findTicketById(String ticketId) 
 * @param ticketId
 * @return boolean
 */
	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		ISeasonTicket seasonTicket = seasonTicketDAO.findTicketById(ticketId);
            return seasonTicket.getCurrentUsageRecord() != null;


/**
 * Records season ticket entry and increments number of cars parked by one
 * @param ticketId 
 */
	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		// TODO Auto-generated method stub
		seasonTicketDAO.recordTicketEntry(ticketId);
		numberOfCarsParked++;
                
	}


/**
 * Records season ticket exit and decrements number of cars parked by one
 * @param ticketId 
 */
	@Override
	public void recordSeasonTicketExit(String ticketId) {
		seasonTicketDAO.recordTicketExit(ticketId);
		numberOfCarsParked--;

}
