package bcccp.tickets.adhoc;

import java.util.List;

public interface IAdhocTicketDAO {
	
	public IAdhocTicket createTicket(String carparkId);
	public IAdhocTicket findTicketByBarcode(String barcode);
	public List<IAdhocTicket> getCurrentTickets();

    public void addToCurrentList(IAdhocTicket ticket);
    public void removeFromCurrentList(IAdhocTicket ticket);


}
