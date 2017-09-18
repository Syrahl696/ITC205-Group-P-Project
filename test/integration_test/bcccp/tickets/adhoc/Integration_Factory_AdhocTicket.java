/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.tickets.adhoc;

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

import bcccp.tickets.adhoc.*;
import org.mockito.Mockito;

/**
 *
 * @author PeacheyMacbook
 */
public class Integration_Factory_AdhocTicket {
    //integration test of the Factory and AdhocTicket
    //Test Factory.make method using a real AdhocTicket. 

    IAdhocTicketFactory factory;
    IAdhocTicket ticket;
    IAdhocTicket ticket2;
    
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
    public void testFactoryMake() {
        factory = new AdhocTicketFactory();
        
        //create two tickets, one through DAO and one through ticket constructor with same parameters
        ticket = factory.make("test carpark", 1);
        ticket2 = new AdhocTicket("test carpark", 1, "barcode");
        
        //check if carparkID and TicketNo match given parameters
        assertEquals(ticket.getCarparkId(), ticket2.getCarparkId());
        assertEquals(ticket.getTicketNo(), ticket2.getTicketNo());
        
        //check for null/ empty values in carparkId and ticketNo
        assertTrue(ticket.getCarparkId() != null);
        assertTrue(ticket.getTicketNo() > 0);
        
        //test if ticket has been given an EntryDateTime
        assertTrue(ticket.getEntryDateTime() > 0);
        
        //test if ticket has been given barcode containing "A" + ticketNo
        assertEquals((ticket.getBarcode()).substring(0, 2), "A1");
        
        
        
    }
    
    
}
