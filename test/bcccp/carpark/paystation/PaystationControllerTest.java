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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        
        String barcode = "A1111";        
        AdhocTicket ticket = mock(AdhocTicket.class);
        when(mockCarpark.getAdhocTicket(barcode)).thenReturn(ticket);
        when(mockCarpark.calculateAdHocTicketCharge(any(long.class))).thenReturn(10.00f);
        
        instance.ticketInserted(barcode);
        
        verify(mockUI, times(1)).display("Pay 10.00");
        verify(mockUI, never()).beep();
    }

    /**
     * Test of ticketPaid method, of class PaystationController.
     */
    @Test
    public void test2TicketPaid() {
        System.out.println("ticketPaid");

        instance.ticketPaid();
        
        verify(mockUI, times(1)).display("Paid");
        verify(mockUI, never()).beep();
    }

    /**
     * Test of ticketTaken method, of class PaystationController.
     */
    @Test
    public void test3TicketTaken() {
        System.out.println("ticketTaken");

        instance.ticketTaken();
        
        verify(mockUI, never()).beep();
    }
    
}
