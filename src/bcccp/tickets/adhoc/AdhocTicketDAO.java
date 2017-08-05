package bcccp.tickets.adhoc;

import java.util.List;

public class AdhocTicketDAO  implements IAdhocTicketDAO  {

	private IAdhocTicketFactory factory;
	private int currentTicketNo;
        private List<IAdhocTicket> currentAdhocTickets;

	
	//AdhocTicketDAO constructor. assigning factory and setting currentTicketNo = 1
	public AdhocTicketDAO(IAdhocTicketFactory factory) {
                this.factory = factory;
                currentTicketNo = 1;
	}



        //createTicket method, calls AdhocTicketFactory passing in carpark Id and current ticket no. 
        //increments currentTicket no, then returns new ticket
	@Override
	public IAdhocTicket createTicket(String carparkId) {
             AdhocTicket newTicket = (AdhocTicket) factory.make(carparkId, currentTicketNo);
                currentTicketNo++;
		return newTicket;
	}



        //searches currentAdhocTickets list for given barcode. Returns found ticket, or null if not found
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



        //returns list of currentAdhocTickets
	@Override
	public List<IAdhocTicket> getCurrentTickets() {
		return currentAdhocTickets;
	}

        
        //method to remove a given ticket from currrentAdhocTickets;  called from Carpark Class
        public void removeFromCurrentList(IAdhocTicket ticket) {   
            currentAdhocTickets.remove(ticket);
        }
        
        //method to add a given ticket to currentAdhocTickets    called from Carpark Class
        public void AddToCurrentList(IAdhocTicket ticket) {
            currentAdhocTickets.add(ticket);
        }
	
	
}
