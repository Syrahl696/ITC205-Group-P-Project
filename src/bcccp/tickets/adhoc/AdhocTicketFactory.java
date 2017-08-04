package bcccp.tickets.adhoc;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdhocTicketFactory implements IAdhocTicketFactory {

	@Override
	public IAdhocTicket make(String carparkId, int ticketNo) {
            //get current miliseconds
            long dateTime = System.currentTimeMillis();
            
            //create SimpleDateFormat object, supply formatting
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
            //convert milliseconds to correct format and add ticket no
            String barcode =  Integer.toString(ticketNo) + formatter.format(dateTime);
            System.out.println("Barcode is: " + barcode);
            
            
            //create new ticket with inputs and return
            AdhocTicket newTicket = new AdhocTicket(carparkId, ticketNo, barcode);
		return newTicket;   
	}
        
      /*  
      public String currentDateFormatted(int ticketNo) {
          String date = Integer.toString(ticketNo);
          
          Date time = new Date();
          //check date
          if (time.getDate() < 10) {
              date += "0";
              date += Integer.toString(time.getDate());
          }
          else {
              date += Integer.toString(time.getDate());
          }
          //check month
          if (time.getMonth() < 10) {
              date += "0";
              date += Integer.toString(time.getMonth());
          }
          else {
             date += Integer.toString(time.getMonth()); 
          }
          
          //check year
          date += Integer.toString(time.getYear() -100);
          System.out.println("Year is: " + time.getYear());
          
          //check hour
          if (time.getHours() < 10) {
              date += "0";
              date += Integer.toString(time.getHours());
          }
          else {
             date += Integer.toString(time.getHours()); 
          }
          
          //check minutes
          if (time.getMinutes() < 10) {
              date += "0";
              date += Integer.toString(time.getMinutes()); 
          }
          else {
              date += Integer.toString(time.getMinutes());
          }
          
          //check seconds
          if (time.getSeconds() < 10) {
              date += "0";
              date += Integer.toString(time.getSeconds());
          }
          else {
              date += Integer.toString(time.getSeconds());
          }
          
          System.out.println(date);
          return date;
         
      }
*/

}
