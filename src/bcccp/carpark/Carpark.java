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
            System.out.println(startTime.getTime());
            System.out.println(endTime.getTime());
            
            
            int curDay = startTime.getDay();
            int endDay = endTime.getDay();
            
            float charge = 0;
            Date curStartTime = new Date(startTime.getTime()+1000);           
            
            while (curDay != endDay) {
            	System.out.println(curDay + " , " + endDay);
                System.out.println("current day does not equal end day");
                Date curEndTime = new Date(startTime.getTime());   //should set time to midnight
                curEndTime.setHours(23);
                curEndTime.setMinutes(59);
                curEndTime.setSeconds(59);
                
                if (curStartTime.getHours() == 23 && curStartTime.getMinutes() == 59 && curStartTime.getSeconds() == 59) {
                		curStartTime.setHours(0);
                		curStartTime.setMinutes(0);
                		curStartTime.setSeconds(0);
                }
                charge += calcDayCharge(curStartTime, curEndTime, curDay);
                curStartTime = new Date(curEndTime.getTime());
                curDay++;
                if (curDay == 7) {
                    curDay = 0;
                }
   
            }
            System.out.println("current day is now end day");
            if (curStartTime.getHours() == 23 && curStartTime.getMinutes() == 59 && curStartTime.getSeconds() == 59) {
        		curStartTime.setHours(0);
        		curStartTime.setMinutes(0);
        		curStartTime.setSeconds(0);
        }
            charge += calcDayCharge(curStartTime, endTime, endDay);
            return charge;
        }
            
            //have to solve the midnight problem, because business days have different charges

        //need a Date variable to represent the start of BH and end of BH. 
            public float calcDayCharge(Date startDate, Date endDate, int day) {
                //set BH and OH  9 - 5pm
                
                Time startTime = new Time(startDate.getHours(), startDate.getMinutes(), startDate.getSeconds());
                Time endTime = new Time(endDate.getHours(), endDate.getMinutes(), endDate.getSeconds());
                Time startBH = new Time(7, 0, 0);
                Time endBH = new Time(19, 0, 0);
                System.out.println(startTime.getTime() + " " + startTime.getHours() + " " + startTime.getMinutes());
                System.out.println(endTime.getTime() + " " + endTime.getHours() + " " + endTime.getMinutes());
                
     
            float dayCharge = (float) 0.0;
            if (isBusinessDay(day)) {
                
                
                if (endTime.before(startBH) || startTime.after(endBH)) {
                    dayCharge = (float) (((getMinutes(endTime) - getMinutes(startTime))/60.0) * OOH_RATE);
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                    System.out.println("all OOH");
                }
                else if (startTime.after(startBH) && endTime.before(endBH)) {
                    dayCharge = (float) (((getMinutes(endTime) - getMinutes(startTime))/60.0) * BH_RATE);
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                    System.out.println("all BH");
                }
                else if (startTime.before(startBH) && endTime.before(endBH)) {
                    dayCharge = (float) (((getMinutes(startBH) - getMinutes(startTime))/60.0) * OOH_RATE);
                    dayCharge += ((getMinutes(endTime) - getMinutes(startBH))/60.0) * BH_RATE;
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                    System.out.println("OOH then BH");
                }
                else if (startTime.after(startBH) && startTime.before(endBH) && endTime.after(endBH)) {
                    dayCharge = (float) (((getMinutes(endBH) - getMinutes(startTime))/60.0) * BH_RATE);
                    dayCharge += ((getMinutes(endTime) - getMinutes(endBH))/60.0) * OOH_RATE;
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                    System.out.println("BH then OOH");
                }
                else if (startTime.before(startBH) && endTime.after(endBH)) {
                    dayCharge = (float) (((getMinutes(startBH) - getMinutes(startTime))/60.0) * OOH_RATE);
                    dayCharge += ((getMinutes(endBH) - getMinutes(startBH))/60.0) * BH_RATE;
                    dayCharge += ((getMinutes(endTime) - getMinutes(endBH))/60.0) * OOH_RATE;
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                    System.out.println("OOH - BH - OOH");
                }
                else {
                    System.out.println("time error");
                }
            }
            else {
                dayCharge = (float) (((getMinutes(endTime) - getMinutes(startTime))/60.0) * OOH_RATE);
                dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                System.out.println(dayCharge);
                System.out.println("All OOH");
            }
            System.out.println("dayCharge: " + dayCharge);
            return dayCharge;
        }
        
        public boolean isBusinessDay(int day) {
            if (day > 0 && day < 5) {
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
            if (time.getSeconds() >= 30) {
            	minutes++;
            }
            
            return minutes;
        }
        

}