package rtt.core.tests.junit.generation.executor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ExecutorCreationTests.class,
		ExecutorCorrectInitializeTests.class,
		ExecutorFailingInitializeTests.class })
public class AllExecutorTests {

}
