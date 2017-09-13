/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp;

import bcccp.carpark.CarparkTest;
import bcccp.tickets.season.SeasonTicketDAOTest;
import bcccp.tickets.season.SeasonTicketTest;
import bcccp.tickets.season.UsageRecordFactoryTest;
import bcccp.tickets.season.UsageRecordTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

/**
 *
 * @author Ryan Smith
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CarparkTest.class, 
    SeasonTicketDAOTest.class, 
    SeasonTicketTest.class,
    UsageRecordFactoryTest.class,
    UsageRecordTest.class })
public class NewTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    public static void main(String[] args) {
      Result result = JUnitCore.runClasses(CarparkTest.class, 
    SeasonTicketDAOTest.class, 
    SeasonTicketTest.class,
    UsageRecordFactoryTest.class,
    UsageRecordTest.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
    
}