package bcccp.tickets.adhoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdhocTicketDAO  implements IAdhocTicketDAO  {

	private IAdhocTicketFactory adhocTicketFactory;
	private int currentTicketNo;
        //using a hashmap instrad of List
        private Map<String, IAdhocTicket> currentAdhocTickets;

	
	//AdhocTicketDAO constructor. assigning factory and setting currentTicketNo = 1
	public AdhocTicketDAO(IAdhocTicketFactory factory) {
            if (factory == null) {
                throw new RuntimeException("AdhocTicketFactory is null");
            }
            
                this.adhocTicketFactory = factory;
                this.currentTicketNo = 1;
                currentAdhocTickets = new HashMap<>();
	}



        //createTicket method, calls AdhocTicketFactory passing in carpark Id and current ticket no. 
        //increments currentTicket no, and adds to current list. then returns new ticket
	@Override
	public IAdhocTicket createTicket(String carparkId) {
            if (carparkId.length() == 0 || carparkId == null) {
                throw new RuntimeException("carparkId is empty");
            }
            
             AdhocTicket newTicket = (AdhocTicket) adhocTicketFactory.make(carparkId, currentTicketNo);
                currentTicketNo++;
                //add new ticket to hasMap
                currentAdhocTickets.put(newTicket.getBarcode(), newTicket);
                
		return newTicket;
	}



        //searches currentAdhocTickets list for given barcode. Returns found ticket, or null if not found
	@Override
	public IAdhocTicket findTicketByBarcode(String findBarcode) {
            //update the currentTickets here, if has exited than remove
            IAdhocTicket ticket = currentAdhocTickets.get(findBarcode);
            
            if (ticket.hasExited()) {
                currentAdhocTickets.remove(ticket.getBarcode(), ticket);
            }
            
            return ticket;

	}
        
        public void removeCurrentTicket(IAdhocTicket ticket) {
            if (ticket.hasExited()) {
            currentAdhocTickets.remove(ticket.getBarcode(), ticket);
            }
        }



        //returns list of currentAdhocTickets
	@Override
	public List<IAdhocTicket> getCurrentTickets() {
            //convert hashMap to an arrayList and then return
		return Collections.unmodifiableList(new ArrayList<IAdhocTicket>(currentAdhocTickets.values()));
	}

//removed the addtoCurrentList and removefromCurrentList methods, no longer required. 
	
	
}
