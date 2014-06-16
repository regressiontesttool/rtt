package rtt.annotation.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClassElementAnnotationTests.class,
		ChainableAnnotationRuleTest.class,
		ClassElementTests.class })
public class AllTests {

}
