package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bcccp.tickets.adhoc.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class testAdhocTicketDAO {
	
	AdhocTicketDAO sut;
	
	HashMap<String, IAdhocTicket> currentAdhocTicketsMap;
	IAdhocTicketFactory ticketFactory;
	IAdhocTicket ticket;
	IAdhocTicket ticket2;
	private Map<String, IAdhocTicket> currentAdhocTickets;


	
	@Before
	public void setUp() throws Exception {
		currentAdhocTicketsMap = spy(new HashMap<String, IAdhocTicket>());
		ticketFactory = new AdhocTicketFactory();
		
		sut = new AdhocTicketDAO(ticketFactory);
		
		
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		sut = null;
	}
	
	
	@Test
	public void testCreateTicket() {
		ticketFactory = mock(AdhocTicketFactory.class);
        sut = new AdhocTicketDAO(ticketFactory);

        IAdhocTicket mockTicket = mock(IAdhocTicket.class);
        when((ticketFactory).make("test carpark", 1)).thenReturn(mockTicket);

        ticket = sut.createTicket("test carpark");

        assertEquals(ticket, mockTicket);
	}
	
	
	@Test
	public void testFindTicketByBarcode() {
	ticketFactory = mock(AdhocTicketFactory.class);

        sut = new AdhocTicketDAO(ticketFactory);
        IAdhocTicket mockTicket = mock(IAdhocTicket.class);
        when(ticketFactory.make("test carpark", 1)).thenReturn(mockTicket);
        when(mockTicket.getBarcode()).thenReturn("A111");
        IAdhocTicket ticket = sut.createTicket("test carpark");
        

        IAdhocTicket retrievedTicket = sut.findTicketByBarcode("A111");

        assertEquals(ticket, retrievedTicket);
	}
	
	@Test
	public void testGetCurrentTickets() {
		ticketFactory = new AdhocTicketFactory();
		sut = new AdhocTicketDAO(ticketFactory);
		currentAdhocTickets = new HashMap<>();
		
		ticket = sut.createTicket("test carpark");
		ticket2 = sut.createTicket("test carpark");
		currentAdhocTickets.put(ticket.getBarcode(), ticket);
		currentAdhocTickets.put(ticket2.getBarcode(), ticket2);
		
		List<IAdhocTicket> retrievedTickets = (List<IAdhocTicket>) sut.getCurrentTickets();
		
		List<IAdhocTicket> adhocTickets = (List<IAdhocTicket>) Collections.unmodifiableList(new ArrayList<IAdhocTicket>(currentAdhocTickets.values()));
		
		assertEquals(adhocTickets, retrievedTickets);
		
	}
	

}
