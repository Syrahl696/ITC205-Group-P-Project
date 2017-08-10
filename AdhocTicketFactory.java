package bcccp.tickets.adhoc;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdhocTicketFactory implements IAdhocTicketFactory {

    
        //Constructor uses given carparkId and ticketno and generates barcode, then creates ticket. 
	@Override
	public IAdhocTicket make(String carparkId, int ticketNo) {
            
            //get current miliseconds assign to dateTime
            long dateTime = System.currentTimeMillis();

            
            //create SimpleDateFormat object, supply formatting
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
            
            //use formatted to convert dateTIme to correct format, add ticketNo and assign to barcode
            String barcode =  Integer.toString(ticketNo) + formatter.format(dateTime);
            
            
            //create new ticket with carparkId, ticketNo & barcode
            AdhocTicket newTicket = new AdhocTicket(carparkId, ticketNo, barcode);
            
            //set current datetime used in barcode to the entry time of ticket. 
            newTicket.enter(dateTime);

            //return ticket
            return newTicket;   
	}

}
