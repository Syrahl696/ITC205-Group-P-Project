package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mock;

import bcccp.tickets.adhoc.*;


public class testAdhocTicketFactory {
	
	private AdhocTicketFactory sut = spy(new AdhocTicketFactory());
	IAdhocTicket ticket;
	
	@Test
	//not working yet
	public void testMake() {
		ticket = mock(AdhocTicket.class);
		String carparkId = "test carpark";
		int ticketNo = 1;
		String barcode = "barcode";
		doReturn(ticket).when(sut).makeTicket(eq(carparkId), eq(ticketNo), anyObject());
		
		sut.make(carparkId, ticketNo);


	}
	
	

}
