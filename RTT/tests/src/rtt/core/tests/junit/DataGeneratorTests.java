package rtt.core.tests.junit;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.Node.Initialize;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;

public class DataGeneratorTests {

	private Input input;
	private List<String> params;

	@Before
	public void setUp() throws Exception {
		input = new Input();
		input.setValue("Das ist ein Testinput.");		
		params = new ArrayList<>();
	}
	
	private Node generateInitNode(Class<?> initObjectType, int childCount) throws Throwable {		
		Executor executor = new Executor(initObjectType);
		Output output = DataGenerator.generateOutput(input, params, executor);
		
		Node initNode = (Node) output.getInitialElement();
		assertNotNull(initNode);
		assertEquals("1", initNode.getAddress());
		assertEquals(initObjectType.getName(), initNode.getFullName());
		assertEquals(initObjectType.getSimpleName(), initNode.getSimpleName());
		
		assertEquals(childCount, initNode.getElement().size());
		
		Output testOutput = DataGenerator.generateOutput(input, params, executor);
		
		OutputCompare.compareOutput(output, testOutput, true);
		OutputCompare.compareOutput(testOutput, output, true);
		
		return initNode;
	}
	
	private void countElements(Node node, int compareCount, int infoCount) {
		
		int realInfoCount = 0;
		for (Element element : node.getElement()) {
			if (element.isInformational()) {				
				realInfoCount++;
			}
		}
		
		int size = node.getElement().size();
		System.out.println(infoCount + "  " + realInfoCount);
		assertEquals(infoCount, realInfoCount);
		assertEquals(compareCount, size - realInfoCount);
		assertEquals(infoCount + compareCount, size);
	}
	
	private void checkElements(Node node, Class<?> elementType) {
		for (Element element : node.getElement()) {
			if (element.getClass() != elementType) {
				fail("Expected type was '" + elementType 
						+ "', but was '" + element.getClass() + "'.");
			}
		}
	}
	
	@rtt.annotations.Node static class EmptyClass {
		@Initialize public EmptyClass(InputStream in) {}		
	}

	@Test
	public void testEmptyClass() throws Throwable {
		generateInitNode(EmptyClass.class, 0);		
	}
	
	@SuppressWarnings("unused")
	@rtt.annotations.Node static class FieldClass {
		@Initialize public FieldClass(InputStream in) {}
		
		private String privateStringField = "";
		protected String protectedStringField = "";
		public String publicStringField = "";
		
		@Compare private String privateCompareStringField = "";
		@Compare protected String protectedCompareStringField = "";
		@Compare public String publicCompareStringField = "";
		
		@Informational private String privateInfoStringField = "";
		@Informational protected String protectedInfoStringField = "";
		@Informational public String publicInfoStringField = "";		
	}
	
	@Test
	public void testFieldClass() throws Throwable {
		Node node = generateInitNode(FieldClass.class, 6);
		countElements(node, 3, 3);
		checkElements(node, Value.class);
	}
	
	@SuppressWarnings("unused")
	@rtt.annotations.Node static class MethodClass {
		@Initialize public MethodClass(InputStream in) {}

		private String privateStringMethod() { return ""; }
		protected String protectedStringMethod() { return ""; }
		public String publicStringMethod() { return ""; }
		
		@Compare private String privateCompareStringMethod() { return ""; }
		@Compare protected String protectedCompareStringMethod() { return ""; }
		@Compare public String publicCompareStringMethod() { return ""; }
		
		@Informational private String privateInfoStringMethod() { return ""; }
		@Informational protected String protectedInfoStringMethod() { return ""; }
		@Informational public String publicInfoStringMethod() { return ""; }
	}
	
	@Test
	public void testMethodClass() throws Throwable {
		Node node = generateInitNode(MethodClass.class, 6);
		countElements(node, 3, 3);
		checkElements(node, Value.class);
	}
	
	@rtt.annotations.Node static class TestClass {
		@Initialize public TestClass(InputStream in) {}
		
		@Compare NodeClass aSampleCompareField = new NodeClass();
		@Informational NodeClass aSampleInfoField = new NodeClass();
		
		@Compare NodeClass aSampleCompareMethod() {	return new NodeClass(); }		
		@Informational NodeClass aSampleInfoMethod() { return new NodeClass(); }
	}
	
	@rtt.annotations.Node static class NodeClass {
		@Compare protected String protectedCompareStringField = "";
		@Compare protected String protectedCompareStringMethod() { return ""; }
	}
	
	@Test
	public void testTestClass() throws Throwable {
		Node node = generateInitNode(TestClass.class, 4);
		countElements(node, 2, 2);
		checkElements(node, Node.class);
		
		for (Element element : node.getElement()) {
			checkElements((Node) element, Value.class);
			if (!element.isInformational()) {				
				countElements((Node) element, 2, 0);
			} else {
				countElements((Node) element, 0, 2);
			}			
		}
	}
}
