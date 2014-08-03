package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.compare.CompareElementTests;
import rtt.core.tests.junit.compare.CompareNodeTests;
import rtt.core.tests.junit.compare.CompareValueTests;

@RunWith(Suite.class)
//@SuiteClasses({ CompareClassNodeTests.class, CompareNodeTests.class,
//		CompareOutputTests.class, CompareValueNodeTests.class })
@SuiteClasses({ CompareElementTests.class, CompareValueTests.class,
	CompareNodeTests.class})
public class AllOutputCompareTests {

}
