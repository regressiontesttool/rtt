package rtt.core.tests.junit.annotations;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Value;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.annotations.processing.ValueMember;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

public class NamedMethodProcessingTest {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: annotated methods without name attribute
	//	 --> compare methods count = 3 
	//	 --> informational methods count = 3
	
	public static class NoNameAttributeClass {		
		@Value(informational=true) private String infoC() { return ""; };
		@Value(informational=true) protected String infoB() { return ""; };
		@Value(informational=true) public String infoA() { return ""; };
		
		@Value private String compareC() { return ""; };	
		@Value protected String compareB() { return ""; };		
		@Value public String compareA() { return ""; };				
	}
	
	@Test
	public void testNonNamedMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				NoNameAttributeClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.checkOrder(valueMembers, 
				"NoNameAttributeClass.compareA", 
				"NoNameAttributeClass.compareB", 
				"NoNameAttributeClass.compareC",
				"NoNameAttributeClass.infoA", 
				"NoNameAttributeClass.infoB", 
				"NoNameAttributeClass.infoC");		
		
		TestAnnotationUtils.executeMembers(valueMembers, NoNameAttributeClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: annotated methods with name attribute
	//	 --> compare methods count = 3 
	//	 --> informational methods count = 3

	public static class NamedAttributeClass {		
		@Value(name="compareZ") public String compareA() { return ""; };
		@Value(name="compareY") protected String compareB() { return ""; };
		@Value(name="compareX") private String compareC() { return ""; };		

		@Value(informational=true, name="compareO") public String infoA() { return ""; };
		@Value(informational=true, name="compareN") protected String infoB() { return ""; };
		@Value(informational=true, name="compareM") private String infoC() { return ""; };
	}

	@Test
	public void testAscendingNamedMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				NamedAttributeClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.checkOrder(valueMembers, 
				"NamedAttributeClass.compareC", 
				"NamedAttributeClass.compareB", 
				"NamedAttributeClass.compareA",				 
				"NamedAttributeClass.infoC", 
				"NamedAttributeClass.infoB",
				"NamedAttributeClass.infoA");
		TestAnnotationUtils.executeMembers(valueMembers, NamedAttributeClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: annotated methods with index attribute
	//	 --> compare methods count = 3 
	//	 --> informational methods count = 3

	public static class EqualNamedAttributeClass {		
		@Value(name="compare") public String compareA() { return ""; };
		@Value(name="compare") protected String compareB() { return ""; };
		@Value(name="compare") private String compareC() { return ""; };		

		@Value(informational=true, name="compare") public String infoA() { return ""; };
		@Value(informational=true, name="compare") protected String infoB() { return ""; };
		@Value(informational=true, name="compare") private String infoC() { return ""; };		
	}

	@Test
	public void testEqualNamedMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				EqualNamedAttributeClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.checkOrder(valueMembers,
				"EqualNamedAttributeClass.compareA", 
				"EqualNamedAttributeClass.compareB", 
				"EqualNamedAttributeClass.compareC",
				"EqualNamedAttributeClass.infoA", 
				"EqualNamedAttributeClass.infoB",
				"EqualNamedAttributeClass.infoC");
		TestAnnotationUtils.executeMembers(valueMembers, EqualNamedAttributeClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: annotated methods with but changed index 
	//	 --> compare methods count = 2
	//	 --> informational methods count = 2

	public static class SuperClass {		
		@Value public String compareMethod() { return ""; };
		@Value(informational=true) public String infoMethod() { return ""; };		
	}

	public static class ExtendingClass extends SuperClass {		
		@Value public String compareMethod() { return ""; };
		@Value(informational=true) public String infoMethod() { return ""; };
	}

	@Test
	public void testExtendingMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				ExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"ExtendingClass.compareMethod",
				"SuperClass.compareMethod",
				"ExtendingClass.infoMethod",
				"SuperClass.infoMethod");
		TestAnnotationUtils.executeMembers(valueMembers, ExtendingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: extended annotated methods with but changed named, 
	//	 --> compare methods count = 2 
	//	 --> informational methods count = 2
	
	public static class NamedSuperClass {		
		@Value(name="compareA") public String compareMethod() { return ""; };
		@Value(informational=true, name="infoA") public String infoMethod() { return ""; };		
	}
	
	public static class NamedExtendingClass extends NamedSuperClass {		
		@Value(name="compareB") public String compareMethod() { return ""; };
		@Value(informational=true, name="infoB") public String infoMethod() { return ""; };
	}
	
	@Test
	public void testExtendingNamedMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				NamedExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"NamedSuperClass.compareMethod",
				"NamedExtendingClass.compareMethod",
				"NamedSuperClass.infoMethod",
				"NamedExtendingClass.infoMethod");
		TestAnnotationUtils.executeMembers(valueMembers, NamedExtendingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: extended annotated methods with but changed named, 
	//	 --> compare methods count = 2
	//	 --> informational methods count = 2

	public static class EqualNamedSuperClass {		
		@Value(name="method") public String compareMethod() { return ""; };
		@Value(informational=true, name="method") public String infoMethod() { return ""; };		
	}

	public static class EqualNamedExtendingClass extends EqualNamedSuperClass {		
		@Value(name="method") public String compareMethod() { return ""; };
		@Value(informational=true, name="method") public String infoMethod() { return ""; };
	}

	@Test
	public void testExtendingEqualNamedMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				EqualNamedExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"EqualNamedExtendingClass.compareMethod",
				"EqualNamedSuperClass.compareMethod",
				"EqualNamedExtendingClass.infoMethod",
				"EqualNamedSuperClass.infoMethod");
		TestAnnotationUtils.executeMembers(valueMembers, EqualNamedExtendingClass.class);
	}
}
