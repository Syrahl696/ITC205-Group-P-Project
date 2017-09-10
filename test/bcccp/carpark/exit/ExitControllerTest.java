/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark.exit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryan Smith
 */
public class ExitControllerTest {
    
    public ExitControllerTest() {
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
     * Test of ticketInserted method, of class ExitController.
     */
    @Test
    public void testTicketInserted() {
        System.out.println("ticketInserted");
        String ticketStr = "";
        ExitController instance = null;
        instance.ticketInserted(ticketStr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ticketTaken method, of class ExitController.
     */
    @Test
    public void testTicketTaken() {
        System.out.println("ticketTaken");
        ExitController instance = null;
        instance.ticketTaken();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carEventDetected method, of class ExitController.
     */
    @Test
    public void testCarEventDetected() {
        System.out.println("carEventDetected");
        String detectorId = "";
        boolean detected = false;
        ExitController instance = null;
        instance.carEventDetected(detectorId, detected);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
