package bcccp.tickets.season;

public class UsageRecord implements IUsageRecord {
	
	String ticketId;
	long startDateTime;
	long endDateTime;
	
	
	
	public UsageRecord(String ticketId, long startDateTime) {
            this.ticketId = ticketId;
            this.startDateTime = startDateTime;
		//TODO Implement constructor
	}


/**
 * @see bcccp.tickets.season.IUsageRecord#getEndTime() 
 */
	@Override
	public void finalise(long endTime) {
		// TODO Auto-generated method stub
                endDateTime = endTime;
		
	}


/**
 * @see bcccp.tickets.season.IUsageRecord#getStartTime() 
 */
	@Override
	public long getStartTime() {
		// TODO Auto-generated method stub
		return startDateTime;
	}


/**
 * @see bcccp.tickets.season.IUsageRecord#getEndTime() 
 */
	@Override
	public long getEndTime() {
		// TODO Auto-generated method stub
		return endDateTime;
	}



	@Override
	public String getSeasonTicketId() {
		// TODO Auto-generated method stub
		return ticketId;
	}
	
	
	
}
