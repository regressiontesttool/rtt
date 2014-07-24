package rtt.core.tests.junit.compare;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CompareClassNodeTests.class, CompareNodeTests.class,
		CompareOutputTests.class, CompareValueNodeTests.class })
public class AllOutputCompareTests {

}
