package bcccp.carpark;

import java.util.ArrayList;
import java.util.List;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import java.sql.Time;
import java.util.Date;

public class Carpark implements ICarpark {
	
	private List<ICarparkObserver> observers;
	private String carparkId;
	private int capacity;
	private int numberOfCarsParked;
	private IAdhocTicketDAO adhocTicketDAO;
	private ISeasonTicketDAO seasonTicketDAO;
        final float BH_RATE = 4;
        final float OOH_RATE = 2;

        
	
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

	public void recordAdhocTicketEntry() {
            
            //no longer adding ticket to currentList
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
            Date current = new Date();
            return calcCharge(entryDateTime, current.getTime());
	}



	@Override
	public void recordAdhocTicketExit() {
            numberOfCarsParked--;
            
            //removal of ticket now done from exit controller

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
                return (seasonTicket != null) && (System.currentTimeMillis() >= seasonTicket.getEndValidPeriod());
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
 * Records season ticket entry and increments number of cars parked by one
 * @param ticketId 
 */
	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		seasonTicketDAO.recordTicketEntry(ticketId);
                
	}


/**
 * Records season ticket exit and decrements number of cars parked by one
 * @param ticketId 
 */
	@Override
	public void recordSeasonTicketExit(String ticketId) {
		seasonTicketDAO.recordTicketExit(ticketId);

}
        
        public float calcCharge(long start, long end) {
            Date startTime = new Date(start);//need to truncate to nearest minute
            Date endTime = new Date(end);//need to truncate
            
            int curDay = startTime.getDay();
            int endDay = endTime.getDay();
            
            float charge = 0;
            Date curStartTime = startTime;
            
            while (curDay != endDay) {
                System.out.println("current day does not equal end day");
                Date curEndTime = startTime;//should set time to midnight
                curEndTime.setHours(23);
                curEndTime.setMinutes(59);
                curEndTime.setSeconds(59);
                charge += calcDayCharge(curStartTime, curEndTime, curDay);
                curStartTime = curEndTime;
                curDay++;
                if (curDay == 8) {
                    curDay = 1;
                }
   
            }
            System.out.println("current day is now end day");
            charge += calcDayCharge(curStartTime, endTime, endDay);
            return charge;
        }
            
            //have to solve the midnight problem, because business days have different charges

        //need a Date variable to represent the start of BH and end of BH. 
            public float calcDayCharge(Date startDate, Date endDate, int day) {
                //set BH and OH  9 - 5pm
                
                Time startTime = new Time(startDate.getHours(), startDate.getMinutes(), startDate.getSeconds());
                Time endTime = new Time(endDate.getHours(), endDate.getMinutes(), endDate.getSeconds());
                Time startBH = new Time(9, 0, 0);
                Time endBH = new Time(17, 0, 0);
                
     
            float dayCharge = 0;
            if (isBusinessDay(day)) {
                
                
                if (endTime.before(startBH) || startTime.after(endBH)) {
                    dayCharge = (getMinutes(endTime) - getMinutes(startTime)) * OOH_RATE;
                    System.out.println("all OOH");
                }
                else if (startTime.after(startBH) && endTime.before(endBH)) {
                    dayCharge = (getMinutes(endTime) - getMinutes(startTime)) * BH_RATE;
                    System.out.println("all BH");
                }
                else if (startTime.before(startBH) && endTime.before(endBH)) {
                    dayCharge = (getMinutes(startBH) - getMinutes(startTime)) * OOH_RATE;
                    dayCharge += (getMinutes(endTime) - getMinutes(startBH)) * BH_RATE;
                    System.out.println("OOH then BH");
                }
                else if (startTime.after(startBH) && startTime.before(endBH) && endTime.before(endBH)) {
                    dayCharge = (getMinutes(endBH) - getMinutes(startTime)) * BH_RATE;
                    dayCharge += (getMinutes(endTime) - getMinutes(endBH)) * OOH_RATE;
                    System.out.println("BH then OOH");
                }
                else if (startTime.before(startBH) && endTime.after(endBH)) {
                    dayCharge = (getMinutes(startBH) - getMinutes(startTime)) * OOH_RATE;
                    dayCharge += (getMinutes(endBH) - getMinutes(startBH)) * BH_RATE;
                    dayCharge += (getMinutes(endTime) - getMinutes(endBH)) * OOH_RATE;
                    System.out.println("OOH - BH - OOH");
                }
                else {
                    System.out.println("time error");
                }
            }
            else {
                dayCharge = (getMinutes(endTime) - getMinutes(startTime)) * OOH_RATE;
                System.out.println("All OOH");
            }
            return dayCharge;
        }
        
        public boolean isBusinessDay(int day) {
            if (day > 0 && day < 6) {
                System.out.println("it is a business day: " + day);
                return true;
            }
            else
                return false;
        }
        
        public int getMinutes(Time time) {
            int minutes = 0;
            minutes += time.getMinutes();
            minutes += (time.getHours() * 60);
            
            return minutes;
        }
        

}