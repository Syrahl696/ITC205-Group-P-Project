/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp.tickets.adhoc;

import org.junit.Test;
import static org.junit.Assert.*;

import bcccp.tickets.adhoc.*;

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
        
        //test if ticket has been given barcode containing "A" + ticketNo
        assertEquals((ticket.getBarcode()).substring(0, 2), "A1");
        
        
        
    }
    
    
}
