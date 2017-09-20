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
public class UsageRecordFactoryIntegrationTest {
    
    public UsageRecordFactoryIntegrationTest() {
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