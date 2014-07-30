package rtt.core.tests.junit.annotations;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FieldProcessingTests.class, MethodProcessingTests.class })
public class AllProcessingTests {

}
