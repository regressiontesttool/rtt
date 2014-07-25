package rtt.core.tests.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;
import rtt.core.testing.generation.ParserExecutor;

public class DataGeneratorTests {

	@Test
	public void testOutputGeneration() throws Throwable {
		Input input = new Input();
		input.setValue("This is a testing input");
		
		List<String> params = new ArrayList<>();
		
		Configuration config = new Configuration();
		config.setName("SampleConfig");
		config.setParserClass("rtt.core.tests.RepositoryGenerator");
		
		Classpath path = new Classpath();
		path.getPath().add("./binary");
		
		config.setClasspath(path);
		
		Executor parser = DataGenerator.locateParserExecutor(config, ".");
		
		DataGenerator.generateOutput(input, params, parser);
	}

}
