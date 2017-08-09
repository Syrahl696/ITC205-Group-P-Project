package bcccp.tickets.season;

public class UsageRecordFactory implements IUsageRecordFactory {

    /**
     * Constructor uses given ticketId and startDateTime to create new usage record
     * @param ticketId
     * @param startDateTime
     * @return UsageRecord
     */
	@Override
	public IUsageRecord make(String ticketId, long startDateTime) {
               
            return new UsageRecord(ticketId, startDateTime);              
	}

}
