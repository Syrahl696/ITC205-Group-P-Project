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
            if (this.isFull()) {
                throw new RuntimeException("carpark is Full");
            }
            else {
                return adhocTicketDAO.createTicket(carparkId);
            }
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



    @Override
	public float calculateAdHocTicketCharge(long entryDateTime) {
            Date current = new Date();
            //calcCharge from given entryDateTime and current as payingTime
            return calcCharge(entryDateTime, current.getTime());
	}



    @Override
	public void recordAdhocTicketExit() {
            numberOfCarsParked--;

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
            //create Date objects with given long values
            Date startTime = new Date(start);
            Date endTime = new Date(end);
            
            //get day Integers from start/end times
            int curDay = startTime.getDay();
            int endDay = endTime.getDay();
            
            //initialize float = 0, currentStartTime = startTime
            float charge = 0;
            Date curStartTime = new Date(startTime.getTime());           
            
            //run while look as long as currentDay does not = endDay
            while (curDay != endDay) {
                Date curEndTime = new Date(startTime.getTime());   //set endDay time to midnight. 
                curEndTime.setHours(23);
                curEndTime.setMinutes(59);
                curEndTime.setSeconds(59);
                
                //if the start time is midnight, then have to set all values to 0. 
                if (curStartTime.getHours() == 23 && curStartTime.getMinutes() == 59 && curStartTime.getSeconds() == 59) {
                		curStartTime.setHours(0);
                		curStartTime.setMinutes(0);
                		curStartTime.setSeconds(0);
                }
                //call calcDayCharge method, passing in current values. 
                charge += calcDayCharge(curStartTime, curEndTime, curDay);
                //reset currentStartTime to endTime
                curStartTime = new Date(curEndTime.getTime());
                //increment day, check if passed into new week
                curDay++;
                if (curDay == 7) {
                    curDay = 0;
                }
   
            }
            //if current day is the same as end day, reset midnight to 0 values. 
            if (curStartTime.getHours() == 23 && curStartTime.getMinutes() == 59 && curStartTime.getSeconds() == 59) {
        		curStartTime.setHours(0);
        		curStartTime.setMinutes(0);
        		curStartTime.setSeconds(0);
        }
            //call calc method. 
            charge += calcDayCharge(curStartTime, endTime, endDay);
            //return accumulated charge
            return charge;
        }

        //calcDayCharge checks for BH and OOH and determines correct charge
        public float calcDayCharge(Date startDate, Date endDate, int day) {
            
            //create time objets from given Date objects
            Time startTime = new Time(startDate.getHours(), startDate.getMinutes(), startDate.getSeconds());
            Time endTime = new Time(endDate.getHours(), endDate.getMinutes(), endDate.getSeconds());
            //create Business Hours Time Objects
            Time startBH = new Time(7, 0, 0);
            Time endBH = new Time(19, 0, 0);

            //initialize dayCharge
            float dayCharge = (float) 0.0;
            //check if it is business day
            if (isBusinessDay(day)) { 
                
                //if isBusiness Day and all Out of Hours
                if (endTime.before(startBH) || startTime.after(endBH)) {
                    dayCharge = (float) (((getMinutes(endTime) - getMinutes(startTime))/60.0) * OOH_RATE);
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                }
                //if isBusiness Day and all in Business Hours
                else if (startTime.after(startBH) && endTime.before(endBH)) {
                    dayCharge = (float) (((getMinutes(endTime) - getMinutes(startTime))/60.0) * BH_RATE);
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                }
                //if isBusiness Day and Out of Hours start / Business Hours end
                else if (startTime.before(startBH) && endTime.before(endBH)) {
                    dayCharge = (float) (((getMinutes(startBH) - getMinutes(startTime))/60.0) * OOH_RATE);
                    dayCharge += ((getMinutes(endTime) - getMinutes(startBH))/60.0) * BH_RATE;
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                }
                //if isBusinessDay and Business Hours start / Out of Hours end
                else if (startTime.after(startBH) && startTime.before(endBH) && endTime.after(endBH)) {
                    dayCharge = (float) (((getMinutes(endBH) - getMinutes(startTime))/60.0) * BH_RATE);
                    dayCharge += ((getMinutes(endTime) - getMinutes(endBH))/60.0) * OOH_RATE;
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                }
                //if isBusiness Day Out of Hours start / through Business Hours / Out of Hours end
                else if (startTime.before(startBH) && endTime.after(endBH)) {
                    dayCharge = (float) (((getMinutes(startBH) - getMinutes(startTime))/60.0) * OOH_RATE);
                    dayCharge += ((getMinutes(endBH) - getMinutes(startBH))/60.0) * BH_RATE;
                    dayCharge += ((getMinutes(endTime) - getMinutes(endBH))/60.0) * OOH_RATE;
                    dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                }
                else {
                    //else time error
                    System.out.println("time error");
                }
            }
            //else not Busines Day, all Out of Hours
            else {
                dayCharge = (float) (((getMinutes(endTime) - getMinutes(startTime))/60.0) * OOH_RATE);
                dayCharge = (float) (Math.round(dayCharge * 100.0) / 100.0);
                System.out.println(dayCharge);
            }
            //return dayCharge
            return dayCharge;
        }
        
        //isBusinessDay() takes int and returns true or false for Business Day
        public boolean isBusinessDay(int day) {
            if (day > 0 && day < 5) {
                return true;
            }
            else
                return false;
        }
        
        //getMinutes() takes a Time object and returns the amount of total minutes. Calculated from hours, minutes and seconds
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