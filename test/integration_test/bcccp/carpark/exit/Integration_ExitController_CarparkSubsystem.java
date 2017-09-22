/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.carpark.exit;

import bcccp.carpark.*;
import bcccp.carpark.exit.*;
import bcccp.carpark.paystation.*;
import bcccp.tickets.adhoc.*;
import bcccp.tickets.season.*;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.mockito.Mockito.*;


/**
 *
 * @author Corey Schmetzer
 */

public class Integration_ExitController_CarparkSubsystem {
    
    public Integration_ExitController_CarparkSubsystem() {
    }
    /**
     * Test of ticketInserted method, of class ExitController.
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testTicketInserted() throws InterruptedException {
        System.out.println("ticketInserted");
        
        //Create carpark
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark carpark = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        //Mock user interface elements
        IGate dummyGate = mock(IGate.class);
	ICarSensor mockOS = mock(ICarSensor.class); 
	ICarSensor mockIS = mock(ICarSensor.class);
	IExitUI mockUI = mock(IExitUI.class);
        IPaystationUI mockPayUI = mock(IPaystationUI.class);
        
        //initialise behaviour of mocks
        when(mockOS.getId()).thenReturn("InsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
        
        //initialise instance
        IPaystationController paystation = new PaystationController(carpark,mockPayUI );
        ExitController seasonInstance = new ExitController(carpark, dummyGate, mockOS, mockIS, mockUI);
        ExitController adhocInstance = new ExitController(carpark, dummyGate, mockOS, mockIS, mockUI);
        
        //initialise season ticket
        ISeasonTicket seasonTicket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        carpark.registerSeasonTicket(seasonTicket);
        carpark.recordSeasonTicketEntry(seasonTicket.getId());

        
        //initialise adhoc ticket
        IAdhocTicket adhocTicket = adhocTicketDAO.createTicket("Bathurst Chase");
        adhocTicket.enter(System.currentTimeMillis());
        //Implemented second delay to avoid paidTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        paystation.ticketInserted(adhocTicket.getBarcode());
        paystation.ticketPaid();
        paystation.ticketTaken();
        
        //Begin tests
        seasonInstance.carEventDetected("InsideSensor", true);
        seasonInstance.ticketInserted(seasonTicket.getId());
        
        adhocInstance.carEventDetected("InsideSensor", true);      
        adhocInstance.ticketInserted(adhocTicket.getBarcode());
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockUI, times(2)).display("Take Processed Ticket");
        //a beep indicates an error that may not have been caught otherwise
        verify(mockUI, never()).beep();
    }
    /**
     * Test of ticketTaken method, of class ExitController.
     */
    @Test
    public void testTicketTaken() {
        System.out.println("ticketTaken");
          
        //Create carpark
        IAdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());
	ISeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());
					
	Carpark carpark = new Carpark("Bathurst Chase", 10, 1, adhocTicketDAO, seasonTicketDAO);
        
        //Mock user interface elements
        IGate mockGate = mock(IGate.class);
	ICarSensor mockOS = mock(ICarSensor.class); 
	ICarSensor mockIS = mock(ICarSensor.class);
	IExitUI mockUI = mock(IExitUI.class);
        IPaystationUI mockPayUI = mock(IPaystationUI.class);
        
        //initialise behaviour of mocks
        when(mockOS.getId()).thenReturn("InsideSensor");
        when(mockOS.carIsDetected()).thenReturn(true);
        
        //initialise instance
        ExitController seasonInstance = new ExitController(carpark, mockGate, mockOS, mockIS, mockUI);
        
        //initialise season ticket
        ISeasonTicket seasonTicket = new SeasonTicket("S1111","Bathurst Chase", 1L, 99999999999999999L);
        carpark.registerSeasonTicket(seasonTicket);
        carpark.recordSeasonTicketEntry(seasonTicket.getId());
        
        seasonInstance.carEventDetected("InsideSensor", true);
        seasonInstance.ticketInserted(seasonTicket.getId());
        
        //Begin test
        seasonInstance.ticketTaken();
        
        //Test that the system entered the correct states as a result of this method.
        verify(mockGate, times(1)).raise();
        verify(mockUI, times(1)).display("Ticket Taken");
        //a beep indicates an error that may not have been caught otherwise
        verify(mockUI, never()).beep();
    }
}
//Test of carEventDetected method, of class ExitController is not necessary, as this method does not rely on integration with the Carpark at all.
    
