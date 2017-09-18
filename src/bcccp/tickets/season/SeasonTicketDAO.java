package bcccp.tickets.season;

import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.IUsageRecordFactory;
import java.util.*;


public class SeasonTicketDAO implements ISeasonTicketDAO {

	private IUsageRecordFactory factory;
        private HashMap<String, ISeasonTicket> seasonTickets = new HashMap();
	
	/**
         * @throws  RuntimeException if the reference to the UsageRecordFactory is null
         * SeasonTicket constructor, assigning factory
         * @param factory 
         */
	public SeasonTicketDAO(IUsageRecordFactory factory) {
           if (factory == null){
            throw new RuntimeException("reference to the UsageRecordFactory is null");
        }
                     //An instance of a class implementing the IUsageRecordFactory
                     this.factory = factory;

	}

/**
 * Registers ticket to hashmap with ticket.getId() as key and ticket as value
 * @throws RuntimeException if ticket is null
 * @param ticket 
 */
	@Override
	public void registerTicket(ISeasonTicket ticket) {
        if (ticket == null){
            throw new RuntimeException("ticket is null");
        }
                seasonTickets.put(ticket.getId(), ticket);
                 }

/**
 * Gets the key for the specified season tickets key and if not null removes season ticket from hashmap
 * @throws RuntimeException if ticket is null
 * @param ticket 
 */
        @Override
	public void deregisterTicket(ISeasonTicket ticket) {
               Object value = seasonTickets.get(ticket.getId());
               if (value == null) {
                   throw new RuntimeException("ticket is null");
               }
               seasonTickets.remove(ticket.getId());  
}


/**
 * method for how many registered season tickets
 * @return seasonTickets.size()
 */
	@Override
	public int getNumberOfTickets() {
		return this.seasonTickets.size();
	}


/**
 * Gets the key for the specified season tickets key and if not null returns said key else return null
 * @param ticketId
 * @return 
 */
	@Override
	public ISeasonTicket findTicketById(String ticketId) {
                Object value = seasonTickets.get(ticketId);
               if (value != null) {
                        return seasonTickets.get(ticketId);
               }
               else {
		return null;
               
        }
        }

/**
 * Records ticket entry method,gets the key for the specified season tickets key and if not null 
 * record start usage method, calls IUsageRecordFactory passing in ticketId and System.currentTimeMillis() 
 * @throws RuntimeException if season ticket identified by ticketId is not in the internal store
 * @param ticketId 
 */
	@Override
	public void recordTicketEntry(String ticketId) {
		Object value = seasonTickets.get(ticketId);
               if (value == null) {
                   throw new RuntimeException("season ticket identified by ticketId is not in the internal store");
               }
                   IUsageRecord record = factory.make(ticketId, System.currentTimeMillis());
                   seasonTickets.get(ticketId).recordUsage(record);
        }

/**
 * Records ticket exit method,gets the key for the specified season tickets key and if not null 
 * record end usage method, calls IUsageRecordFactory passing in ticketId and System.currentTimeMillis() 
 * @throws RuntimeException if season ticket identified by ticketId is not in the internal store
 * @throws RuntimeException if the season ticket identified by ticketId is not currently in use
 * @param ticketId 
 */
	@Override
	public void recordTicketExit(String ticketId) {
		Object value = seasonTickets.get(ticketId);
                    if (value == null) {
                        throw new RuntimeException("season ticket identified by ticketId is not in the internal store");
                    }
                    if (seasonTickets.get(ticketId).getCurrentUsageRecord() == null){
                        throw new RuntimeException("season ticket not in use");
                    }
                        seasonTickets.get(ticketId).endUsage(System.currentTimeMillis());}
               
	}

