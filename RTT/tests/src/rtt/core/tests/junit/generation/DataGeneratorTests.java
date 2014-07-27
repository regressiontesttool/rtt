package rtt.core.tests.junit.generation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.results.TestFailure;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;

public class DataGeneratorTests {
	
	private List<String> params;
	private Input input;	

	private Executor getExecutor(String name) throws Exception {
		input = new Input();
		input.setValue("This is a testing input");
		
		params = new ArrayList<>();
		
		Configuration config = new Configuration();
		config.setName("SampleConfig");
		config.setParserClass(name);
		
		Classpath path = new Classpath();
		path.getPath().add("./binary");
		
		config.setClasspath(path);
		
		return DataGenerator.locateParserExecutor(config, ".");
	}
	
	private void printNode(int level, Node node) {
		StringBuilder builder = new StringBuilder();
		builder.append("|");
		for (int i = 0; i < level; i++) {
			builder.append("   |");
		}
		builder.append("-- ");
		if (node.isInformational()) {
			builder.append("[Info] ");
		}
		
		builder.append(node.getGeneratorName());
		builder.append(" - ");
		builder.append(node.getGeneratorType());
		builder.append(" (" + node.getClass().getSimpleName() + "): ");
		
		if (node.isIsNull()) {
			builder.append("<null>");
		}
		
		if (node instanceof ValueNode) {
			builder.append(((ValueNode) node).getValue());
		}
		
		if (node instanceof ClassNode) {
			builder.append(((ClassNode) node).getFullName());
		}
		
		System.out.println(builder.toString());
		
		if (node instanceof ClassNode) {
			for (Node childNode : ((ClassNode) node).getNodes()) {
				printNode(level + 1, childNode);
			}
		}
	}
	
	@Test
	public void testOutputGeneration() throws Throwable {
		Executor parser = getExecutor("rtt.core.tests.RepositoryGenerator");
		Output output = DataGenerator.generateOutput(input, params, parser);
		if (output == null) {
			fail("Output was null.");
		}
		
		for (Node node : output.getNodes()) {
			printNode(0, node);
		}
	}
	
	@Test
	public void testCompare() throws Throwable {
		Executor executor = getExecutor("rtt.core.tests.MyParserClass");
		Output refOutput = DataGenerator.generateOutput(input, params, executor);
		Output actualOutput = DataGenerator.generateOutput(input, params, executor);
		
		List<TestFailure> failures = OutputCompare.compareOutput(refOutput, actualOutput, true);
		for (TestFailure testFailure : failures) {
			System.out.println(testFailure.getMessage());
		}
		
		assertTrue(failures.isEmpty());
	}

	
	
	

}
