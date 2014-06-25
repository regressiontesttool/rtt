package rtt.annotation.test.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AnnotatableTests.class, ClassElementTests.class,
		ClassModelTests.class, ModelElementTests.class })
public class AllModelTests {

}
