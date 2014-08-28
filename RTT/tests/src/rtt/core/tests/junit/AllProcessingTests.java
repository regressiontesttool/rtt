package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.annotations.IndexedFieldProcessingTests;
import rtt.core.tests.junit.annotations.FieldProcessingTests;
import rtt.core.tests.junit.annotations.IndexedMethodProcessingTests;
import rtt.core.tests.junit.annotations.MethodProcessingTests;
import rtt.core.tests.junit.annotations.NamedFieldProcessingTests;

@RunWith(Suite.class)
@SuiteClasses({ FieldProcessingTests.class, 
	IndexedFieldProcessingTests.class, NamedFieldProcessingTests.class,
	MethodProcessingTests.class,
	IndexedMethodProcessingTests.class})
public class AllProcessingTests {

}
