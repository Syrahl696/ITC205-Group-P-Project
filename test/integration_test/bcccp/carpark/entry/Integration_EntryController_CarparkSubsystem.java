/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.carpark.entry;

import bcccp.carpark.*;
import bcccp.carpark.entry.EntryController;
import bcccp.carpark.entry.IEntryUI;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketFactory;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;
import org.junit.*;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.*;


/**
 *
 * @author Ryan Smith
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Test methods in sequence, to lower code reuse.
public class Integration_EntryController_CarparkSubsystem {
    
    public Integration_EntryController_CarparkSubsystem() {
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
     * Test of buttonPushed method, of class EntryController.
     */
    @Test
    public void testButtonPushed() {
        System.out.println("buttonPushed");
        
        //Create carpark
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark carpark = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        //Mock user interface elements
        IGate dummyGate = mock(IGate.class);
	ICarSensor mockOS = mock(ICarSensor.class); 
	ICarSensor mockIS = mock(ICarSensor.class);
	IEntryUI mockUI = mock(IEntryUI.class);
        
        //initialise behaviour of mocks
        when(mockOS.getId()).thenReturn("OutsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
        
        EntryController instance = new EntryController(carpark, dummyGate, mockOS, mockIS, mockUI);
        
        //Put the system in the correct state using the appropriate action
        instance.carEventDetected("OutsideSensor", true);
       
        //Begin test
        instance.buttonPushed();
        
        
        //Use a new instance for a branch use case
        Carpark carpark2 = new Carpark("Bathurst Chase", 1, 0, adhocTicketDAO, seasonTicketDAO);
        EntryController instanceIsFull = new EntryController(carpark2, dummyGate, mockOS, mockIS, mockUI);
        
        //Set the state of the new instance to the correct state to test a different case
        carpark2.recordAdhocTicketEntry();
        instanceIsFull.carEventDetected("OutsideSensor", true);
       
        //Begin test
        instanceIsFull.buttonPushed();
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockUI, times(1)).display("Take Ticket");
        verify(mockUI, times(1)).display("Carpark Full");
        //a beep indicates an error that may not have been caught otherwise.
        verify(mockUI, never()).beep();
        
    }


    /**
     * Test of ticketInserted method, of class EntryController.
     */
    @Test
    public void testTicketInserted() {
        System.out.println("ticketInserted");
        
        //Create carpark
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark carpark = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        //Mock user interface elements
        IGate dummyGate = mock(IGate.class);
	ICarSensor mockOS = mock(ICarSensor.class); 
	ICarSensor mockIS = mock(ICarSensor.class);
	IEntryUI mockUI = mock(IEntryUI.class);
        
        //initialise behaviour of mocks
        when(mockOS.getId()).thenReturn("OutsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
        
        //initialise instance
        EntryController instance = new EntryController(carpark, dummyGate, mockOS, mockIS, mockUI);
        
        //initialise season ticket
        ISeasonTicket seasonTicket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        carpark.registerSeasonTicket(seasonTicket);
        
        //Set up mock behaviour
        
        //Begin tests
        instance.carEventDetected("OutsideSensor", true);
        instance.ticketInserted("S1111");
        
        //Test that the system entered the correct state as a result of this method.
        verify(mockUI, times(1)).display("Ticket Validated");
        //a beep indicates an error that may not have been caught otherwise.
        verify(mockUI, never()).beep();
    }

    /**
     * Test of ticketTaken method, of class EntryController.
     */
    @Test
    public void testTicketTaken() {
        System.out.println("ticketTaken");
        
        //Create carpark
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark carpark = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        //Mock user interface elements
        IGate dummyGate = mock(IGate.class);
	ICarSensor mockOS = mock(ICarSensor.class); 
	ICarSensor mockIS = mock(ICarSensor.class);
	IEntryUI mockUI = mock(IEntryUI.class);
        
        //initialise behaviour of mocks
        when(mockOS.getId()).thenReturn("OutsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
        
        //initialise instance
        EntryController instance = new EntryController(carpark, dummyGate, mockOS, mockIS, mockUI);
        instance.buttonPushed();
        
        //Begin tests
        instance.ticketTaken();
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockUI, times(1)).display("Ticket Taken");
        //a beep indicates an error that may not have been caught otherwise.
        verify(mockUI, never()).beep();
    }

    /**
     * Test of notifyCarparkEvent method, of class EntryController.
     */
    @Test
    public void testNotifyCarparkEvent() {
        System.out.println("notifyCarparkEvent");
        
        
        //Create carpark
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark carpark = new Carpark("Bathurst Chase", 1, 0, adhocTicketDAO, seasonTicketDAO);
        
        //Mock user interface elements
        IGate dummyGate = mock(IGate.class);
	ICarSensor mockOS = mock(ICarSensor.class); 
	ICarSensor mockIS = mock(ICarSensor.class);
	IEntryUI mockUI = mock(IEntryUI.class);
        
        //initialise behaviour of mocks
        when(mockOS.getId()).thenReturn("OutsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
        
        EntryController instance = new EntryController(carpark, dummyGate, mockOS, mockIS, mockUI);
        
        //Put the system in the correct state using the appropriate action
        carpark.recordAdhocTicketEntry();
        instance.carEventDetected("OutsideSensor", true);
        instance.buttonPushed();
        carpark.recordAdhocTicketExit();
        
        //Begin test
        //The instance is not registered as an observer to the carpark. 
        //Ordinarily this method would be called in the recordAdhocTicketExit method,
        //but I chose to not register the instance to improve clarity and enable setup-test-assert format in this test.
        instance.notifyCarparkEvent();
        
        //Test that the system entered the correct state as a result of this method.
        //Waiting state should be called total of 2 times.
        verify(mockUI, times(2)).display("Push Button");
        //a beep indicates an error that may not have been caught otherwise.
        verify(mockUI, never()).beep();
    }

    //Test of carEventDetected method, of class EntryController is not necessary, as this method does not rely on integration with the Carpark at all.
    
}
