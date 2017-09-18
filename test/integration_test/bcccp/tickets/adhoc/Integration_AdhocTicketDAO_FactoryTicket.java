/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.tickets.adhoc;
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

import bcccp.tickets.adhoc.IAdhocTicketDAO;
import java.util.List;
/**
 *
 * @author PeacheyMacbook
 */

//Integration testing for the AdhocTicketDAO with the Ticket and Factory previously Integrated. 
public class Integration_AdhocTicketDAO_FactoryTicket {
    
    IAdhocTicket ticket;
    IAdhocTicket ticket2;
    IAdhocTicketFactory factory;
    IAdhocTicketDAO ticketDAO;
    
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
    public void testDAOcreateTicket() {
        factory = new AdhocTicketFactory();
        ticketDAO = new AdhocTicketDAO(factory);
        
        //create two tickets, one through DAO and one through ticket constrcutor with same parameters
        ticket = ticketDAO.createTicket("test carpark");
        ticket2 = new AdhocTicket("test carpark", 1, "barcode");
        
        //check if carparkId is not empty
        assertTrue(ticket.getCarparkId() != null);
        
        //test if both tickets contain same given carparkId and TicketNo. 
        assertEquals(ticket.getCarparkId(), ticket2.getCarparkId());
        assertEquals(ticket.getTicketNo(), ticket2.getTicketNo());
        
        
    }
    
    
    @Test
    public void testDAOfindTicketByBarcode() {
        factory = new AdhocTicketFactory();
        ticketDAO = new AdhocTicketDAO(factory);
        
        //create two tickets, one with DAO and one by passing barcode into method under test. 
        ticket = ticketDAO.createTicket("test carpark");
        ticket2 = ticketDAO.findTicketByBarcode(ticket.getBarcode());
        
        //test if tickets equal
        assertEquals(ticket, ticket2);
        
        
        
    }
    
    
    @Test
    public void testDAOgetCurrentTickets() {
        factory = new AdhocTicketFactory();
        ticketDAO = spy(new AdhocTicketDAO(factory));
        
        ticketDAO.createTicket("test carpark");  //create first ticket
        ticketDAO.createTicket("test carpark");  //create second ticket
        
        //verofy both tickets created by DAO.
        verify(ticketDAO, times(2)).createTicket("test carpark");
        
        List<IAdhocTicket> retrievedTickets = ticketDAO.getCurrentTickets();
        
        //test that both tickets placed into HashMap
        assertTrue(retrievedTickets.size() == 2);
        
    }
    
    
    //test constructor??
    
    
    
}
