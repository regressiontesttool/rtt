package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.core.DataGeneratorTests;
import rtt.core.tests.junit.core.ManagerInitializeTests;

@RunWith(Suite.class)
@SuiteClasses({ AllExecutorTests.class, AllOutputCompareTests.class,
		AllProcessingTests.class, ManagerInitializeTests.class,
		DataGeneratorTests.class})
public class AllTests {

}
