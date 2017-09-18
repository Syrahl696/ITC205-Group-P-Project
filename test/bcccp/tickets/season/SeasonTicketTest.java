/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.tickets.season;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Corey Schmetzer
 */
public class SeasonTicketTest {

    public SeasonTicketTest() {
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
     * Test of getId method, of class SeasonTicket.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);

        String expResult = "S1111";
        String result = instance.getId();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getCarparkId method, of class SeasonTicket.
     */
    @Test
    public void testGetCarparkId() {
        System.out.println("getCarparkId");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);

        String expResult = "Bathurst Chase";
        String result = instance.getCarparkId();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getStartValidPeriod method, of class SeasonTicket.
     */
    @Test
    public void testGetStartValidPeriod() {
        System.out.println("getStartValidPeriod");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        
        long expResult = 1L;
        long result = instance.getStartValidPeriod();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getEndValidPeriod method, of class SeasonTicket.
     */
    @Test
    public void testGetEndValidPeriod() {
        System.out.println("getEndValidPeriod");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        
        long expResult = 9999999999999L;
        long result = instance.getEndValidPeriod();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of inUse method, of class SeasonTicket.
     */
    @Test
    public void testInUse() {
        System.out.println("inUse");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        IUsageRecord dummyRecord = mock(UsageRecord.class);
        
        instance.recordUsage(dummyRecord);
        
        //Expected result of false by default but after entering a valid record is true
        boolean expResult = true;
        boolean result = instance.inUse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of recordUsage method, of class SeasonTicket.
     */
    @Test
    public void testRecordUsage() {
        System.out.println("recordUsage");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        IUsageRecord dummyRecord = mock(UsageRecord.class);
        
        //Asserts that the seasonTicket is not currently in use
        assertFalse(instance.inUse());
        
        instance.recordUsage(dummyRecord);
        
        //Asserts that the seasonTicket is in use after going through method
        assertTrue(instance.inUse());
        
        //Asserts that the seasonTicket has been added to the currentUsageRecords
        dummyRecord = instance.getCurrentUsageRecord();
        assertTrue(dummyRecord != null);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentUsageRecord method, of class SeasonTicket.
     */
    @Test
    public void testGetCurrentUsageRecord() {
        System.out.println("getCurrentUsageRecord");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        IUsageRecord dummyRecord = mock(UsageRecord.class);
        
        instance.recordUsage(dummyRecord);
        
        IUsageRecord expResult = dummyRecord;
        IUsageRecord result = instance.getCurrentUsageRecord();
        
        assertTrue(instance.inUse());
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of endUsage method, of class SeasonTicket.
     */
    @Test
    public void testEndUsage() {
        System.out.println("endUsage");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        IUsageRecord dummyRecord = mock(UsageRecord.class);
        
        instance.recordUsage(dummyRecord);
        
        long dateTime = System.currentTimeMillis();
        
        instance.endUsage(dateTime);
                
        dummyRecord = instance.getCurrentUsageRecord();
        assertFalse(instance.inUse());
        assertTrue(dummyRecord == null);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUsageRecords method, of class SeasonTicket.
     */
    @Test
    public void testGetUsageRecords() {
        System.out.println("getUsageRecords");
        
        SeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        IUsageRecord dummyRecord = mock(UsageRecord.class);
        List<IUsageRecord> mockList = new ArrayList<>();
        mockList.add(dummyRecord);
        
        instance.recordUsage(dummyRecord);
        instance.endUsage(System.currentTimeMillis());
        
        List<IUsageRecord> expResult = mockList;
        List<IUsageRecord> result = instance.getUsageRecords();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
