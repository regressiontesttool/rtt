package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.compare.CompareClassNodeTests;
import rtt.core.tests.junit.compare.CompareNodeTests;
import rtt.core.tests.junit.compare.CompareOutputTests;
import rtt.core.tests.junit.compare.CompareValueNodeTests;

@RunWith(Suite.class)
@SuiteClasses({ CompareClassNodeTests.class, CompareNodeTests.class,
		CompareOutputTests.class, CompareValueNodeTests.class })
public class AllOutputCompareTests {

}
