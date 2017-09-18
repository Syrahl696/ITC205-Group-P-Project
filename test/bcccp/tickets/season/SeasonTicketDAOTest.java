/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.tickets.season;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import static net.bytebuddy.matcher.ElementMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Corey Schmetzer
 */
public class SeasonTicketDAOTest {
    
    public SeasonTicketDAOTest() {
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
        
        UsageRecordFactory dummyUsageRecordFactory = mock(UsageRecordFactory.class);
        SeasonTicketDAO instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicket dummySeasonTicket = mock(SeasonTicket.class);
        
        when(dummySeasonTicket.getId()).thenReturn("S1111");
        instance.registerTicket(dummySeasonTicket);
        assertTrue(instance.getNumberOfTickets() == 1);
        assertTrue(instance.findTicketById("S1111") == dummySeasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deregisterTicket method, of class SeasonTicketDAO.
     */
    @Test
    public void testDeregisterTicket() {
        System.out.println("deregisterTicket");
        
        UsageRecordFactory dummyUsageRecordFactory = mock(UsageRecordFactory.class);
        SeasonTicketDAO instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicket dummySeasonTicket = mock(SeasonTicket.class);
        String ticketId = "S1111";
        
        when(dummySeasonTicket.getId()).thenReturn("S1111");
        instance.registerTicket(dummySeasonTicket);
        
        instance.deregisterTicket(dummySeasonTicket);
        assertTrue(instance.getNumberOfTickets() == 0);
        assertTrue(instance.findTicketById("S1111") == null);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfTickets method, of class SeasonTicketDAO.
     */
    @Test
    public void testGetNumberOfTickets() {
        System.out.println("getNumberOfTickets");
        
        UsageRecordFactory dummyUsageRecordFactory = mock(UsageRecordFactory.class);
        SeasonTicketDAO instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicket dummySeasonTicket = mock(SeasonTicket.class);
        
        HashMap<String, ISeasonTicket> mockMap= new HashMap();
        mockMap.put(dummySeasonTicket.getId(), dummySeasonTicket);
        
        instance.registerTicket(dummySeasonTicket);
              
        int expResult = mockMap.size();
        int result = instance.getNumberOfTickets();

        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of findTicketById method, of class SeasonTicketDAO.
     */
    @Test
    public void testFindTicketById() {
        System.out.println("findTicketById");
        
        UsageRecordFactory dummyUsageRecordFactory = mock(UsageRecordFactory.class);
        SeasonTicketDAO instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicket dummySeasonTicket = mock(SeasonTicket.class);

        instance.registerTicket(dummySeasonTicket);
        
        String ticketId = dummySeasonTicket.getId();
        ISeasonTicket expResult = dummySeasonTicket;
        ISeasonTicket result = instance.findTicketById(ticketId);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordTicketEntry method, of class SeasonTicketDAO.
     */
    @Test
    public void testRecordTicketEntry() {
        System.out.println("recordTicketEntry");

        SeasonTicket dummySeasonTicket = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        ISeasonTicket spySeason = spy(dummySeasonTicket);
        IUsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory();
        IUsageRecordFactory spyUsageRecordFactory = spy(dummyUsageRecordFactory);
        SeasonTicketDAO instance = new SeasonTicketDAO(spyUsageRecordFactory);
        
        instance.registerTicket(spySeason);

        String ticketId = spySeason.getId();
        
        instance.recordTicketEntry(ticketId);
        assertTrue(ticketId != null);
        verify(spyUsageRecordFactory, times(1)).make(ticketId, System.currentTimeMillis());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordTicketExit method, of class SeasonTicketDAO.
     */
    @Test
    public void testRecordTicketExit() throws InterruptedException {
        System.out.println("recordTicketEntry");
        
        SeasonTicket dummySeasonTicket = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        ISeasonTicket spySeason = spy(dummySeasonTicket);
        IUsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory();
        IUsageRecordFactory spyUsageRecordFactory = spy(dummyUsageRecordFactory);
        SeasonTicketDAO instance = new SeasonTicketDAO(spyUsageRecordFactory);
        
        instance.registerTicket(dummySeasonTicket);
        
        String ticketId = dummySeasonTicket.getId();
        instance.recordTicketEntry(ticketId);
                
        //Implemented second delay to avoid exitTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        
        instance.recordTicketExit(ticketId);
        assertTrue(ticketId != null);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
