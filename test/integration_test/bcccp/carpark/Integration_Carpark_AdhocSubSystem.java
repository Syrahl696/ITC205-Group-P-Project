/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.carpark;
import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import bcccp.tickets.season.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author PeacheyMacbook
 */
public class Integration_Carpark_AdhocSubSystem {
    IAdhocTicket ticket;
    IAdhocTicket ticket2;
    IAdhocTicketDAO adhocDAO;
    ISeasonTicketDAO seasonDAO;
    ICarpark carpark;
    
    @Test
    public void testCarparkIssueAdhocTicket() {
        adhocDAO = new AdhocTicketDAO(new AdhocTicketFactory());
        seasonDAO = new SeasonTicketDAO(new UsageRecordFactory());
        carpark = new Carpark("test carpark", 1, 0, adhocDAO, seasonDAO);
        
        ticket = carpark.issueAdhocTicket();
        
        //check if ticket exists
        assertTrue(ticket != null);

    }
    
    @Test
    public void testCarparkGetAdhocTicket() {
        adhocDAO = new AdhocTicketDAO(new AdhocTicketFactory());
        seasonDAO = new SeasonTicketDAO(new UsageRecordFactory());
        carpark = new Carpark("test carpark", 5, 1, adhocDAO, seasonDAO);
        
        ticket = carpark.issueAdhocTicket();
        ticket2 = carpark.getAdhocTicket(ticket.getBarcode());
        
        //test if tickets equal
        assertEquals(ticket, ticket2);
        
    }
    
    //thorough testing on the calcCharge method has already been complted during unit testing
    //this covered Business Hours, Out of Hours, Weekend and Multi Day stays
    //this test will just ensure the system communicates from the CalculateAdhocTicketCharge to the calcCharge()
    @Test
    public void testCalculateAdhocTicketCharge() throws InterruptedException {
        adhocDAO = new AdhocTicketDAO(new AdhocTicketFactory());
        seasonDAO = new SeasonTicketDAO(new UsageRecordFactory());
        carpark = new Carpark("test carpark", 5, 1, adhocDAO, seasonDAO);
        
        ticket = carpark.issueAdhocTicket();
        
        Date current = new Date();
        float charge = carpark.calculateAdhocTicketCharge(current.getTime()-1000000);
        
        assertTrue(charge > 0);
        
    }
    
    
    
}
