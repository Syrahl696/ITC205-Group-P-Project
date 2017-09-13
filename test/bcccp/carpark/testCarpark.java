package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.tickets.adhoc.*;

import bcccp.tickets.season.SeasonTicketDAO;

import java.util.Date;

	

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
		adhocDAO = mock(AdhocTicketDAO.class);
		seasonDAO = mock(SeasonTicketDAO.class);
		//0 car spaces, force carpark to be full
		sut = new Carpark("test carpark", 1, adhocDAO, seasonDAO);
		ICarparkObserver observer = mock(ICarparkObserver.class);
		sut.register(observer);
		
		sut.recordAdhocTicketEntry();
		
		//carpark should not be full
		assertTrue(sut.isFull() == true);
		
	}
	
	@Test
	public void testRecordAdhocTicketExit() {
		adhocDAO = mock(AdhocTicketDAO.class);
		seasonDAO = mock(SeasonTicketDAO.class);

		sut = new Carpark("test carpark", 1, adhocDAO, seasonDAO);
		ICarparkObserver observer = mock(ICarparkObserver.class);
		sut.register(observer);
		sut.recordAdhocTicketEntry();
		//first check is carpark is full
		assertTrue(sut.isFull() == true);
		
		sut.recordAdhocTicketExit();
		
		//now check if carpark is now not full
		assertTrue(sut.isFull() == false);
		
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
	public void testCaclulateAdhocTicketCharge() {
		adhocDAO = mock(AdhocTicketDAO.class);
		seasonDAO = mock(SeasonTicketDAO.class);
		sut = new Carpark("test carpark", 3, adhocDAO, seasonDAO);
		float charge = (float) 0.0;

		
		Date start = new Date();
		charge = sut.calculateAdhocTicketCharge(start.getTime()-900000);
		
		assertTrue(charge != 0);
	
		
	}
	
	
	@Test
	public void testCalcTicketCharge() {
		adhocDAO = mock(AdhocTicketDAO.class);
		seasonDAO = mock(SeasonTicketDAO.class);
		sut = new Carpark("test carpark", 3, adhocDAO, seasonDAO);
		float charge = (float) 0.0;
		float expectedCharge = (float) 0.0;
		//have to test a lot of edge conditions, different days, OOH vs BH etc. 
		
		
		//both in Business Hours
		long start = 1505084400000L; //9am
		long end = 1505107800000L;  //3:30pm same day
		
		charge = sut.calcCharge(start, end);
		expectedCharge =  (float) 26.0;
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		//both out of Hours
		start = 1505122200000L; //7:30pm
		end = 1505135400000L; //11:10pm same day
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 7.33;  //3hrs, 40mins = 7.something
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		//both OOH then BH
		start = 1505067300000L; //4:15 am
		end = 1505105100000L;  //2:45 pm same day
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 36.5;  //2hrs, 45 of OOH   then 7hrs, 45 of BH
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		
		//BH then OOH
		start = 1505105100000L;  //2:45 pm
		end = 1505126700000L;  //8:45 pm same day
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 20.5;  //4hrs, 15 of BH   then 1hrs, 45 of OOH
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		
		//OOH then BH then OOH
		start = 1505061000000L; //2:30am
		end = 1505132100000L; //10:15pm same day
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 63.5;  //4hrs, 30 of OOH  then 12hrs of BH   then 3hrs 15 of OOH
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		
		//Out of Business Day all OOH
		start = 1504988100000L; //6:15am
		end = 1505017800000L; //2:30pm same day
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 16.5;  //8hrs 15min
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		//a few more tests for multi day stays
		
		//all weekend stay
		start = 1504909800000L;  //Saturday 8:30 am
		end = 1505025000000L;  //Sunday 4:30 pm
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 64;  //15hrs 30 + 16hrs 30 hrs all OOH
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		//weekend to Business Day
		start = 1505025000000L;  //Sunday 4:30pm
		end = 1505111400000L; //Monday 4:30pm
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 67;  //7hrs 30 OOH + 7hrs OOH + 9hrs 30 BH
		
		assertEquals(expectedCharge, charge, 0.001);
		
		
		//all Business Day stay
		start = 1505189700000L; //Tuesday 2:15pm
		end = 1505333700000L; //Thursday 6:15am     //
		
		charge = sut.calcCharge(start, end);
		expectedCharge = (float) 113.5;  //4:45 BH + 5 OOH + 7 OOH + 12 BH + 5 OOH + 6:15 OOH  = 46.5 + 92
		
		assertEquals(expectedCharge, charge, 0.001);
		
	}
	
	

}
