package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.*;

import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.SeasonTicketDAO;

import java.text.SimpleDateFormat;

	

public class testCarpark {
	Carpark sut;
	AdhocTicketDAO adhocDAO;
	AdhocTicketFactory adhocFactory;
	IAdhocTicket ticket;
	IAdhocTicket ticket2;
	SeasonTicketDAO seasonDAO;
	
	@Test
	public void testIssueAdhocTicket() {
		adhocDAO = mock(AdhocTicketDAO.class);
		seasonDAO = mock(SeasonTicketDAO.class);
		sut = new Carpark("test carpark", 3, adhocDAO, seasonDAO);
		ticket = mock(AdhocTicket.class);
		
		when(adhocDAO.createTicket("test carpark")).thenReturn(ticket);
		
		ticket2 = sut.issueAdhocTicket();
		
		assertEquals(ticket, ticket2);
	}
	
	@Test
	public void testRecordAdhocTicketEntry() {
		//test carpark size, as well as the notifyCarparkEvent
		
	}
	
	@Test
	public void testRecordAdhocTicketExit() {
		//test carpark size, as well as the notifyCarparkEvent
		
	}
	
	@Test
	public void testGetAdhocTicket() {
		adhocDAO = mock(AdhocTicketDAO.class);
		seasonDAO = mock(SeasonTicketDAO.class);
		sut = new Carpark("test carpark", 3, adhocDAO, seasonDAO);
		ticket = mock(AdhocTicket.class);
		
		when(adhocDAO.findTicketByBarcode("barcode")).thenReturn(ticket);
		
		ticket2 = sut.getAdhocTicket("barcode");
		
		assertEquals(ticket, ticket2);
		
	}
	
	@Test
	public void testCalculateAdhocTicketCharge() {
		//have to test a lot of edge conditions, different days, OOH vs BH etc. 
	}
	
	

}
