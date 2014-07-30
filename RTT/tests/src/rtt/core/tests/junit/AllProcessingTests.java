package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.annotations.FieldProcessingTests;
import rtt.core.tests.junit.annotations.MethodProcessingTests;

@RunWith(Suite.class)
@SuiteClasses({ FieldProcessingTests.class, MethodProcessingTests.class })
public class AllProcessingTests {

}
