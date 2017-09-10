/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark.entry;

import bcccp.carpark.*;
import bcccp.tickets.adhoc.AdhocTicket;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.*;


/**
 *
 * @author Ryan Smith
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntryControllerTest {
    
    static Carpark mockCarpark;
    static IGate dummyGate;
    static ICarSensor mockOS; 
    static ICarSensor mockIS;
    static IEntryUI mockUI;
    
    static EntryController seasonInstance;
    static EntryController adhocInstance;
    static EntryController isFullInstance;
    
            
    public EntryControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        mockCarpark = mock(Carpark.class);
        dummyGate = mock(IGate.class);
	mockOS = mock(ICarSensor.class); 
	mockIS = mock(ICarSensor.class);
	mockUI = mock(IEntryUI.class);
        
        when(mockOS.getId()).thenReturn("OutsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
                
        seasonInstance = new EntryController(mockCarpark, dummyGate, mockOS, mockIS, mockUI);
        adhocInstance = new EntryController(mockCarpark, dummyGate, mockOS, mockIS, mockUI);
        isFullInstance = new EntryController(mockCarpark, dummyGate, mockOS, mockIS, mockUI);

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
     * Test of buttonPushed method, of class EntryController.
     */
    @Test
    public void test1ButtonPushed() {
        System.out.println("buttonPushed");
        
        //Set up mock behaviour
        when(mockCarpark.isFull()).thenReturn(false);
        
        //Mock a ticket, as this method needs to be passed a ticket from the carpark.
        AdhocTicket ticket = mock(AdhocTicket.class);
        when(mockCarpark.issueAdhocTicket()).thenReturn(ticket);
        
        
        //Put the system in the correct state using the appropriate action
        adhocInstance.carEventDetected("OutsideSensor", true);
       
        //test the class
        adhocInstance.buttonPushed();
        
        
        //Make a new instance for a branch use case
        
        //Set the state of the new insance to the correct state to test a different case
        when(mockCarpark.isFull()).thenReturn(true);
        isFullInstance.carEventDetected("OutsideSensor", true);
       
        //Test!
        isFullInstance.buttonPushed();
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockUI, times(1)).display("Take Ticket");
        verify(mockUI, times(1)).display("Carpark Full");
        
    }


    /**
     * Test of ticketInserted method, of class EntryController.
     */
    @Test
    public void test2TicketInserted() {
        System.out.println("ticketInserted");
        
        
        //Set up mock behaviour
        String barcode = "S1111";
        
        when(mockCarpark.isSeasonTicketValid(barcode)).thenReturn(true);
        when(mockCarpark.isSeasonTicketInUse(barcode)).thenReturn(false);
        
        
        seasonInstance.carEventDetected("OutsideSensor", true);
        seasonInstance.ticketInserted(barcode);
        
        verify(mockUI, times(1)).display("Ticket Validated");
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ticketTaken method, of class EntryController.
     */
    @Test
    public void test3TicketTaken() {
        System.out.println("ticketTaken");

        seasonInstance.ticketTaken();
        adhocInstance.ticketTaken();
        
        verify(mockUI, times(2)).display("Ticket Taken");
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of notifyCarparkEvent method, of class EntryController.
     */
    @Test
    public void test4NotifyCarparkEvent() {
        System.out.println("notifyCarparkEvent");
        
        when(mockCarpark.isFull()).thenReturn(false);
        
        isFullInstance.notifyCarparkEvent();
        
        //verify that the waiting state has been entered 4 times thus far
        //3 times, once each for the 3 instances used for testing, a 4th time by this test
        verify(mockUI, times(4)).display("Push Button");
        
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of carEventDetected method, of class EntryController.
     */
    @Test
    public void test5CarEventDetected() {
        System.out.println("carEventDetected");
        
        //SetState checks this directly to switch between Idle and Waiting as appropriate,
        //so I have to simulate a car entering and leaving.
        when(mockOS.carIsDetected()).thenReturn(false);
        EntryController carEventInstance = new EntryController(mockCarpark, dummyGate, mockOS, mockIS, mockUI);        
        
        when(mockOS.getId()).thenReturn("OutsideSensor");
        when(mockIS.getId()).thenReturn("InsideSensor");
        
        
        //From Idle to Blocked and back
        carEventInstance.carEventDetected("InsideSensor", true);
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //From Idle to Waiting
        when(mockOS.carIsDetected()).thenReturn(true); //SetState checks this directly... 
        carEventInstance.carEventDetected("OutsideSensor", true);
        
        //Waiting, Full, Issued, and Validated all use the same switch case
        //to transition to Blocked if the inside sensor is tripped
        //and to Idle if the car leaves the outside sensor.
        //So I'll only test this code (newt 3 lines) once, using the Waiting state.
        
        //From Waiting to Blocked and back
        carEventInstance.carEventDetected("InsideSensor", true);
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //From Waiting back to Idle
        when(mockOS.carIsDetected()).thenReturn(false); //SetState checks this directly...
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //From Idle through Waiting and Issued to Taken
        when(mockOS.carIsDetected()).thenReturn(true); //SetState checks this directly...
        carEventInstance.carEventDetected("OutsideSensor", true);
        carEventInstance.buttonPushed();
        carEventInstance.ticketTaken();
        
        //from Taken back to Idle
        when(mockOS.carIsDetected()).thenReturn(false); //SetState checks this directly...
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //From Idle back to Taken
        when(mockOS.carIsDetected()).thenReturn(true); //SetState checks this directly...
        carEventInstance.carEventDetected("OutsideSensor", true);
        carEventInstance.buttonPushed();
        carEventInstance.ticketTaken();
        
        //To Entering
        carEventInstance.carEventDetected("InsideSensor", true);
        
        //Back to Taken and back to Entering
        carEventInstance.carEventDetected("InsideSensor", false);
        carEventInstance.carEventDetected("InsideSensor", true);
        
        //To Entered
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //Back to Entering and back to Entered
        carEventInstance.carEventDetected("OutsideSensor", true);
        carEventInstance.carEventDetected("OutsideSensor", false);
        
        //To Idle after having entered.
        when(mockOS.carIsDetected()).thenReturn(false); //SetState checks this directly...
        carEventInstance.carEventDetected("InsideSensor", false);
        
        //should have entered Waiting 4 times (after 4 from previous tests)
        verify(mockUI, times(8)).display("Push Button");
        //should have entered Blocked 2 times
        verify(mockUI, times(2)).display("Blocked");
        //should have entered Taken 3 times (after 2 from previous tests)
        verify(mockUI, times(5)).display("Ticket Taken");
        //should have entered Entering 3 times
        verify(mockUI, times(3)).display("Entering");
        //should have entered Entered state 2 times
        verify(mockUI, times(2)).display("Entered");
    }
    
}
