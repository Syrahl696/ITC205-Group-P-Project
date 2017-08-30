package bcccp.tickets.season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

/**
 * Class SeasonTicket
 */
public class SeasonTicket implements ISeasonTicket {
	
	private List<IUsageRecord> usages;
	private IUsageRecord currentUsage = null;
	
	private String ticketId;
	private String carparkId;
	private long startValidPeriod;
	private long endValidPeriod;
	
                /**
         * Instantiates new Season Ticket
         * @param ticketId
         * @param carparkId
         * @param startValidPeriod
         * @param endValidPeriod 
         */
	public SeasonTicket (String ticketId, 
			             String carparkId, 
			             long startValidPeriod,
			             long endValidPeriod) {            
            //Throws a RuntimeException if the ticket id string is empty or null.
            if (ticketId == null || ticketId == "") {
                throw new RuntimeException("the ticket id string is empty or null");
                    }
            
            //Throws a RuntimeException if the carpark name is empty or null
            if (carparkId == null || carparkId == "") {
                throw new RuntimeException("the carpark name is empty or null");
                    }
            
            //Throws a RuntimeException if the starting date is less than or equal to zero
            if (startValidPeriod <= 0) {
                throw new RuntimeException("the starting date is less than or equal to zero");
                    }
            
            //Throws a RuntimeException if the end date is less than or equal to the starting date
            if (endValidPeriod <= startValidPeriod) {
                throw new RuntimeException("the end date is less than or equal to the starting date");
                    }
                //A unique string identifying the season ticket.
                this.ticketId = ticketId;
                
                //A string identifying the carpark name of the carpark for which the season ticket is issued.
		this.carparkId = carparkId;
                
                //A long specifying the starting date for which the season ticket is issued
		this.startValidPeriod = startValidPeriod;
                
                //A long specifying the end date for which the season ticket is issued
		this.endValidPeriod = endValidPeriod;
                
                //Initialises an arraylist of usages
                this.usages = new ArrayList<>();
	}
/**
 * @return ticketId
 * @see bcccp.tickets.season.ISeasonTicket#getId() 
 */
	@Override
	public String getId() {
		return ticketId;
	}
/**
 * @return carparkId
 * @see bcccp.tickets.season.ISeasonTicket#getCarparkId() 
 */
	@Override
	public String getCarparkId() {
		return carparkId;
	}
/**
 * @return startValidPeriod
 * @see bcccp.tickets.season.ISeasonTicket#getStartValidPeriod() 
 */
	@Override
	public long getStartValidPeriod() {
		return startValidPeriod;
	}
/**
 * @return endValidPeriod
 * @see bcccp.tickets.season.ISeasonTicket#getEndValidPeriod() 
 */
	@Override
	public long getEndValidPeriod() {
		return endValidPeriod;
	}

/**
 * @return currentUsage true if !null
 * @see bcccp.tickets.season.ISeasonTicket#currentUsage() 
 */    
	@Override
	public boolean inUse() {
                return currentUsage!=null;
		
	}
/**
 * records a new UsageRecord as current
 * @throws a RuntimeException if UsageRecord is null
 * @param record 
 */
	@Override
	public void recordUsage(IUsageRecord record) {
            if (record==null){
                throw new RuntimeException("Runtime Exception: UsageRecord is null");
            }
                currentUsage = record;		
	}
/**
 * @return currentUsage
 */
	@Override
	public IUsageRecord getCurrentUsageRecord() {
		return currentUsage;
	}

        /**
         * end season Ticket usage method, then adds to usages Arraylist and makes the ticket no longer in use
         * @param dateTime 
         */
	@Override
	public void endUsage(long dateTime) {
		currentUsage.finalise(dateTime);
		usages.add(currentUsage);
		currentUsage = null;
	}

        /**
         * Returns ArrayList of Usages from records
         * @return usages
         */
	@Override
	public List<IUsageRecord> getUsageRecords() {
		// TODO Auto-generated method stub
		return usages;
	}


}
