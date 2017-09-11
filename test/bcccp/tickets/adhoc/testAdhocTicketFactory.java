package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.*;

import bcccp.tickets.season.ISeasonTicketDAO;

import java.text.SimpleDateFormat;

public class testAdhocTicketFactory {
	
	AdhocTicketFactory sut = new AdhocTicketFactory();
	IAdhocTicket ticket;
	IAdhocTicket ticket2;
	
	@Test
	//not working yet
	public void testMake() {
		ticket = sut.make("test carpark", 1);
		ticket2 = new AdhocTicket("test carpark", 1, ticket.getBarcode());

		
		assertEquals(ticket.getCarparkId(), ticket2.getCarparkId());
		assertEquals(ticket.getBarcode(), ticket2.getBarcode());
		assertEquals(ticket.getTicketNo(), ticket2.getTicketNo());
	}

}
