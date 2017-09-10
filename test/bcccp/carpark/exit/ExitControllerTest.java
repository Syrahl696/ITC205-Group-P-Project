/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark.exit;

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
public class ExitControllerTest {
    
    static Carpark mockCarpark;
    static IGate mockGate;
    static ICarSensor mockOS; 
    static ICarSensor mockIS;
    static IExitUI mockUI;
    
    static ExitController seasonInstance;
    static ExitController adhocInstance;
    
    public ExitControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        mockCarpark = mock(Carpark.class);
        mockGate = mock(IGate.class);
	mockOS = mock(ICarSensor.class); 
	mockIS = mock(ICarSensor.class);
	mockUI = mock(IExitUI.class);
        
        when(mockIS.getId()).thenReturn("InsideSensor");
        when(mockIS.carIsDetected()).thenReturn(true);
                
        seasonInstance = new ExitController(mockCarpark, mockGate, mockIS, mockOS, mockUI);
        adhocInstance = new ExitController(mockCarpark, mockGate, mockIS, mockOS, mockUI);
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
     * Test of ticketInserted method, of class ExitController.
     */
    @Test
    public void test1TicketInserted() {
        System.out.println("ticketInserted");
        
        
        String sBarcode = "S1111";
        String aBarcode = "A1111";        
        when(mockCarpark.isSeasonTicketValid(sBarcode)).thenReturn(true);
        when(mockCarpark.isSeasonTicketInUse(sBarcode)).thenReturn(true);
        
        AdhocTicket ticket = mock(AdhocTicket.class);
        when(mockCarpark.getAdhocTicket(aBarcode)).thenReturn(ticket);
        when(ticket.isPaid()).thenReturn(true);

        
        seasonInstance.carEventDetected("InsideSensor", true);
        seasonInstance.ticketInserted(sBarcode);
        
        adhocInstance.carEventDetected("InsideSensor", true);
        adhocInstance.ticketInserted(aBarcode);
        
        verify(mockUI, times(2)).display("Take Processed Ticket");
    }

    /**
     * Test of ticketTaken method, of class ExitController.
     */
    @Test
    public void test2TicketTaken() {
        System.out.println("ticketTaken");
        
        seasonInstance.ticketTaken();
        
        verify(mockGate, times(1)).raise();
        verify(mockUI, times(1)).display("Ticket Taken");
    }

    /**
     * Test of carEventDetected method, of class ExitController.
     */
    @Test
    public void test3CarEventDetected() {
        System.out.println("carEventDetected");
        //SetState checks this directly to switch between Idle and Waiting as appropriate,
        //so I have to simulate a car entering and leaving.
        when(mockIS.carIsDetected()).thenReturn(false);
        ExitController carEventInstance = new ExitController(mockCarpark, mockGate, mockIS, mockOS, mockUI);        
        
        when(mockIS.getId()).thenReturn("InsideSensor");
        when(mockOS.getId()).thenReturn("OutsideSensor");
        String sBarcode = "S1111";
        when(mockCarpark.isSeasonTicketValid(sBarcode)).thenReturn(true);
        when(mockCarpark.isSeasonTicketInUse(sBarcode)).thenReturn(true);
        
        //From Idle to Blocked and back
        carEventInstance.carEventDetected("OutsideSensor", true);
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //From Idle to Waiting
        when(mockIS.carIsDetected()).thenReturn(true); //SetState checks this directly... 
        carEventInstance.carEventDetected("InsideSensor", true);
        
        //Waiting and Processed use the same switch case
        //to transition to Blocked if the inside sensor is tripped
        //and to Idle if the car leaves the outside sensor, and Rejected is not accessed from this method.
        //So I'll only test this code (next 3 lines) once, using the Waiting state.
        
        //From Waiting to Blocked and back
        carEventInstance.carEventDetected("OutsideSensor", true);
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //From Waiting back to Idle
        when(mockIS.carIsDetected()).thenReturn(false); //SetState checks this directly...
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //From Idle through Waiting and Processed to Taken
        when(mockIS.carIsDetected()).thenReturn(true); //SetState checks this directly...
        carEventInstance.carEventDetected("InsideSensor", true);
        carEventInstance.ticketInserted(sBarcode);
        carEventInstance.ticketTaken();
        
        //from Taken back to Idle
        when(mockIS.carIsDetected()).thenReturn(false); //SetState checks this directly...
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //From Idle back to Taken
        when(mockIS.carIsDetected()).thenReturn(true); //SetState checks this directly...
        carEventInstance.carEventDetected("InsideSensor", true);
        carEventInstance.ticketInserted(sBarcode);
        carEventInstance.ticketTaken();
        
        //To Exiting
        carEventInstance.carEventDetected("OutsideSensor", true);
        
        //Back to Taken and back to Exiting
        carEventInstance.carEventDetected("OutsideSensor", false);
        carEventInstance.carEventDetected("OutsideSensor", true);
        
        //To Exited
        when(mockIS.carIsDetected()).thenReturn(false); //SetState checks this directly...
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //Back to Exiting and back to Exited
        carEventInstance.carEventDetected("InsideSensor", true);
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //To Idle after having exited.
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //should have entered Waiting 4 times (after 2 from previous tests)
        verify(mockUI, times(6)).display("Insert Ticket");
        //should have entered Blocked 2 times
        verify(mockUI, times(2)).display("Blocked");
        //should have entered Taken 3 times (after 1 from previous tests)
        verify(mockUI, times(4)).display("Ticket Taken");
        //should have entered Exiting 3 times
        verify(mockUI, times(3)).display("Exiting");
        //should have entered Exited state 2 times
        verify(mockUI, times(2)).display("Exited");
    }
}
    

