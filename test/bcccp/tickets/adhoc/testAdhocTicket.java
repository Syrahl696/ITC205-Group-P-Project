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
        //testing AdhocTicket constructor
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
        //test for getCarparkId
	public void testgetCarparkId() {
		System.out.println("getCarparkId");
		String expResult = "test carpark";
		String result = sut.getCarparkId();
		assertEquals(expResult, result);
	}
	
	@Test
        //test for geTicketNo
	public void testgetTicketNo() {
		
		sut = adhocFactory.make("test carpark", 2);
		int expResult = 2;
		int result = sut.getTicketNo();
		assertEquals(result, expResult);
	}
	
	@Test
        //test for getBarcode
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
        //test for Enter
	public void testEnter() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456789);
		long expEnterResult = 123456789;
		long enterResult = sut.getEntryDateTime();
		
		assertEquals(enterResult, expEnterResult);		
	}
	
	@Test
        //test for getEntryDateTime
	public void testgetEntryDateTime() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456789);
		long expEnterResult = 123456789;
		long enterResult = sut.getEntryDateTime();
		
		assertEquals(enterResult, expEnterResult);
	}
	
	@Test
        //test for pay
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
        //test for getPaidDateTime
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
        //test for isCurrent
	public void testisCurrent() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(12345);
		assertTrue(sut.isCurrent());				
	}
	
	@Test
        //test for isPaid
	public void testIsPaid() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(12344);
		sut.pay(12345, 10);
		assertTrue(sut.isPaid());			
	}
	
	@Test
        //test for Exit
	public void testExit() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.exit(123456789);
		long expexitResult = 123456789;
		long exitResult = sut.getExitDateTime();
		
		assertEquals(exitResult, expexitResult);		
	}
	
	@Test
        //test for getExitDateTime
	public void testgetExitDateTime() {
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.exit(123456789);
		long expexitResult = 123456789;
		long exitResult = sut.getExitDateTime();
		
		assertEquals(exitResult, expexitResult);		
	}
	
	@Test
        //test for getCharge
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
        //test for hasExited
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
