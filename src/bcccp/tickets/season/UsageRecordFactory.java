package bcccp.tickets.season;

public class UsageRecordFactory implements IUsageRecordFactory {

    /**
     * Constructor uses given ticketId and startDateTime to create new usage record
     * @throws RuntimeException if ticketId is null or empty
     * @throws RuntimeException if startDateTime is less than or equal to zero
     * @param ticketId
     * @param startDateTime
     * @return UsageRecord
     */
	@Override
	public IUsageRecord make(String ticketId, long startDateTime) {
               if (ticketId == null || ticketId == ""){
                   throw new RuntimeException("ticket is null or empty");
        }
               //specification says "throws a RuntimeException is less than or equal to zero" , assumed startDateTime
               if (startDateTime <= 0){
                   throw new RuntimeException("startDateTime is less than or equal to zero");
        }
            return new UsageRecord(ticketId, startDateTime);              
	}

}
