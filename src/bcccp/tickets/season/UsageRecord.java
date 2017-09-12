package bcccp.tickets.season;

public class UsageRecord implements IUsageRecord {
	
	String ticketId;
	long startDateTime;
	long endDateTime;
	
	
/**
         * UsageRecord constructor, assigning ticketId value and setting entry time to current time
         * @throws RuntimeException if the string identifying the season ticket is null or empty
         * @throws RuntimeException if the start time is less than or equal to zero
         * @param ticketId
         * @param startDateTime 
         */
	public UsageRecord(String ticketId, long startDateTime) {
                if (ticketId == null || ticketId == "") {
                    throw new RuntimeException("the string identifying the season ticket is null or empty");
                    }
                if (startDateTime <= 0) {
                    throw new RuntimeException("the start time is less than or equal to zero");
                    }
            this.ticketId = ticketId;
            this.startDateTime = startDateTime;
	}


/**
 * @throws RuntimeException if the end time is less than or equal to zero
 * @param endDateTime
 * @see bcccp.tickets.season.IUsageRecord#getEndTime() 
 */
	@Override
	public void finalise(long endDateTime) {
            if (endDateTime <= 0) {
                    throw new RuntimeException("the end time is less than or equal to zero");
                    }
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
