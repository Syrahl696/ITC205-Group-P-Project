/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.tickets.season;

import bcccp.tickets.season.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Corey Schmetzer
 */
public class SeasonTicketIntegrationTest {

    public SeasonTicketIntegrationTest() {
    }
    /**
    * Test of recordUsage method, of class SeasonTicket.
    */
    @Test
    public void testRecordUsage() {
        System.out.println("recordUsage");
        
        ISeasonTicket instance = new SeasonTicket("S1111", "Bathurst Chase", 1L, 9999999999999L);
        UsageRecordFactory factory = new UsageRecordFactory();
        IUsageRecord record = factory.make("S1111", 1L);
        
        //Asserts that the seasonTicket is not currently in use
        assertFalse(instance.inUse());
        assertTrue(record != null);
        
        instance.recordUsage(record);
        
        //Asserts that the seasonTicket is in use after going through method
        assertTrue(instance.inUse());
        
        //Asserts that the seasonTicket has been added to the currentUsageRecords
        assertTrue(instance.getCurrentUsageRecord()!= null);
        assertEquals(instance.getCurrentUsageRecord(), record);
        
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
        UsageRecordFactory factory = new UsageRecordFactory();
        IUsageRecord record = factory.make("S1111", 1L);
        
        instance.recordUsage(record);
        
        IUsageRecord expResult = record;
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
        UsageRecordFactory factory = new UsageRecordFactory();
        IUsageRecord record = factory.make("S1111", 1L);
        
        instance.recordUsage(record);
        
        long dateTime = System.currentTimeMillis();
        
        assertTrue(dateTime > 0);
        assertTrue(instance.getCurrentUsageRecord()!= null);
        assertTrue(instance.getCurrentUsageRecord().getStartTime() < dateTime);
        instance.endUsage(dateTime);
                
        assertNotEquals(record, instance.getCurrentUsageRecord());
        assertFalse(instance.inUse());
        assertTrue(instance.getCurrentUsageRecord() == null);
        assertTrue(instance.getUsageRecords().size() == 1);
        assertEquals(instance.getUsageRecords().get(0), record);
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
        UsageRecordFactory factory = new UsageRecordFactory();
        IUsageRecord record = factory.make("S1111", 1L);
        
        instance.recordUsage(record);
        instance.endUsage(System.currentTimeMillis());
        
        instance.getUsageRecords();
        
        assertTrue(instance.getUsageRecords().size() == 1);
        assertEquals(instance.getUsageRecords().get(0), record);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
