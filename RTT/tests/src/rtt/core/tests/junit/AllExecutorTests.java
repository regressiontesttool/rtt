package rtt.core.tests.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rtt.core.tests.junit.generation.executor.ExecutorCorrectInitializeTests;
import rtt.core.tests.junit.generation.executor.ExecutorCreationTests;
import rtt.core.tests.junit.generation.executor.ExecutorFailingInitializeTests;
import rtt.core.tests.junit.generation.executor.ExecutorInheritanceInitializeTests;

@RunWith(Suite.class)
@SuiteClasses({ ExecutorCreationTests.class,
		ExecutorCorrectInitializeTests.class,
		ExecutorFailingInitializeTests.class,
		ExecutorInheritanceInitializeTests.class})
public class AllExecutorTests {

}
