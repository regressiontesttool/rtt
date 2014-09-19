package rtt.core.tests.junit.core;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Initialize;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.archive.output.Output;
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
	
	private Element generateInitNode(Class<?> initObjectType, int childCount) throws Throwable {		
		Executor executor = new Executor(initObjectType);
		Output output = DataGenerator.generateOutput(input, params, executor);
		
		Element initNode = (Element) output.getInitialElement();
		assertNotNull(initNode);
		assertEquals("1", initNode.getAddress());
		assertEquals(initObjectType.getName(), initNode.getValue());
		
		assertEquals(childCount, initNode.getElements().size());
		
		Output testOutput = DataGenerator.generateOutput(input, params, executor);
		
		OutputCompare.compareOutput(output, testOutput, true);
		OutputCompare.compareOutput(testOutput, output, true);
		
		return initNode;
	}
	
	private void countElements(Element element, int compareCount, int infoCount) {
		
		int realInfoCount = 0;
		for (Element childElement : element.getElements()) {
			if (childElement.isInformational()) {				
				realInfoCount++;
			}
		}
		
		int size = element.getElements().size();
		
		assertEquals(infoCount, realInfoCount);
		assertEquals(compareCount, size - realInfoCount);
		assertEquals(infoCount + compareCount, size);
	}
	
	private void checkElements(Element element, ElementType elementType) {
		for (Element childElement : element.getElements()) {
			assertEquals(elementType, childElement.getElementType());
		}
	}
	
	private void checkAddresses(Element element) {
		int i = 1;
		for (Element childElement : element.getElements()) {
			assertEquals(element.getAddress() + "." + i++, childElement.getAddress());
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
		
		@rtt.annotations.Node.Value private String privateCompareStringField = "";
		@rtt.annotations.Node.Value protected String protectedCompareStringField = "";
		@rtt.annotations.Node.Value public String publicCompareStringField = "";
		
		@rtt.annotations.Node.Value(informational=true) private String privateInfoStringField = "";
		@rtt.annotations.Node.Value(informational=true) protected String protectedInfoStringField = "";
		@rtt.annotations.Node.Value(informational=true) public String publicInfoStringField = "";		
	}
	
	@Test
	public void testFieldClass() throws Throwable {
		Element node = generateInitNode(FieldClass.class, 6);
		countElements(node, 3, 3);
		checkElements(node, ElementType.VALUE);
	}
	
	@SuppressWarnings("unused")
	@rtt.annotations.Node static class MethodClass {
		@Initialize public MethodClass(InputStream in) {}

		private String privateStringMethod() { return ""; }
		protected String protectedStringMethod() { return ""; }
		public String publicStringMethod() { return ""; }
		
		@rtt.annotations.Node.Value private String privateCompareStringMethod() { return ""; }
		@rtt.annotations.Node.Value protected String protectedCompareStringMethod() { return ""; }
		@rtt.annotations.Node.Value public String publicCompareStringMethod() { return ""; }
		
		@rtt.annotations.Node.Value(informational=true) private String privateInfoStringMethod() { return ""; }
		@rtt.annotations.Node.Value(informational=true) protected String protectedInfoStringMethod() { return ""; }
		@rtt.annotations.Node.Value(informational=true) public String publicInfoStringMethod() { return ""; }
	}
	
	@Test
	public void testMethodClass() throws Throwable {
		Element node = generateInitNode(MethodClass.class, 6);
		countElements(node, 3, 3);
		checkElements(node, ElementType.VALUE);
		checkAddresses(node);
	}
	
	@rtt.annotations.Node static class TestClass {
		@Initialize public TestClass(InputStream in) {}
		
		@rtt.annotations.Node.Value NodeClass aSampleCompareField = new NodeClass();
		@rtt.annotations.Node.Value(informational=true) NodeClass aSampleInfoField = new NodeClass();
		
		@rtt.annotations.Node.Value NodeClass aSampleCompareMethod() {	return new NodeClass(); }		
		@rtt.annotations.Node.Value(informational=true) NodeClass aSampleInfoMethod() { return new NodeClass(); }
	}
	
	@rtt.annotations.Node static class NodeClass {
		@rtt.annotations.Node.Value protected String protectedCompareStringField = "";
		@rtt.annotations.Node.Value protected String protectedCompareStringMethod() { return ""; }
	}
	
	@Test
	public void testTestClass() throws Throwable {
		Element node = generateInitNode(TestClass.class, 4);
		countElements(node, 2, 2);
		checkElements(node, ElementType.NODE);
		checkAddresses(node);
		
		for (Element element : node.getElements()) {
			Element childNode = (Element) element;
			
			checkElements(childNode, ElementType.VALUE);
			checkAddresses(childNode);
			if (!element.isInformational()) {				
				countElements(childNode, 2, 0);
			} else {
				countElements(childNode, 0, 2);
			}			
		}		
	}
	
	@rtt.annotations.Node static class ReferencingClass {
		@Initialize public ReferencingClass(InputStream in) {}
		private ReferencedClass referencedClass = new ReferencedClass();
		
		@rtt.annotations.Node.Value 
		protected ReferencedClass referencingMethod1() {
			return referencedClass;
		}
		
		@rtt.annotations.Node.Value 
		protected ReferencedClass referencingMethod2() {
			return referencedClass;
		}
	}
	
	@rtt.annotations.Node static class ReferencedClass {
		@rtt.annotations.Node.Value private String aField = "aField";
	}
	
	@Test
	public void testReferencing() throws Throwable {
		Element node = generateInitNode(ReferencingClass.class, 2);
		countElements(node, 2, 0);
		checkAddresses(node);
		
		assertEquals(ElementType.NODE, node.getElements().get(0).getElementType());
		assertEquals("ReferencingClass.referencingMethod1",
				node.getElements().get(0).getName());
		assertEquals(ElementType.REFERENCE, node.getElements().get(1).getElementType());
		assertEquals("ReferencingClass.referencingMethod2",
				node.getElements().get(1).getName());
		
		Element childNode = (Element) node.getElements().get(0);
		Element reference = (Element) node.getElements().get(1);
		
		assertEquals(childNode.getAddress(), reference.getValue());
	}
	
	@rtt.annotations.Node static class IndexedReferencingClass {
		@Initialize public IndexedReferencingClass(InputStream in) {}
		private ReferencedClass referencedClass = new ReferencedClass();
		
		@rtt.annotations.Node.Value(index=1) 
		protected ReferencedClass referencingMethod2() {
			return referencedClass;
		}
		
		@rtt.annotations.Node.Value(index=2)
		protected ReferencedClass referencingMethod1() {
			return referencedClass;
		}
	}
	
	@Test
	public void testIndexedReferencing() throws Throwable {
		Element node = generateInitNode(IndexedReferencingClass.class, 2);
		countElements(node, 2, 0);
		checkAddresses(node);
		
		assertEquals(ElementType.NODE, node.getElements().get(0).getElementType());
		assertEquals("IndexedReferencingClass.referencingMethod2",
				node.getElements().get(0).getName());
		assertEquals(ElementType.REFERENCE, node.getElements().get(1).getElementType());
		assertEquals("IndexedReferencingClass.referencingMethod1",
				node.getElements().get(1).getName());
		
		Element childNode = (Element) node.getElements().get(0);
		Element reference = (Element) node.getElements().get(1);
		
		assertEquals(childNode.getAddress(), reference.getValue());
	}
	
	@rtt.annotations.Node static class NamedValuesClass {
		@Initialize public NamedValuesClass(InputStream in) {}
		
		@rtt.annotations.Node.Value(name="string")
		private String aString = "aString";
		
		@rtt.annotations.Node.Value(name="string")
		private String anOtherString = "anOtherString";
	}
	
	@Test
	public void testNamedValues() throws Throwable {
		Element node = generateInitNode(NamedValuesClass.class, 2);
		countElements(node, 2, 0);
		checkAddresses(node);
		
		assertEquals("string", node.getElements().get(0).getName());
		assertEquals("aString", node.getElements().get(0).getValue());
		assertEquals("string", node.getElements().get(1).getName());
		assertEquals("anOtherString", node.getElements().get(1).getValue());
	}
}
