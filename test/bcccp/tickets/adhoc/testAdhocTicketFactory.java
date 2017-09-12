package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;


import bcccp.tickets.adhoc.*;


public class testAdhocTicketFactory {
	
	AdhocTicketFactory sut = new AdhocTicketFactory();
	IAdhocTicket ticket;
	IAdhocTicket ticket2;
	
	@Test
	//test for make, done by comparing created ticket variables with ticket2
	public void testMake() {
		ticket = sut.make("test carpark", 1);
		ticket2 = new AdhocTicket("test carpark", 1, ticket.getBarcode());

		
		assertEquals(ticket.getCarparkId(), ticket2.getCarparkId());
		assertEquals(ticket.getBarcode(), ticket2.getBarcode());
		assertEquals(ticket.getTicketNo(), ticket2.getTicketNo());
	}

}
