/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_test.bcccp;

import integration_test.bcccp.carpark.*;
import integration_test.bcccp.carpark.Paystation.Integration_Carpark_PaystationUseCase;
import integration_test.bcccp.carpark.entry.Integration_EntryController_CarparkSubsystem;
import integration_test.bcccp.carpark.exit.Integration_ExitController_CarparkSubsystem;
import integration_test.bcccp.tickets.adhoc.*;
import integration_test.bcccp.tickets.season.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

/**
 *
 * @author Corey Schmetzer
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SeasonTicketDAOIntegrationTest.class, 
      SeasonTicketIntegrationTest.class,
      UsageRecordFactoryIntegrationTest.class,
      Integration_AdhocTicketDAO_FactoryTicket.class, 
      Integration_Factory_AdhocTicket.class,
      Integration_Carpark_SeasonTicketSubsystem.class,
      Integration_Carpark_AdhocSubSystem.class, 
      Integration_EntryController_CarparkSubsystem.class, 
      Integration_Carpark_PaystationUseCase.class,
      Integration_ExitController_CarparkSubsystem.class})

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
      UsageRecordFactoryIntegrationTest.class,
      Integration_Carpark_PaystationUseCase.class,
      Integration_Carpark_SeasonTicketSubsystem.class,
      Integration_EntryController_CarparkSubsystem.class, 
      Integration_Carpark_AdhocSubSystem.class, 
      Integration_AdhocTicketDAO_FactoryTicket.class, 
      Integration_Factory_AdhocTicket.class,
      Integration_ExitController_CarparkSubsystem.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
    
}
