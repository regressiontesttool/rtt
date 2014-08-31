package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllExecutorTests.class, AllOutputCompareTests.class,
		AllProcessingTests.class, ManagerInitializeTests.class,
		DataGeneratorTests.class})
public class AllTests {

}
