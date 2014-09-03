package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.compare.CompareElementInformationalTests;
import rtt.core.tests.junit.compare.CompareElementTests;
import rtt.core.tests.junit.compare.CompareNodeInformationalTests;
import rtt.core.tests.junit.compare.CompareNodeTests;
import rtt.core.tests.junit.compare.CompareOutputTests;

@RunWith(Suite.class)
@SuiteClasses({ CompareOutputTests.class, 
	CompareElementTests.class, CompareElementInformationalTests.class,
	CompareNodeTests.class, CompareNodeInformationalTests.class})
public class AllOutputCompareTests {

}
