package rtt.core.tests.junit.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node;
import rtt.annotations.Node.Initialize;
import rtt.annotations.Node.Value;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.archive.output.Output;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;

public class DataGeneratorTest {

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
	
	private static void assertElementType(Element element, ElementType type) {
		assertEquals(type, element.getElementType());
	}
	
	private static void assertChildElementTypes(Element element, ElementType elementType) {
		for (Element childElement : element.getElements()) {
			assertEquals(elementType, childElement.getElementType());
		}
	}
	
	private void countChildElements(Element element, int compareCount, int infoCount) {
		
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
	
	
	
	private void checkAddresses(Element element) {
		int i = 1;
		for (Element childElement : element.getElements()) {
			assertEquals(element.getAddress() + "." + i++, childElement.getAddress());
		}
	}
	
	private void hasChildValues(Element element, Object... values) {
		assertNotNull(element);		
		assertEquals(values.length, element.getElements().size());
		
		Element child = null;
		
		for (int i = 0; i < values.length; i++) {
			child = element.getElements().get(i);
			assertEquals(ElementType.VALUE, child.getElementType());
			assertEquals(values[i].toString(), child.getValue());
		}
	}
	
	private static Element getFirstChild(Element parent) {
		return parent.getElements().get(0);
	}
	
	private static Element getSecondChild(Element parent) {
		return parent.getElements().get(1);
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
		countChildElements(node, 3, 3);
		assertChildElementTypes(node, ElementType.VALUE);
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
		countChildElements(node, 3, 3);
		assertChildElementTypes(node, ElementType.VALUE);
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
		countChildElements(node, 2, 2);
		assertChildElementTypes(node, ElementType.NODE);
		checkAddresses(node);
		
		for (Element element : node.getElements()) {
			Element childNode = (Element) element;
			
			assertChildElementTypes(childNode, ElementType.VALUE);
			checkAddresses(childNode);
			if (!element.isInformational()) {				
				countChildElements(childNode, 2, 0);
			} else {
				countChildElements(childNode, 0, 2);
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
		countChildElements(node, 2, 0);
		checkAddresses(node);
		
		assertEquals(ElementType.NODE, getFirstChild(node).getElementType());
		assertEquals("ReferencingClass.referencingMethod1",
				getFirstChild(node).getName());
		assertEquals(ElementType.REFERENCE, node.getElements().get(1).getElementType());
		assertEquals("ReferencingClass.referencingMethod2",
				node.getElements().get(1).getName());
		
		Element childNode = (Element) getFirstChild(node);
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
		countChildElements(node, 2, 0);
		checkAddresses(node);
		
		assertEquals(ElementType.NODE, getFirstChild(node).getElementType());
		assertEquals("IndexedReferencingClass.referencingMethod2",
				getFirstChild(node).getName());
		assertEquals(ElementType.REFERENCE, node.getElements().get(1).getElementType());
		assertEquals("IndexedReferencingClass.referencingMethod1",
				node.getElements().get(1).getName());
		
		Element childNode = (Element) getFirstChild(node);
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
		countChildElements(node, 2, 0);
		checkAddresses(node);
		
		assertEquals("string", getFirstChild(node).getName());
		assertEquals("aString", getFirstChild(node).getValue());
		assertEquals("string", node.getElements().get(1).getName());
		assertEquals("anOtherString", node.getElements().get(1).getValue());
	}
	
	@Node static class SimpleArrayClass {
		@Value private int[] primitiveArray = 
			{ 1,2,3,4 };
		
		@Value private String[] stringArray = 
			{ "Hello", "this", "is", "a", "Test" };
		
		@Value private Object[] objectArray = 
			{ new Integer(10), new Boolean(false), new Float(2.0) };
		
		@Initialize public SimpleArrayClass(InputStream in) {}		
	}
	
	@Test
	public void testSimpleArrayValues() throws Throwable {
		Element node = generateInitNode(SimpleArrayClass.class, 3);
		
		// first value is objectArray because of lexical order
		Element objectArray = getFirstChild(node);
		assertElementType(objectArray, ElementType.NODE);
		countChildElements(objectArray, 3, 0);
		hasChildValues(objectArray, new Integer(10), new Boolean(false), new Float(2.0));
		
		// second value is primitiveArray
		Element primitiveArray = node.getElements().get(1);
		assertElementType(primitiveArray, ElementType.NODE);
		countChildElements(primitiveArray, 4, 0);
		hasChildValues(primitiveArray, 1, 2, 3, 4);
		
		// third value is stringArray
		Element stringArray = node.getElements().get(2);
		assertElementType(stringArray, ElementType.NODE);
		countChildElements(stringArray, 5, 0);
		hasChildValues(stringArray, "Hello", "this", "is", "a", "Test");
	}
	
	@Node static class SimpleListClass {
		@Value private List<Object> objectList = new ArrayList<>();
		@Value private List<String> stringList = new ArrayList<>();
		
		@Initialize public SimpleListClass(InputStream in) {
			objectList.add(new Integer(2));
			objectList.add(new Boolean(false));
			objectList.add(new Float(2.0));
			
			stringList.add("Hello");
			stringList.add("World");
		}
	}
	
	@Test
	public void testSimpleListValues() throws Throwable {
		Element node = generateInitNode(SimpleListClass.class, 2);
		
		// first value is objectList because of lexical order
		Element objectArray = getFirstChild(node);
		assertElementType(objectArray, ElementType.NODE);
		countChildElements(objectArray, 3, 0);
		hasChildValues(objectArray, new Integer(2), new Boolean(false), new Float(2.0));
		
		// second value is stringArray
		Element stringArray = node.getElements().get(1);
		assertElementType(stringArray, ElementType.NODE);
		countChildElements(stringArray, 2, 0);
		hasChildValues(stringArray, "Hello", "World");
	}
	
	@Node static class SimpleMapClass {
		@Value private Map<Object, Object> objectMap = new HashMap<>();
		@Value private Map<String, String> stringMap = new HashMap<>();
		
		@Initialize public SimpleMapClass(InputStream in) {
			objectMap.put(new Integer(10), new Boolean(false));
			objectMap.put(new Float(2.0), new String("HelloWorld"));
			
			stringMap.put("firstName", "Max");
			stringMap.put("lastName", "Mustermann");
			stringMap.put("age", "23");
		}
	}
	
	@Test
	public void testSimpleMapValues() throws Throwable {
		Element root = generateInitNode(SimpleMapClass.class, 2);
		
		Element objectMap = getFirstChild(root);
		assertElementType(objectMap, ElementType.NODE);
		countChildElements(objectMap, 2, 0);
		countChildElements(getFirstChild(objectMap), 2, 0); // 1 key + 1 value
		countChildElements(objectMap.getElements().get(1), 2, 0); // 1 key + 1 value
		
		Element stringMap = root.getElements().get(1);
		assertElementType(stringMap, ElementType.NODE);
		countChildElements(stringMap, 3, 0);
		countChildElements(getFirstChild(stringMap), 2, 0); // 1 key + 1 value
		countChildElements(stringMap.getElements().get(1), 2, 0); // 1 key + 1 value
	}	
	
	@Node static class KeyListMap {
		@Value private Map<List<String>, String> map = new HashMap<>();
		
		@Initialize public KeyListMap(InputStream in) {
			List<String> keys = new ArrayList<>();
			keys.add("Key11");
			keys.add("Key12");

			map.put(keys, "Value1");
			
			keys = new ArrayList<>();
			keys.add("Key21");
			keys.add("Key22");
			
			map.put(keys, "Value2");
		}
	}
	
	@Test
	public void testKeyListMap() throws Throwable {
		Element root = generateInitNode(KeyListMap.class, 1);
		Element map = getFirstChild(root);
		
		countChildElements(map, 2, 0);
		Element keyElement = null;
		Element valueElement = null;
		
		for (Element mapEntry : map.getElements()) {
			keyElement = getFirstChild(mapEntry);
			assertElementType(keyElement, ElementType.NODE);
			countChildElements(keyElement, 2, 0);
			
			valueElement = getSecondChild(mapEntry);
			assertElementType(valueElement, ElementType.VALUE);
		}
	}
	
	@Node static class ValueListMap {
		@Value private Map<String, List<String>> map = new HashMap<>();
		
		@Initialize public ValueListMap(InputStream in) {
			List<String> values = new ArrayList<>();
			values.add("Value11");
			values.add("Value12");

			map.put("Key1", values);
			
			values = new ArrayList<>();
			values.add("Value21");
			values.add("Value22");
			
			map.put("Key2", values);		
		}	
	}
	
	@Test
	public void testValueListMap() throws Throwable {
		Element root = generateInitNode(ValueListMap.class, 1);
		Element map = getFirstChild(root);
		
		countChildElements(map, 2, 0);
		Element keyElement = null;
		Element valueElement = null;
		
		for (Element mapEntry : map.getElements()) {
			keyElement = getFirstChild(mapEntry);
			assertElementType(keyElement, ElementType.VALUE);
			
			valueElement = getSecondChild(mapEntry);
			assertElementType(valueElement, ElementType.NODE);
			countChildElements(valueElement, 2, 0);
		}
	}
	
	@Node static class KeyValueListMap {
		@Value private Map<List<String>, List<String>> map = new HashMap<>();
		
		@Initialize public KeyValueListMap(InputStream in) {
			List<String> keys = new ArrayList<>();
			keys.add("Key11");
			keys.add("Key12");
			
			List<String> values = new ArrayList<>();
			values.add("Value11");
			values.add("Value12");
			
			map.put(keys, values);
			
			keys = new ArrayList<>();
			keys.add("Key21");
			keys.add("Key22");
			
			values = new ArrayList<>();
			values.add("Value21");
			values.add("Value22");
			
			map.put(keys, values);
		}		
	}
	
	@Test
	public void testKeyValueListMap() throws Throwable {
		Element root = generateInitNode(KeyValueListMap.class, 1);
Element map = getFirstChild(root);
		
		countChildElements(map, 2, 0);
		Element keyElement = null;
		Element valueElement = null;
		
		for (Element mapEntry : map.getElements()) {
			keyElement = getFirstChild(mapEntry);
			assertElementType(keyElement, ElementType.NODE);
			countChildElements(keyElement, 2, 0);
			
			valueElement = getSecondChild(mapEntry);
			assertElementType(valueElement, ElementType.NODE);
			countChildElements(valueElement, 2, 0);
		}
	}
}
