/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp;

import integration_test.bcccp.tickets.season.SeasonTicketDAOIntegrationTest;
import integration_test.bcccp.tickets.season.SeasonTicketIntegrationTest;
import integration_test.bcccp.tickets.season.UsageRecordFactoryIntegrationTest;
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
@Suite.SuiteClasses({SeasonTicketDAOIntegrationTest.class, 
      SeasonTicketIntegrationTest.class,
      UsageRecordFactoryIntegrationTest.class})

public class CollectiveIntegrationTestSuite {

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
      Result result = JUnitCore.runClasses(
      SeasonTicketDAOIntegrationTest.class, 
      SeasonTicketIntegrationTest.class,
      UsageRecordFactoryIntegrationTest.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
    
}