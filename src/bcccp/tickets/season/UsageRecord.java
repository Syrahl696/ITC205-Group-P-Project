package bcccp.tickets.season;

public class UsageRecord implements IUsageRecord {
	
	String ticketId;
	long startDateTime;
	long endDateTime;
	
	
	/**
         * UsageRecord constructor, assigning ticketId value and setting entry time to current time
         * @param ticketId
         * @param startDateTime 
         */
	public UsageRecord(String ticketId, long startDateTime) {
            this.ticketId = ticketId;
            this.startDateTime = startDateTime;
	}


/**
 * @param endDateTime
 * @see bcccp.tickets.season.IUsageRecord#getEndTime() 
 */
	@Override
	public void finalise(long endDateTime) {
                this.endDateTime = endDateTime;
		
	}


/**
 * @return startDateTime
 * @see bcccp.tickets.season.IUsageRecord#getStartTime() 
 */
	@Override
	public long getStartTime() {
		return startDateTime;
	}


/**
 * @return endDateTime
 * @see bcccp.tickets.season.IUsageRecord#getEndTime() 
 */
	@Override
	public long getEndTime() {
		return endDateTime;
	}


/**
 * @return endDateTime
 * @see bcccp.tickets.season.IUsageRecord#getId() 
 */
	@Override
	public String getSeasonTicketId() {
		return ticketId;
	}
	
	
	
}
