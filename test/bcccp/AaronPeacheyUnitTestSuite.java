package bcccp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

@RunWith
(Suite.class)

@Suite.SuiteClasses
({testCarpark.class, 
    testAdhocTicket.class, 
    testAdhocTicketDAO.class,
    testAdhocTicketFactory.class})

public class AaronPeacheyUnitTestSuite {
	
	public void main(String args[]) {
	Result result = JUnitCore.runClasses(testCarpark.class, testAdhocTicket.class, 
			testAdhocTicketDAO.class, testAdhocTicketFactory.class);

    for (Failure failure : result.getFailures()) {
       System.out.println(failure.toString());
    }
		
    System.out.println(result.wasSuccessful());
 }

}
