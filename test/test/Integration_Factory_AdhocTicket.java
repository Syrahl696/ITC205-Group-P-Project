/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
    
    IAdhocTicket ticket = new AdhocTicket("placeholder", 1, "placeholder");
    IAdhocTicket spyTicket = spy(ticket);
    IAdhocTicket ticket2;
    IAdhocTicketFactory factory;
    
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
        spyTicket = factory.make("test carpark", 1);
        
        ticket2 = new AdhocTicket("test carpark", 1, "barcode");
        
        assertEquals(spyTicket.getCarparkId(), ticket2.getCarparkId());
        assertEquals(spyTicket.getTicketNo(), ticket2.getTicketNo());
        
        //fails here
        //verify(spyTicket).enter(anyLong());
        
        //verify ticket has a barcode
        //verify ticket has an entry time greater than o
        //verify that entry() inside ticket has been called
        //check if first few digits of barcode match                                        
        
        
        
    }
    
    
}
