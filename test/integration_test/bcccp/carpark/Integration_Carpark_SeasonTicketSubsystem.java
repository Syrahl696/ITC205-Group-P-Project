/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.carpark;

import bcccp.carpark.*;
import bcccp.tickets.adhoc.*;
import bcccp.tickets.season.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Ryan Smith
 */
public class Integration_Carpark_SeasonTicketSubsystem {
    /**
     * Test of register method, of class Carpark.
     */
    @Test
    public void testIsFull() {
        System.out.println("isFull");
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
					
	instance.registerSeasonTicket(ticket);
        
        for (int i = 0; i < 9; i++){
            instance.recordAdhocTicketEntry();
        }
        
        boolean result = instance.isFull();
        
        boolean expResult = true;
        
        assertEquals(expResult, result);
    }

    /**
     * Test of registerSeasonTicket method, of class Carpark.
     */
    @Test
    public void testRegisterSeasonTicket() {
        System.out.println("registerSeasonTicket");
        
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
					
	instance.registerSeasonTicket(ticket);
        
        assertEquals(ticket.getCarparkId(), "Bathurst Chase");
        assertEquals(seasonTicketDAO.getNumberOfTickets(), 1);
        assertEquals(seasonTicketDAO.findTicketById("S1111"), ticket);
    }

    /**
     * Test of deregisterSeasonTicket method, of class Carpark.
     */
    @Test
    public void testDeregisterSeasonTicket() {
        System.out.println("deregisterSeasonTicket");
         
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        instance.registerSeasonTicket(ticket);
        
        instance.deregisterSeasonTicket(ticket);
        
        assertEquals(ticket.getCarparkId(), "Bathurst Chase");
        assertEquals(seasonTicketDAO.getNumberOfTickets(), 0);
        assertNull(seasonTicketDAO.findTicketById("S1111"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isSeasonTicketValid method, of class Carpark.
     */
    @Test
    public void testIsSeasonTicketValid() {
        System.out.println("isSeasonTicketValid");       
        
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 20, 2, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket1 = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        instance.registerSeasonTicket(ticket1);
        
        ISeasonTicket ticket2 = new SeasonTicket("S2222","Bathurst Chase", 1L, 10L);
        instance.registerSeasonTicket(ticket2);
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        boolean businessHours = false;
                try {
                //sets opening business hours at 7am
                String stringOpeningTime = "07:00:00";
                Date openingTime = sdf.parse(stringOpeningTime);
                Calendar calenderOpeningTime = Calendar.getInstance();
                calenderOpeningTime.setTime(openingTime);

                //sets closing business hours at 7pm
                String stringClosingTime = "19:00:00";
                Date closingTime = sdf.parse(stringClosingTime);
                Calendar calenderClosingTime = Calendar.getInstance();
                calenderClosingTime.setTime(closingTime);

                //sets current time
                Calendar calendarCurrentTime = Calendar.getInstance();
                String stringCurrentTime = sdf.format(calendarCurrentTime.getTime());
                Date currentTime = sdf.parse(stringCurrentTime);
                calendarCurrentTime.setTime(currentTime);

                //tests if current time is between opening time and closing time
                Date current = calendarCurrentTime.getTime();
                if (current.after(calenderOpeningTime.getTime()) && (current.before(calenderClosingTime.getTime()))) {
                    businessHours = true;
                }
            } catch (ParseException e) {
                }
                
                Calendar c = Calendar.getInstance();
                //Retrieves current day as integer from 1-7 Sunday =1, Saturday = 7
                int day= c.get(Calendar.DAY_OF_WEEK);     
                
        boolean expResult = true;
        if (!(businessHours == true) || (day >= 2) && (day <= 6)){
            expResult = false;
        }       
        
        boolean result = instance.isSeasonTicketValid("S1111");
        boolean result2 = instance.isSeasonTicketValid("S2222");
        
        assertEquals(expResult, result);
        assertFalse(result2);
    }

    /**
     * Test of isSeasonTicketInUse method, of class Carpark.
     */
    @Test
    public void testIsSeasonTicketInUse() {
        System.out.println("isSeasonTicketInUse");
        
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 20, 2, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket1 = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        ISeasonTicket ticket2 = new SeasonTicket("S2222","Bathurst Chase", 1L, 99999999999999999L);
        
        instance.registerSeasonTicket(ticket1);
        instance.registerSeasonTicket(ticket2);

        instance.recordSeasonTicketEntry("S1111");
        
        boolean test1 = instance.isSeasonTicketInUse("S1111");
        boolean test2 = instance.isSeasonTicketInUse("S2222");
        
        assertTrue(test1);
        assertFalse(test2);
    }

    /**
     * Test of recordSeasonTicketEntry method, of class Carpark.
     */
    @Test
    public void testRecordSeasonTicketEntry() {
        System.out.println("recordSeasonTicketEntry");
        
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        instance.registerSeasonTicket(ticket);
        
        instance.recordSeasonTicketEntry("S1111");
        
        assertTrue(ticket.inUse());
    }

    /**
     * Test of recordSeasonTicketExit method, of class Carpark.
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testRecordSeasonTicketExit() throws InterruptedException {
        System.out.println("recordSeasonTicketExit");
        
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark instance = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        ISeasonTicket ticket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        instance.registerSeasonTicket(ticket);
        
        instance.recordSeasonTicketEntry("S1111");
        //Implemented second delay to avoid exitTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        
        
        instance.recordSeasonTicketExit("S1111");
        
        assertFalse(ticket.inUse());
        assertEquals(ticket.getUsageRecords().size(), 1);
    }
}
