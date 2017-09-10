/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.tickets.season;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Smitz
 */
public class UsageRecordTest {

    public UsageRecordTest() {
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
     * Test of finalise method, of class UsageRecord.
     */
    @Test
    public void testFinalise() {
        System.out.println("finalise");
        
        UsageRecord instance = new UsageRecord("S1111", 1L);
        long endDateTime = 2L;
        

        instance.finalise(endDateTime);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getStartTime method, of class UsageRecord.
     */
    @Test
    public void testGetStartTime() {
        System.out.println("getStartTime");

        UsageRecord instance = new UsageRecord("S1111", 1L);
        
        long expResult = 1L;
        long result = instance.getStartTime();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getEndTime method, of class UsageRecord.
     */
    @Test
    public void testGetEndTime() {
        System.out.println("getEndTime");

        UsageRecord instance = new UsageRecord("S1111", 1L);
        
        instance.finalise(2L);
        long expResult = 2L;
        long result = instance.getEndTime();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getSeasonTicketId method, of class UsageRecord.
     */
    @Test
    public void testGetSeasonTicketId() {
        System.out.println("getSeasonTicketId");

        UsageRecord instance = new UsageRecord("S1111", 1L);
        
        String expResult = "S1111";
        String result = instance.getSeasonTicketId();
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
