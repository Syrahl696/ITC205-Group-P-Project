/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark.paystation;

import bcccp.carpark.*;
import bcccp.tickets.adhoc.AdhocTicket;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.*;

/**
 *
 * @author Ryan Smith
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Test methods in sequence, to lower code reuse.
public class PaystationControllerTest {
    
    static Carpark mockCarpark;
    static IPaystationUI mockUI;
    static PaystationController instance;
    
    public PaystationControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        mockCarpark = mock(Carpark.class);
        mockUI = mock(IPaystationUI.class);
        
        //create a static instance for testing multiple methods in sequence
        instance = new PaystationController(mockCarpark, mockUI);
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
     * Test of ticketInserted method, of class PaystationController.
     */
    @Test
    public void test1TicketInserted() {
        System.out.println("ticketInserted");
        
        //Initialise behaviour of mocks
        String barcode = "A1111";        
        AdhocTicket ticket = mock(AdhocTicket.class);
        when(mockCarpark.getAdhocTicket(barcode)).thenReturn(ticket);
        when(mockCarpark.calculateAdHocTicketCharge(any(long.class))).thenReturn(10.00f);
        
        //Begin test
        instance.ticketInserted(barcode);
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockUI, times(1)).display("Pay 10.00");
        //a beep indicates an error that may not have been caught otherwise.
        verify(mockUI, never()).beep();
    }

    /**
     * Test of ticketPaid method, of class PaystationController.
     */
    @Test
    public void test2TicketPaid() {
        System.out.println("ticketPaid");
        
        //Begin test
        instance.ticketPaid();
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockUI, times(1)).display("Paid");
        //a beep indicates an error that may not have been caught otherwise.
        verify(mockUI, never()).beep();
    }

    /**
     * Test of ticketTaken method, of class PaystationController.
     */
    @Test
    public void test3TicketTaken() {
        System.out.println("ticketTaken");

        instance.ticketTaken();
        
        //Internally changes state to Waiting. No public methods are called to verify this change.
        //a beep indicates an error that may not have been caught otherwise.        
        verify(mockUI, never()).beep();
    }
    
}
