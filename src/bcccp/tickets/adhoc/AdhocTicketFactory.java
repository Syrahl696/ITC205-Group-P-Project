package bcccp.tickets.adhoc;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdhocTicketFactory implements IAdhocTicketFactory {

    
        //Constructor uses given carparkId and ticketno and generates barcode, then creates ticket. 
	@Override
	public IAdhocTicket make(String carparkId, int ticketNo) {
             if (carparkId.length() == 0 || carparkId == null) {
                throw new RuntimeException("carparkId is empty");
            }
             if (ticketNo <= 0) {
                 throw new RuntimeException("ticketNo is less than or equal to zero");
             }
            
            
            //get current miliseconds assign to dateTime
            long dateTime = System.currentTimeMillis();

            
            //create SimpleDateFormat with correct format, then convert to long value (int cannot take the size of number)
            String formatter = new SimpleDateFormat("ddMMyyyyHHmmss").format(dateTime);
            long date = Long.parseLong(formatter);

            //create barcode by converting ticketNo and date to HexString. 
            String barcode =  "A" + Integer.toHexString(ticketNo) + Long.toHexString(date);
            System.out.println("New barcode: " + barcode);
            
            
            //create new ticket with carparkId, ticketNo & barcode
            AdhocTicket newTicket = makeTicket(carparkId, ticketNo, barcode);
            

            //return ticket
            return newTicket;   
	}
	
        //factory helper, allows for testing
	public AdhocTicket makeTicket(String a, int b, String c) {
		return new AdhocTicket(a, b, c);
	}

}
