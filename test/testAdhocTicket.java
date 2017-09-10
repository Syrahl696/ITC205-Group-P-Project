package bcccp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.*;

import bcccp.tickets.season.ISeasonTicketDAO;

import java.text.SimpleDateFormat;


public class testAdhocTicket {
	
	IAdhocTicket sut;
	IAdhocTicket but;
	IAdhocTicketDAO dao;
	IAdhocTicketFactory adhocFactory;
	ISeasonTicketDAO seasondao;
	ICarpark carpark;
	
	public STATE state;
    
    public enum STATE {ISSUED, CURRENT, PAID, EXITED };
	
	@Before
	public void setUp() throws Exception {
		//dao and factory cannot be mock objects since they are required to be linked in order to create tickets
		adhocFactory = new AdhocTicketFactory();
		dao = new AdhocTicketDAO(adhocFactory);
		seasondao = mock(ISeasonTicketDAO.class);
		//carpark also cannot be mocked since the getid must match the ticket
		carpark = new Carpark("test carpark", 3, dao, seasondao);

		sut = dao.createTicket("test carpark");	
		
	}
	
	@Test
	public void testConstructor() {
		String carparkName = "test carpark";
		int ticketNo = 1;
		String barcode = "123456";
		
		sut = new AdhocTicket(carparkName, ticketNo, barcode);
		
		assertEquals(sut.getCarparkId(), carparkName);
		assertEquals(sut.getTicketNo(), ticketNo);
		assertEquals(sut.getBarcode(), barcode);
		
	}
	
	@Test
	public void testgetCarparkId() {
		System.out.println("getCarparkId");
		String expResult = "test carpark";
		String result = sut.getCarparkId();
		assertEquals(expResult, result);

	}
	
	@Test
	public void testgetTicketNo() {
		
		sut = adhocFactory.make("test carpark", 2);
		int expResult = 2;
		int result = sut.getTicketNo();
		assertEquals(result, expResult);
	}
	
	@Test
	public void testgetBarcode() {
		sut = adhocFactory.make("test carpark", 2);
		long dateTime = System.currentTimeMillis();
		String formatter = new SimpleDateFormat("ddMMyyyyHHmmss").format(dateTime);
        long date = Long.parseLong(formatter);

        String expBarcode =  "A" + Integer.toHexString(2) + Long.toHexString(date);
        String barcode = sut.getBarcode();
        
        assertEquals(barcode, expBarcode);
	}
	
	
	@Test
	public void testEnter() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456789);
		long expEnterResult = 123456789;
		long enterResult = sut.getEntryDateTime();
		
		assertEquals(enterResult, expEnterResult);
		
	}
	
	@Test
	public void testgetEntryDateTime() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456789);
		long expEnterResult = 123456789;
		long enterResult = sut.getEntryDateTime();
		
		assertEquals(enterResult, expEnterResult);
	}
	
	@Test
	public void testpay() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456780);
		sut.pay(123456789, 10);
		long expDateResult = 123456789;
		double expChargeResult = 10;
		long dateResult = sut.getPaidDateTime();
		double chargeResult = sut.getCharge();
		
		assertTrue(chargeResult == expChargeResult);
		assertEquals(dateResult, expDateResult);
		
		
	}

	@Test
	public void testgetPaidDateTime() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456780);
		sut.pay(123456789, 10);
		long expDateResult = 123456789;
		double expChargeResult = 10;
		long dateResult = sut.getPaidDateTime();
		double chargeResult = sut.getCharge();
		
		assertTrue(chargeResult == expChargeResult);
		assertEquals(dateResult, expDateResult);	
	}
	
	@Test
	public void testisCurrent() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(12345);
		assertTrue(sut.isCurrent());		
		
	}
	
	@Test
	public void testIsPaid() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(12344);
		sut.pay(12345, 10);
		assertTrue(sut.isPaid());	
		
	}
	
	@Test
	public void testExit() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.exit(123456789);
		long expexitResult = 123456789;
		long exitResult = sut.getExitDateTime();
		
		assertEquals(exitResult, expexitResult);
		
	}
	
	@Test
	public void testgetExitDateTime() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.exit(123456789);
		long expexitResult = 123456789;
		long exitResult = sut.getExitDateTime();
		
		assertEquals(exitResult, expexitResult);
		
	}
	
	@Test
	public void testgetCharge() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456780);
		sut.pay(123456789, 10);
		long expDateResult = 123456789;
		double expChargeResult = 10;
		long dateResult = sut.getPaidDateTime();
		double chargeResult = sut.getCharge();
		
		assertTrue(chargeResult == expChargeResult);
		assertEquals(dateResult, expDateResult);
		
	}
	
	@Test
	public void testhasExited() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.exit(12344);
		assertTrue(sut.hasExited());
		
	}
	
	@After
	public void tearDown() throws Exception {
		sut = null;
	}
	
	

}
