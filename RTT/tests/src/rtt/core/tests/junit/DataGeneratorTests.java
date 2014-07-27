package rtt.core.tests.junit;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;

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
		
		Output output = DataGenerator.generateOutput(input, params, parser);
		if (output == null) {
			fail("Output was null.");
		}
		
		for (Node node : output.getNodes()) {
			printNode(0, node);
		}
	}

	private void printNode(int level, Node node) {
		StringBuilder builder = new StringBuilder();
		builder.append("|");
		for (int i = 0; i < level; i++) {
			builder.append("   |");
		}
		builder.append("--");
		builder.append(node.getGeneratorName());
		builder.append(" - ");
		builder.append(node.getGeneratorType());
		builder.append(" (" + node.getClass().getSimpleName() + "): ");
		
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
	
	

}
