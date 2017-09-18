/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit_test.bcccp.tickets.season;

import bcccp.tickets.season.IUsageRecord;
import bcccp.tickets.season.UsageRecord;
import bcccp.tickets.season.UsageRecordFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Corey Schmetzer
 */
public class UsageRecordFactoryTest {
    
    public UsageRecordFactoryTest() {
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
     * Test of make method, of class UsageRecordFactory.
     */
    @Test
    public void testMake() {
        System.out.println("make");
        
        String ticketId = "S1111";
        long startDateTime = 1L;
        
        UsageRecordFactory instance = new UsageRecordFactory();
        instance = new UsageRecordFactory();
       
        IUsageRecord expResult = new UsageRecord ("S1111", 1L);
        
        IUsageRecord result = instance.make(ticketId, startDateTime);
        
        //Asserts that the object is correct implementation
        Assert.assertTrue(result instanceof IUsageRecord);
        assertEquals(expResult.getSeasonTicketId(), result.getSeasonTicketId());
        assertEquals(expResult.getStartTime(), result.getStartTime());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
