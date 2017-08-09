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
                this.ticketId = ticketId;
		this.carparkId = carparkId;
		this.startValidPeriod = startValidPeriod;
		this.endValidPeriod = endValidPeriod;
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
 *  Records current usage
 * @param record 
 */
	@Override
	public void recordUsage(IUsageRecord record) {
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
