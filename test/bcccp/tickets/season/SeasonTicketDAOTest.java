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

/**
 *
 * @author Smitz
 */
public class SeasonTicketDAOTest {
    
    public SeasonTicketDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
       /* dummyUsageRecordFactory = mock(UsageRecordFactory.class);
        instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        dummySeasonTicket = mock(SeasonTicket.class);
        HashMap<String, ISeasonTicket> mockMap = new HashMap();
        mockMap.put(dummySeasonTicket.getId(), dummySeasonTicket);
        mockMap.put(dummySeasonTicket.getId(), dummySeasonTicket);*/
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
        //ISeasonTicket ticket = dummySeasonTicket;
        
        instance.registerTicket(dummySeasonTicket);
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
        
        instance.registerTicket(dummySeasonTicket);
        
        instance.deregisterTicket(dummySeasonTicket);
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
        
        HashMap<String, ISeasonTicket> mockMap= new HashMap();
        mockMap.put(dummySeasonTicket.getId(), dummySeasonTicket);
        
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
        IUsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        
        instance.registerTicket(dummySeasonTicket);

        HashMap<String, ISeasonTicket> mockMap= new HashMap();
        mockMap.put(dummySeasonTicket.getId(), dummySeasonTicket);
        
        String ticketId = dummySeasonTicket.getId();
        
        instance.recordTicketEntry(ticketId);
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
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO instance = new SeasonTicketDAO(dummyUsageRecordFactory);
        
        instance.registerTicket(dummySeasonTicket);
        
        HashMap<String, ISeasonTicket> mockMap= new HashMap();
        mockMap.put(dummySeasonTicket.getId(), dummySeasonTicket);
        
        String ticketId = dummySeasonTicket.getId();
        
        instance.recordTicketEntry(ticketId);
        
        
        //Implemented second delay to avoid exitTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        
        instance.recordTicketExit(ticketId);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
