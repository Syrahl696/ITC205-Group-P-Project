/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.tickets.season;

import bcccp.tickets.season.IUsageRecord;
import bcccp.tickets.season.UsageRecord;
import bcccp.tickets.season.UsageRecordFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Corey Schmetzer
 */
public class UsageRecordFactoryIntegrationTest {
    
    public UsageRecordFactoryIntegrationTest() {
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

    @Test
    public void testMake() {
        UsageRecordFactory factory = new UsageRecordFactory();
        IUsageRecord ticket = factory.make("S1111", 1L);
        UsageRecord ticket2 = new UsageRecord("S1111", 1L);

        assertTrue(ticket instanceof IUsageRecord);
        
        assertTrue(ticket.getStartTime() > 0);
        assertEquals(ticket.getSeasonTicketId(), ticket2.getSeasonTicketId());
        assertEquals(ticket.getStartTime(), ticket2.getStartTime());
    }  
}