package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bcccp.tickets.adhoc.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
		currentAdhocTickets = new HashMap<>();
		
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		sut = null;
	}
	
	
	@Test
	public void testCreateTicket() {
		ticketFactory = new AdhocTicketFactory();
		
		sut = new AdhocTicketDAO(ticketFactory);
		
		ticket = sut.createTicket("test carpark");
		ticket2 = new AdhocTicket("test carpark", 1, ticket.getBarcode());
		
		assertEquals(ticket.getCarparkId(), ticket2.getCarparkId());
		assertEquals(ticket.getBarcode(), ticket2.getBarcode());
		assertEquals(ticket.getTicketNo(), ticket2.getTicketNo());
		//same as the factory issue
		
		//should retunrn a ticket created by the factory. can mock that bit. 
		//has two parameters, not sure how to test that

	}
	
	@Test
	public void testFindTicketByBarcode() {
		ticketFactory = new AdhocTicketFactory();
		
		sut = new AdhocTicketDAO(ticketFactory);
		ticket = sut.createTicket("test carpark");
		String barcode = ticket.getBarcode();
		IAdhocTicket retrievedTicket = sut.findTicketByBarcode(barcode);
		
		assertEquals(ticket, retrievedTicket);	
	}
	
	@Test
	public void testGetCurrentTickets() {
		ticketFactory = new AdhocTicketFactory();
		sut = new AdhocTicketDAO(ticketFactory);
		
		ticket = sut.createTicket("test carpark");
		ticket2 = sut.createTicket("test carpark");
		currentAdhocTickets.put(ticket.getBarcode(), ticket);
		currentAdhocTickets.put(ticket2.getBarcode(), ticket2);
		
		ArrayList<IAdhocTicket> retrievedTickets = (ArrayList<IAdhocTicket>) sut.getCurrentTickets();
		
		ArrayList<IAdhocTicket> adhocTickets = (ArrayList<IAdhocTicket>) Collections.unmodifiableList(new ArrayList<IAdhocTicket>(currentAdhocTickets.values()));
		
		assertEquals(adhocTickets, retrievedTickets);
		
	}
	

}