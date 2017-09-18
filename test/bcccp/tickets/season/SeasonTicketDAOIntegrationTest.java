/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.tickets.season;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Corey Schmetzer
 */
public class SeasonTicketDAOIntegrationTest {
    
    public SeasonTicketDAOIntegrationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
     /**
     * Test of registerTicket method, of class SeasonTicketDAO.
     */
    @Test
    public void testRegisterTicket() {
        System.out.println("registerTicket");
        
        IUsageRecordFactory factory = new UsageRecordFactory();
        ISeasonTicketDAO instance = new SeasonTicketDAO(factory);
        ISeasonTicket ticket = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);

        assertTrue(ticket != null);
        instance.registerTicket(ticket);
        assertTrue(instance.getNumberOfTickets() == 1);
        assertTrue(instance.findTicketById("S1111") == ticket);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
     /**
     * Test of deregisterTicket method, of class SeasonTicketDAO.
     */
    @Test
    public void testDeregisterTicket() {
        System.out.println("deregisterTicket");
        
        IUsageRecordFactory factory = new UsageRecordFactory();
        ISeasonTicketDAO instance = new SeasonTicketDAO(factory);
        ISeasonTicket ticket = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);       
        
        instance.registerTicket(ticket);
        
        assertTrue(instance.findTicketById("S1111") == ticket);
        instance.deregisterTicket(ticket);
        assertTrue(instance.getNumberOfTickets() == 0);
        assertTrue(instance.findTicketById(ticket.getId()) == null);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordTicketEntry method, of class SeasonTicketDAO.
     */
    @Test
    public void testRecordTicketEntry() {
        System.out.println("recordTicketEntry");

        ISeasonTicket ticket = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        ISeasonTicket spyTicket = spy(ticket);
        IUsageRecordFactory factory = new UsageRecordFactory();
        ISeasonTicketDAO instance = new SeasonTicketDAO(factory);
        
        instance.registerTicket(spyTicket);
        String ticketId = spyTicket.getId();
        
        instance.recordTicketEntry(ticketId);        
        IUsageRecord record = spyTicket.getCurrentUsageRecord();
        
        assertTrue(ticketId != null);
        verify(spyTicket).recordUsage(record);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
        /**
     * Test of recordTicketExit method, of class SeasonTicketDAO.
     */
    @Test
    public void testRecordTicketExit() throws InterruptedException {
        System.out.println("recordTicketExit");
        
        ISeasonTicket ticket = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        ISeasonTicket spyTicket = spy(ticket);
        IUsageRecordFactory factory = new UsageRecordFactory();
        ISeasonTicketDAO instance = new SeasonTicketDAO(factory);
        
        instance.registerTicket(spyTicket);
        
        String ticketId = spyTicket.getId();
        instance.recordTicketEntry(ticketId);
                
        //Implemented second delay to avoid exitTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        
        instance.recordTicketExit(ticketId);
        IUsageRecord record = spyTicket.getCurrentUsageRecord();
        
        assertTrue(ticketId != null);
        assertTrue(record == null);
        verify(spyTicket).endUsage(anyLong());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
