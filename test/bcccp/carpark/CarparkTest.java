/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark;

import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.*;

/**
 *
 * @author Ryan Smith
 */
public class CarparkTest {
    public CarparkTest() {
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
     * Test of register method, of class Carpark.
     */
    @Test
    public void testRegister() {
        System.out.println("register");
        
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        ICarparkObserver observer = mock(ICarparkObserver.class);
        Carpark instance = new Carpark("Bathurst Chase", 1, 0, dummyAdhocDAO, dummySeasonDAO);
        
        instance.register(observer);
                
        instance.recordAdhocTicketEntry();
        instance.recordAdhocTicketExit();
        
        verify(observer, times(1)).notifyCarparkEvent();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deregister method, of class Carpark.
     */
    @Test
    public void testDeregister() {
        System.out.println("deregister");
        
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        ICarparkObserver observer = mock(ICarparkObserver.class);
        Carpark instance = new Carpark("Bathurst Chase", 1, 0, dummyAdhocDAO, dummySeasonDAO);
        
        instance.register(observer);
        
        instance.deregister(observer);
        
        instance.recordAdhocTicketEntry();
        instance.recordAdhocTicketExit();
        
        verify(observer, times(0)).notifyCarparkEvent();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class Carpark.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 0, dummyAdhocDAO, dummySeasonDAO);

        String expResult = "Bathurst Chase";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isFull method, of class Carpark.
     */
    @Test
    public void testIsFull() {
        System.out.println("isFull");
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 20, 2, dummyAdhocDAO, dummySeasonDAO);
        when(dummySeasonDAO.getNumberOfTickets()).thenReturn(2);
        for (int i = 0; i < 18; i++){
            instance.recordAdhocTicketEntry();
        }

        boolean expResult = true;
        boolean result = instance.isFull();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of issueAdhocTicket method, of class Carpark.
     */
    @Ignore
    public void testIssueAdhocTicket() {
        System.out.println("issueAdhocTicket");
        Carpark instance = null;
        IAdhocTicket expResult = null;
        IAdhocTicket result = instance.issueAdhocTicket();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recordAdhocTicketEntry method, of class Carpark.
     */
    @Ignore
    public void testRecordAdhocTicketEntry() {
        System.out.println("recordAdhocTicketEntry");
        Carpark instance = null;
        instance.recordAdhocTicketEntry();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdhocTicket method, of class Carpark.
     */
    @Ignore
    public void testGetAdhocTicket() {
        System.out.println("getAdhocTicket");
        String barcode = "";
        Carpark instance = null;
        IAdhocTicket expResult = null;
        IAdhocTicket result = instance.getAdhocTicket(barcode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateAdHocTicketCharge method, of class Carpark.
     */
    @Ignore
    public void testCalculateAdHocTicketCharge() {
        System.out.println("calculateAdHocTicketCharge");
        long entryDateTime = 0L;
        Carpark instance = null;
        float expResult = 0.0F;
        float result = instance.calculateAdHocTicketCharge(entryDateTime);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recordAdhocTicketExit method, of class Carpark.
     */
    @Ignore
    public void testRecordAdhocTicketExit() {
        System.out.println("recordAdhocTicketExit");
        Carpark instance = null;
        instance.recordAdhocTicketExit();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerSeasonTicket method, of class Carpark.
     */
    @Test
    public void testRegisterSeasonTicket() {
        System.out.println("registerSeasonTicket");
        
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        ISeasonTicket seasonTicket = mock(ISeasonTicket.class);
        
        
        when(seasonTicket.getCarparkId()).thenReturn("Bathurst Chase");
        instance.registerSeasonTicket(seasonTicket);
        
        
        verify(seasonTicket, times(1)).getCarparkId();
        verify(dummySeasonDAO, times(1)).registerTicket(seasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deregisterSeasonTicket method, of class Carpark.
     */
    @Test
    public void testDeregisterSeasonTicket() {
        System.out.println("deregisterSeasonTicket");
         
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        ISeasonTicket seasonTicket = mock(ISeasonTicket.class);
        
        when(seasonTicket.getCarparkId()).thenReturn("Bathurst Chase");
        instance.registerSeasonTicket(seasonTicket);
        
        instance.deregisterSeasonTicket(seasonTicket);
        
        verify(dummySeasonDAO, times(1)).deregisterTicket(seasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isSeasonTicketValid method, of class Carpark.
     */
    @Test
    public void testIsSeasonTicketValid() {
        System.out.println("isSeasonTicketValid");       
        
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        SeasonTicket dummySeason = mock(SeasonTicket.class);

        when(dummySeason.getCarparkId()).thenReturn("Bathurst Chase");
        when(dummySeason.getEndValidPeriod()).thenReturn(999999999999999L);
        instance.registerSeasonTicket(dummySeason);
        
        String ticketId = dummySeason.getId();
        boolean expResult = true;
        boolean result = instance.isSeasonTicketValid(ticketId);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isSeasonTicketInUse method, of class Carpark.
     */
    @Test
    public void testIsSeasonTicketInUse() {
        System.out.println("isSeasonTicketInUse");
        
        SeasonTicket dummySeason = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        String ticketId = "S1111";
        

        instance.registerSeasonTicket(dummySeason);
        
        instance.recordSeasonTicketEntry(ticketId);

        boolean expResult = true;
        boolean result = instance.isSeasonTicketInUse(ticketId);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordSeasonTicketEntry method, of class Carpark.
     */
    @Test
    public void testRecordSeasonTicketEntry() {
        System.out.println("recordSeasonTicketEntry");
        
        SeasonTicket dummySeason = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicketDAO spySeasonDAO = spy(dummySeasonDAO);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, spySeasonDAO);
        Carpark spyInstance = spy(instance);
        String ticketId = dummySeason.getId();
        
        
        spyInstance.registerSeasonTicket(dummySeason);
        
        spyInstance.recordSeasonTicketEntry(ticketId);
        //verify(spySeasonDAO, times(1)).findTicketById(ticketId);
        verify(spyInstance, times(1)).isSeasonTicketInUse(ticketId); 
        verify(spySeasonDAO, times(1)).recordTicketEntry(ticketId);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordSeasonTicketExit method, of class Carpark.
     */
    @Test
    public void testRecordSeasonTicketExit() throws InterruptedException {
        System.out.println("recordSeasonTicketExit");
        
        SeasonTicket dummySeason = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicketDAO spySeasonDAO = spy(dummySeasonDAO);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, spySeasonDAO);
        Carpark spyInstance = spy(instance);
        String ticketId = dummySeason.getId();
        
        spyInstance.registerSeasonTicket(dummySeason);
        
        spyInstance.recordSeasonTicketEntry(ticketId);
        
        //Implemented second delay to avoid exitTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        
        spyInstance.recordSeasonTicketExit(ticketId);
        verify(spyInstance, times(2)).isSeasonTicketInUse(ticketId); 
        verify(spySeasonDAO, times(1)).recordTicketExit(ticketId);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
