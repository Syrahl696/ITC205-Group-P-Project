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
    @Ignore
    public void testRegisterSeasonTicket() {
        System.out.println("registerSeasonTicket");
        ISeasonTicket seasonTicket = null;
        Carpark instance = null;
        instance.registerSeasonTicket(seasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deregisterSeasonTicket method, of class Carpark.
     */
    @Ignore
    public void testDeregisterSeasonTicket() {
        System.out.println("deregisterSeasonTicket");
        ISeasonTicket seasonTicket = null;
        Carpark instance = null;
        instance.deregisterSeasonTicket(seasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSeasonTicketValid method, of class Carpark.
     */
    @Ignore
    public void testIsSeasonTicketValid() {
        System.out.println("isSeasonTicketValid");
        String ticketId = "";
        Carpark instance = null;
        boolean expResult = false;
        boolean result = instance.isSeasonTicketValid(ticketId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSeasonTicketInUse method, of class Carpark.
     */
    @Ignore
    public void testIsSeasonTicketInUse() {
        System.out.println("isSeasonTicketInUse");
        String ticketId = "";
        Carpark instance = null;
        boolean expResult = false;
        boolean result = instance.isSeasonTicketInUse(ticketId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recordSeasonTicketEntry method, of class Carpark.
     */
    @Ignore
    public void testRecordSeasonTicketEntry() {
        System.out.println("recordSeasonTicketEntry");
        String ticketId = "";
        Carpark instance = null;
        instance.recordSeasonTicketEntry(ticketId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recordSeasonTicketExit method, of class Carpark.
     */
    @Ignore
    public void testRecordSeasonTicketExit() {
        System.out.println("recordSeasonTicketExit");
        String ticketId = "";
        Carpark instance = null;
        instance.recordSeasonTicketExit(ticketId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
