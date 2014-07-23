package rtt.core.tests.junit;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.schlichtherle.io.FileInputStream;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.utils.ExecutorLoader;

public class ParserInitializeTests {

	private Configuration config;

	@Before
	public void setUp() throws Exception {
		config = new Configuration();
		config.setName("TestConfiguration");
		config.setParserClass("rtt.tests.RepositoryGenerator");
		
		Classpath classpath = new Classpath();
		classpath.getPath().add("../bin/");
		classpath.getPath().add("test.jar");
		
		config.setClasspath(classpath);		
	}

	@Test
	public void testParserInitializing() throws Exception {
		try (ExecutorLoader loader = new ExecutorLoader(config.getClasspath())) {
			Class<?> parserClass = loader.resolveClass(config.getParserClass());
			
			Constructor<?> constructor = parserClass.getConstructor(InputStream.class);
			if (constructor != null) {
				constructor.setAccessible(true);
				Object parser = constructor.newInstance(new FileInputStream(Paths.get("tests/testsets/ccc/1.testcase").toAbsolutePath().toFile()));
				System.out.println(parser);
			} else {
				System.out.println("Could not find constructor");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
