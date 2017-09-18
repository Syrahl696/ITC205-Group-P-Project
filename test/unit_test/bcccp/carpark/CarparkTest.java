/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit_test.bcccp.carpark;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketFactory;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.*;

/**
 *
 * @author Ryan Smith
 */
public class CarparkTest {
    public CarparkTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {      
    }
    
    @After
    public void tearDown() {
    }
    
    
    
    /**
     * Test of register method, of class Carpark.
     */
    @Test
    public void testConstructor() {
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        when(dummySeasonDAO.getNumberOfTickets()).thenReturn(0);
        
        Carpark instance = new Carpark("Test Carpark", 1, 0, dummyAdhocDAO, dummySeasonDAO);
        
        assertEquals(instance.getName(), "Test Carpark");
        assertFalse(instance.isFull());
        
    }
    
    /**
     * Test of register method, of class Carpark.
     */
    @Test
    public void testRegister() {
        System.out.println("register");
        
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        ICarparkObserver observer = mock(ICarparkObserver.class);
        Carpark instance = new Carpark("Bathurst Chase", 1, 0, dummyAdhocDAO, dummySeasonDAO);
        
        instance.register(observer);
                
        instance.recordAdhocTicketEntry();
        instance.recordAdhocTicketExit();
        
        verify(observer, times(1)).notifyCarparkEvent();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deregister method, of class Carpark.
     */
    @Test
    public void testDeregister() {
        System.out.println("deregister");
        
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        ICarparkObserver observer = mock(ICarparkObserver.class);
        Carpark instance = new Carpark("Bathurst Chase", 1, 0, dummyAdhocDAO, dummySeasonDAO);
        
        instance.register(observer);
        
        instance.deregister(observer);
        
        instance.recordAdhocTicketEntry();
        instance.recordAdhocTicketExit();
        
        verify(observer, times(0)).notifyCarparkEvent();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class Carpark.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 0, dummyAdhocDAO, dummySeasonDAO);

        String expResult = "Bathurst Chase";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isFull method, of class Carpark.
     */
    @Test
    public void testIsFull() {
        System.out.println("isFull");
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 20, 2, dummyAdhocDAO, dummySeasonDAO);
        when(dummySeasonDAO.getNumberOfTickets()).thenReturn(2);
        for (int i = 0; i < 18; i++){
            instance.recordAdhocTicketEntry();
        }

        boolean expResult = true;
        boolean result = instance.isFull();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    @Test
	public void testIssueAdhocTicket() {
		AdhocTicketDAO mockAdhocDAO = mock(AdhocTicketDAO.class);
		SeasonTicketDAO mockSeasonDAO = mock(SeasonTicketDAO.class);
		Carpark sut = new Carpark("test carpark", 3, 0, mockAdhocDAO, mockSeasonDAO);
		IAdhocTicket mockTicket = mock(IAdhocTicket.class);
		
		when(mockAdhocDAO.createTicket("test carpark")).thenReturn(mockTicket);
		
		IAdhocTicket ticket = sut.issueAdhocTicket();
		
		assertEquals(mockTicket, ticket);
	}
	
	@Test
	public void testRecordAdhocTicketEntry() {
		AdhocTicketDAO mockAdhocDAO = mock(AdhocTicketDAO.class);
		SeasonTicketDAO mockSeasonDAO = mock(SeasonTicketDAO.class);
		//1 car space, should be full after RecordAdhocTicketEntry() is called once
		Carpark sut = new Carpark("test carpark", 1, 0, mockAdhocDAO, mockSeasonDAO);
		
		sut.recordAdhocTicketEntry();
		
		//carpark should be full
		assertTrue(sut.isFull() == true);
		
	}
	
	@Test
	public void testRecordAdhocTicketExit() {
		AdhocTicketDAO mockAdhocDAO = mock(AdhocTicketDAO.class);
		SeasonTicketDAO mockSeasonDAO = mock(SeasonTicketDAO.class);

		Carpark sut = new Carpark("test carpark", 1, 0, mockAdhocDAO, mockSeasonDAO);
		ICarparkObserver observer = mock(ICarparkObserver.class);
		sut.register(observer);
		sut.recordAdhocTicketEntry();
		//first check is carpark is full
		assertTrue(sut.isFull() == true);
		
		sut.recordAdhocTicketExit();
		
		//now check if carpark is now not full
		assertTrue(sut.isFull() == false);
                
                verify(observer, times(1)).notifyCarparkEvent();
		
	}
	
	@Test
	public void testGetAdhocTicket() {
		AdhocTicketDAO mockAdhocDAO = mock(AdhocTicketDAO.class);
		SeasonTicketDAO mockSeasonDAO = mock(SeasonTicketDAO.class);
		Carpark sut = new Carpark("test carpark", 3, 0, mockAdhocDAO, mockSeasonDAO);
		IAdhocTicket mockTicket = mock(IAdhocTicket.class);
		
		when(mockAdhocDAO.findTicketByBarcode("barcode")).thenReturn(mockTicket);
		
		IAdhocTicket ticket = sut.getAdhocTicket("barcode");
		
		assertEquals(mockTicket, ticket);
		
	}
	
	@Test
	public void testCaclulateAdhocTicketCharge() {
		AdhocTicketDAO mockAdhocDAO = mock(AdhocTicketDAO.class);
		SeasonTicketDAO mockSeasonDAO = mock(SeasonTicketDAO.class);
		Carpark sut = new Carpark("test carpark", 3, 0, mockAdhocDAO, mockSeasonDAO);
		float charge = (float) 0.0;

		
		Date start = new Date();
		charge = sut.calculateAdhocTicketCharge(start.getTime()-900000);
		
		assertTrue(charge != 0);
	
		
	}

    /**
     * Test of registerSeasonTicket method, of class Carpark.
     */
    @Test
    public void testRegisterSeasonTicket() {
        System.out.println("registerSeasonTicket");
        
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        ISeasonTicket seasonTicket = mock(ISeasonTicket.class);
        
        
        when(seasonTicket.getCarparkId()).thenReturn("Bathurst Chase");
        instance.registerSeasonTicket(seasonTicket);
        
        
        verify(seasonTicket, times(1)).getCarparkId();
        verify(dummySeasonDAO, times(1)).registerTicket(seasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deregisterSeasonTicket method, of class Carpark.
     */
    @Test
    public void testDeregisterSeasonTicket() {
        System.out.println("deregisterSeasonTicket");
         
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        ISeasonTicket seasonTicket = mock(ISeasonTicket.class);
        
        when(seasonTicket.getCarparkId()).thenReturn("Bathurst Chase");
        instance.registerSeasonTicket(seasonTicket);
        
        instance.deregisterSeasonTicket(seasonTicket);
        
        verify(dummySeasonDAO, times(1)).deregisterTicket(seasonTicket);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isSeasonTicketValid method, of class Carpark.
     */
    @Test
    public void testIsSeasonTicketValid() {
        System.out.println("isSeasonTicketValid");       
        
        UsageRecordFactory dummyUsageRecordFactory = mock(UsageRecordFactory.class);
        SeasonTicketDAO dummySeasonDAO = mock(SeasonTicketDAO.class);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        SeasonTicket mockSeason = mock(SeasonTicket.class);

        when(mockSeason.getCarparkId()).thenReturn("Bathurst Chase");
        when(mockSeason.getEndValidPeriod()).thenReturn(999999999999999L);
        when(mockSeason.getId()).thenReturn("S1111");
        instance.registerSeasonTicket(mockSeason);
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        boolean businessHours = false;
                try {
                //sets opening business hours at 7am
                String stringOpeningTime = "07:00:00";
                Date openingTime = sdf.parse(stringOpeningTime);
                Calendar calenderOpeningTime = Calendar.getInstance();
                calenderOpeningTime.setTime(openingTime);

                //sets closing business hours at 7pm
                String stringClosingTime = "19:00:00";
                Date closingTime = sdf.parse(stringClosingTime);
                Calendar calenderClosingTime = Calendar.getInstance();
                calenderClosingTime.setTime(closingTime);

                //sets current time
                Calendar calendarCurrentTime = Calendar.getInstance();
                String stringCurrentTime = sdf.format(calendarCurrentTime.getTime());
                Date currentTime = sdf.parse(stringCurrentTime);
                calendarCurrentTime.setTime(currentTime);

                //tests if current time is between opening time and closing time
                Date current = calendarCurrentTime.getTime();
                if (current.after(calenderOpeningTime.getTime()) && (current.before(calenderClosingTime.getTime()))) {
                    businessHours = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                }
                
                Calendar c = Calendar.getInstance();
                //Retrieves current day as integer from 1-7 Sunday =1, Saturday = 7
                int day= c.get(Calendar.DAY_OF_WEEK);     
                
        boolean expResult = true;
        if (!(businessHours == true) || (day >= 2) && (day <= 6)){
            expResult = false;
        }       
        
        boolean result = instance.isSeasonTicketValid("S1111");
        
        assertEquals(expResult, result);
    }

    /**
     * Test of isSeasonTicketInUse method, of class Carpark.
     */
    @Test
    public void testIsSeasonTicketInUse() {
        System.out.println("isSeasonTicketInUse");
        
        SeasonTicket dummySeason = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, dummySeasonDAO);
        String ticketId = "S1111";
        

        instance.registerSeasonTicket(dummySeason);
        
        instance.recordSeasonTicketEntry(ticketId);

        boolean expResult = true;
        boolean result = instance.isSeasonTicketInUse(ticketId);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordSeasonTicketEntry method, of class Carpark.
     */
    @Test
    public void testRecordSeasonTicketEntry() {
        System.out.println("recordSeasonTicketEntry");
        
        SeasonTicket dummySeason = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicketDAO spySeasonDAO = spy(dummySeasonDAO);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, spySeasonDAO);
        Carpark spyInstance = spy(instance);
        String ticketId = dummySeason.getId();
        
        
        spyInstance.registerSeasonTicket(dummySeason);
        
        spyInstance.recordSeasonTicketEntry(ticketId);
        //verify(spySeasonDAO, times(1)).findTicketById(ticketId);
        verify(spyInstance, times(1)).isSeasonTicketInUse(ticketId); 
        verify(spySeasonDAO, times(1)).recordTicketEntry(ticketId);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordSeasonTicketExit method, of class Carpark.
     */
    @Test
    public void testRecordSeasonTicketExit() throws InterruptedException {
        System.out.println("recordSeasonTicketExit");
        
        SeasonTicket dummySeason = new SeasonTicket("S1111", "Bathurst Chase", 1504741164243L, 1594242000000L);
        UsageRecordFactory dummyUsageRecordFactory = new UsageRecordFactory ();
        SeasonTicketDAO dummySeasonDAO = new SeasonTicketDAO(dummyUsageRecordFactory);
        SeasonTicketDAO spySeasonDAO = spy(dummySeasonDAO);
        AdhocTicketDAO dummyAdhocDAO = mock(AdhocTicketDAO.class);
        Carpark instance = new Carpark("Bathurst Chase", 3, 3, dummyAdhocDAO, spySeasonDAO);
        Carpark spyInstance = spy(instance);
        String ticketId = dummySeason.getId();
        
        spyInstance.registerSeasonTicket(dummySeason);
        
        spyInstance.recordSeasonTicketEntry(ticketId);
        
        //Implemented second delay to avoid exitTime equalling entryTime
        TimeUnit.SECONDS.sleep(1);
        
        spyInstance.recordSeasonTicketExit(ticketId);
        verify(spyInstance, times(2)).isSeasonTicketInUse(ticketId); 
        verify(spySeasonDAO, times(1)).recordTicketExit(ticketId);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
