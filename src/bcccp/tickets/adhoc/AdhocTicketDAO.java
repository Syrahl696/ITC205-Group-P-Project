package bcccp.tickets.adhoc;

import java.util.List;

public class AdhocTicketDAO  implements IAdhocTicketDAO  {

	private IAdhocTicketFactory factory;
	private int currentTicketNo;
        private List<IAdhocTicket> currentAdhocTickets;

	
	
	public AdhocTicketDAO(IAdhocTicketFactory factory) {
                this.factory = factory;
                currentTicketNo = 1;
	}



	@Override
	public IAdhocTicket createTicket(String carparkId) {
             AdhocTicket newTicket = (AdhocTicket) factory.make(carparkId, currentTicketNo);
                currentTicketNo++;
		return newTicket;
	}



	@Override
	public IAdhocTicket findTicketByBarcode(String findBarcode) {
            for (int i = 0; i < currentAdhocTickets.size(); i++) {
            if (currentAdhocTickets.get(i).getBarcode() ==  findBarcode) {
                System.out.println("barcode found: " + findBarcode);
                return currentAdhocTickets.get(i);
            }
            
        }
            return null;

	}



	@Override
	public List<IAdhocTicket> getCurrentTickets() {
		return currentAdhocTickets;
	}

        
        //method to remove a given ticket from currrentAdhocTickets;
        public void removeFromCurrentList(IAdhocTicket ticket) {   
            currentAdhocTickets.remove(ticket);
        }
        
        //method to add a given ticket to currentAdhocTickets
        public void AddToCurrentList(IAdhocTicket ticket) {
            currentAdhocTickets.add(ticket);
        }
	
	
}
