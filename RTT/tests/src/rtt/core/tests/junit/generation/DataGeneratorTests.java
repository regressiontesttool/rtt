package rtt.core.tests.junit.generation;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Parser;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;

@SuppressWarnings("unused")
public class DataGeneratorTests {
	
	private List<String> params;
	private Input input;
	
	@Before
	public void setUp() {
		input = new Input();
		input.setValue("This is a testing input");
		
		params = new ArrayList<>();
	}
	
//	private void printNode(int level, Node node) {
//		StringBuilder builder = new StringBuilder();
//		builder.append("|");
//		for (int i = 0; i < level; i++) {
//			builder.append("   |");
//		}
//		builder.append("-- ");
//		if (node.isInformational()) {
//			builder.append("[Info] ");
//		}
//		
//		builder.append(node.getGeneratorName());
//		builder.append(" - ");
//		builder.append(node.getGeneratorType());
//		builder.append(" (" + node.getClass().getSimpleName() + "): ");
//		
//		if (node.isIsNull()) {
//			builder.append("<null>");
//		}
//		
//		if (node instanceof ValueNode) {
//			builder.append(((ValueNode) node).getValue());
//		}
//		
//		if (node instanceof ClassNode) {
//			builder.append(((ClassNode) node).getFullName());
//		}
//		
//		System.out.println(builder.toString());
//		
//		if (node instanceof ClassNode) {
//			for (Node childNode : ((ClassNode) node).getNodes()) {
//				printNode(level + 1, childNode);
//			}
//		}
//	} 
	
	private void generateOutput(Class<?> objectType) throws Exception {
		Executor executor = new Executor(objectType);
		DataGenerator.generateOutput(input, params, executor);
	}	
	
	
	
	
}
