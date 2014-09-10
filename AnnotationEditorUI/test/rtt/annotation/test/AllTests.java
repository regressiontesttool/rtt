package rtt.annotation.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.annotation.test.model.AllModelTests;

@RunWith(Suite.class)
@SuiteClasses({ ClassElementAnnotationTests.class,
		CombinedAnnotationRuleTests.class, AllModelTests.class })
public class AllTests {

}
