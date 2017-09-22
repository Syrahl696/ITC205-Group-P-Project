package unit_test.bcccp.tickets.adhoc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.*;

import bcccp.tickets.season.ISeasonTicketDAO;



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
		adhocFactory = mock(IAdhocTicketFactory.class);
		dao = mock(IAdhocTicketDAO.class);
		seasondao = mock(ISeasonTicketDAO.class);
		//carpark also cannot be mocked since the getid must match the ticket
		carpark = mock(Carpark.class);

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
		String expResult = "test carpark";
		sut = new AdhocTicket(expResult, 1, "barcode");
		
		String result = sut.getCarparkId();
		assertEquals(expResult, result);

	}
	
	@Test
	public void testgetTicketNo() {
		int expResult = 5;
		sut =  new AdhocTicket("test carpark", expResult, "barcode");
		
		int result = sut.getTicketNo();
		assertEquals(result, expResult);
	}
	
	@Test
	public void testgetBarcode() {
		String expResult = "barcode1234";
		sut =  new AdhocTicket("test carpark", 1, "barcode1234");
		
		String result = sut.getBarcode();
		assertEquals(result, expResult);

	}
	
	
	@Test
	public void testEnter() {
		adhocFactory = new AdhocTicketFactory();
		dao = new AdhocTicketDAO(adhocFactory);
		
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(123456789);
		long expEnterResult = 123456789;
		long enterResult = sut.getEntryDateTime();
		
		assertEquals(enterResult, expEnterResult);
		
	}
	
	@Test
	public void testgetEntryDateTime() {
	
		sut = new AdhocTicket("test carpark", 1, "barcode");
		long expEnterResult = 123456789;
		sut.enter(expEnterResult);
		
		long enterResult = sut.getEntryDateTime();
		
		assertEquals(enterResult, expEnterResult);
	}
	
	@Test
	public void testpay() {		
		sut = new AdhocTicket("test carpark", 1, "barcode");
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
		sut = new AdhocTicket("test carpark", 1, "barcode");
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
		//need to use current real objects since STATE.current is set when ticket initialized by DAO. 
		adhocFactory = new AdhocTicketFactory();
		dao = new AdhocTicketDAO(adhocFactory);
		
		IAdhocTicket sut = dao.createTicket("test carpark");
		sut.enter(12345);
		assertTrue(sut.isCurrent());		
		
	}
	
	@Test
	public void testIsPaid() {
		sut = new AdhocTicket("test carpark", 1, "barcode");
		sut.enter(12344);
		sut.pay(12345, 10);
		assertTrue(sut.isPaid());	
		
	}
	
	@Test
	public void testExit() {
		sut = new AdhocTicket("test carpark", 1, "barcode");
		sut.exit(123456789);
		long expexitResult = 123456789;
		long exitResult = sut.getExitDateTime();
		
		assertEquals(exitResult, expexitResult);
		
	}
	
	@Test
	public void testgetExitDateTime() {
		sut = new AdhocTicket("test carpark", 1, "barcode");
		sut.exit(123456789);
		long expexitResult = 123456789;
		long exitResult = sut.getExitDateTime();
		
		assertEquals(exitResult, expexitResult);
		
	}
	
	@Test
	public void testgetCharge() {
		sut = new AdhocTicket("test carpark", 1, "barcode");
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
		sut = new AdhocTicket("test carpark", 1, "barcode");
		sut.exit(12344);
		assertTrue(sut.hasExited());
		
	}
	
	@After
	public void tearDown() throws Exception {
		sut = null;
	}
	
	

}
