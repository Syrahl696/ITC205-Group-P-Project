package bcccp.carpark;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        final long FIFTEEN_MINUTES = 900000;
        final float FIFTEEN_MINUTE_PRICE = 4;
	
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
     * @param ticket
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
            
            float fifteenMinuteLotsStayed = (stayTime / FIFTEEN_MINUTES) + 1;
            
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
 * registers season ticket with the carpark so that the season ticket may be used to access the carpark
 * @throws RuntimeException if the carpark the season ticket is associated with is not the same as the carpark name
 * @see bcccp.tickets.season.ISeasonTicketDAO#registerTicket(seasonTicket) 
 * @param seasonTicket 
 */
	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
            if (seasonTicket.getCarparkId() != carparkId){
            throw new RuntimeException("the carpark the season ticket is associated with is not the same as the carpark name");
        }
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
 * Finds season ticket by Id and if it exists, is still valid and it is within business hours returns true else returns false 
 * @see bcccp.tickets.season.ISeasonTicketDAO#findTicketById(String ticketId)
 * @param ticketId
 * @return boolean
 */
@Override
	public boolean isSeasonTicketValid(String ticketId) {
            
		ISeasonTicket seasonTicket = seasonTicketDAO.findTicketById(ticketId);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                boolean businessHours = false;
                try {
                //sets opening business hours at 7am
                String time1 = "07:00:00";
                Date openingTime = sdf.parse(time1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(openingTime);

                //sets closing business hours at 7pm
                String time2 = "19:00:00";
                Date closingTime = sdf.parse(time2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(closingTime);

                //sets current time
                Calendar calendar3 = Calendar.getInstance();
                String time3 = sdf.format(calendar3.getTime());
                Date currentTime = sdf.parse(time3);
                calendar3.setTime(currentTime);

                //tests if current time is between opening time and closing time
                Date current = calendar3.getTime();
                if (current.after(calendar1.getTime()) && (current.before(calendar2.getTime()))) {
                    businessHours = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                }
                
                Calendar c = Calendar.getInstance();
                //Retrieves current day as integer from 1-7 Sunday =1, Saturday = 7
                int day= c.get(Calendar.DAY_OF_WEEK);     
                
                return ((seasonTicket != null) && (System.currentTimeMillis() <= seasonTicket.getEndValidPeriod() &&
                        (businessHours == true) && (day >= 2) && (day <= 6)));
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
        }


/**
 * causes a new usage record to be created and associated with a season ticket
 * @throws RuntimeException if the season ticket associated with ticketId does not exist, or is currently in use
 * @param ticketId 
 */
	@Override
	public void recordSeasonTicketEntry(String ticketId) {
            if (seasonTicketDAO.findTicketById(ticketId) == null){
            throw new RuntimeException("season ticket associated with ticketId does not exist");
        }
            if (isSeasonTicketInUse(ticketId) == true){
                throw new RuntimeException("season ticket associated with ticketId is currently in use");
        }
		seasonTicketDAO.recordTicketEntry(ticketId);
                
	}


/**
 * causes the current usage record of the season ticket associated with ticketID to be finalized.
 * @throws RuntimeException if the season ticket associated with ticketId does not exist, or is not currently in use 
 * @param ticketId 
 */
	@Override
	public void recordSeasonTicketExit(String ticketId) {
             if (seasonTicketDAO.findTicketById(ticketId) == null){
            throw new RuntimeException("season ticket associated with ticketId does not exist");
        }
            if (isSeasonTicketInUse(ticketId) == false){
                throw new RuntimeException("season ticket associated with ticketId is currently not in use");
        }
		seasonTicketDAO.recordTicketExit(ticketId);

}

}
