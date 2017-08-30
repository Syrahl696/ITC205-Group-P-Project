package bcccp.tickets.season;

import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.IUsageRecordFactory;
import java.util.*;


public class SeasonTicketDAO implements ISeasonTicketDAO {

	private IUsageRecordFactory factory;
        private HashMap<String, ISeasonTicket> seasonTickets = new HashMap();
	
	/**
         * @throws  RuntimeException if the reference to the adhocTicketFactory is null
         * SeasonTicket constructor, assigning factory
         * @param factory 
         */
	public SeasonTicketDAO(IUsageRecordFactory factory) {
           if (factory == null){
            throw new RuntimeException("reference to the UsageRecordFactory is null");
        }
                     //An instance of a class implementing the IAdhocTicketFactory
                     this.factory = factory;

	}

/**
 * Registers ticket to hashmap with ticket.getId() as key and ticket as value
 * @param ticket 
 */
	@Override
	public void registerTicket(ISeasonTicket ticket) {
		// TODO Auto-generated method stub
                seasonTickets.put(ticket.getId(), ticket);
                 }
	


/**
 * Gets the key for the specified season tickets key and if not null removes season ticket from hashmap
 * @param ticket 
 */
        @Override
	public void deregisterTicket(ISeasonTicket ticket) {
               /*if(seasonTickets.containsKey(ticket.getId())){
			seasonTickets.remove(ticket.getId());
		}*/
               Object value = seasonTickets.get(ticket.getId());
               if (value != null) {
                        seasonTickets.remove(ticket.getId());
            }
	}


/**
 * method for how many registered season tickets
 * @return seasonTickets.size()
 */
	@Override
	public int getNumberOfTickets() {
		return seasonTickets.size();
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
 * @param ticketId 
 */
	@Override
	public void recordTicketEntry(String ticketId) {
		Object value = seasonTickets.get(ticketId);
               if (value != null) {
                   IUsageRecord record = factory.make(ticketId, System.currentTimeMillis());
			seasonTickets.get(ticketId).recordUsage(record);
               }
	}


/**
 * Records ticket exit method,gets the key for the specified season tickets key and if not null 
 * record end usage method, calls IUsageRecordFactory passing in ticketId and System.currentTimeMillis() 
 * @param ticketId 
 */
	@Override
	public void recordTicketExit(String ticketId) {
		// TODO Auto-generated method stub
		Object value = seasonTickets.get(ticketId);
               if (value != null) {
                   seasonTickets.get(ticketId).endUsage(System.currentTimeMillis());
               }
	}
	
	
	
}
