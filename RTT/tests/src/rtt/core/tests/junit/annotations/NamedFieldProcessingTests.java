package rtt.core.tests.junit.annotations;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Value;
import rtt.annotations.processing2.AnnotationProcessor;
import rtt.annotations.processing2.ValueMember;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.EqualNamedAttributeClass;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.EqualNamedExtendingClass;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.ExtendingClass;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.NamedAttributeClass;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.NamedExtendingClass;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.NoNameAttributeClass;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class NamedFieldProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: annotated fields without name attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	public static class NoNameAttributeClass {		
		@Value(informational=true) private String infoC = "";
		@Value(informational=true) protected String infoB = "";
		@Value(informational=true) public String infoA = "";
		
		@Value private String compareC = "";	
		@Value protected String compareB = "";		
		@Value public String compareA = "";				
	}
	
	@Test
	public void testNonNamedFields() throws Exception {
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
	// Test: annotated fields with name attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	public static class NamedAttributeClass {		
		@Value(name="compareZ") public String compareA = "";
		@Value(name="compareY") protected String compareB = "";
		@Value(name="compareX") private String compareC = "";		

		@Value(informational=true, name="compareO") public String infoA = "";
		@Value(informational=true, name="compareN") protected String infoB = "";
		@Value(informational=true, name="compareM") private String infoC = "";		
	}

	@Test
	public void testAscendingNamedFields() throws Exception {
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
	// Test: annotated fields with index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	public static class EqualNamedAttributeClass {		
		@Value(name="compare") public String compareA = "";
		@Value(name="compare") protected String compareB = "";
		@Value(name="compare") private String compareC = "";		

		@Value(informational=true, name="compare") public String infoA = "";
		@Value(informational=true, name="compare") protected String infoB = "";
		@Value(informational=true, name="compare") private String infoC = "";		
	}

	@Test
	public void testEqualNamedFields() throws Exception {
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
	// Test: annotated fields with but changed index 
	//	 --> compare fields count = 2
	//	 --> informational fields count = 2

	public static class SuperClass {		
		@Value public String compareField = "";
		@Value(informational=true) public String infoField = "";		
	}

	public static class ExtendingClass extends SuperClass {		
		@Value public String compareField = "";
		@Value(informational=true) public String infoField = "";
	}

	@Test
	public void testExtendingFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				ExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"ExtendingClass.compareField",
				"SuperClass.compareField",
				"ExtendingClass.infoField",
				"SuperClass.infoField");
		TestAnnotationUtils.executeMembers(valueMembers, ExtendingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: extended annotated fields with but changed named, 
	//	 --> compare fields count = 2 
	//	 --> informational fields count = 2
	
	public static class NamedSuperClass {		
		@Value(name="compareA") public String compareField = "";
		@Value(informational=true, name="infoA") public String infoField = "";		
	}
	
	public static class NamedExtendingClass extends NamedSuperClass {		
		@Value(name="compareB") public String compareField = "";
		@Value(informational=true, name="infoB") public String infoField = "";
	}
	
	@Test
	public void testExtendingNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				NamedExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"NamedSuperClass.compareField",
				"NamedExtendingClass.compareField",
				"NamedSuperClass.infoField",
				"NamedExtendingClass.infoField");
		TestAnnotationUtils.executeMembers(valueMembers, NamedExtendingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: extended annotated fields with but changed named, 
	//	 --> compare fields count = 2
	//	 --> informational fields count = 2

	public static class EqualNamedSuperClass {		
		@Value(name="field") public String compareField = "";
		@Value(informational=true, name="field") public String infoField = "";		
	}

	public static class EqualNamedExtendingClass extends EqualNamedSuperClass {		
		@Value(name="field") public String compareField = "";
		@Value(informational=true, name="field") public String infoField = "";
	}

	@Test
	public void testExtendingEqualNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				EqualNamedExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"EqualNamedExtendingClass.compareField",
				"EqualNamedSuperClass.compareField",
				"EqualNamedExtendingClass.infoField",
				"EqualNamedSuperClass.infoField");
		TestAnnotationUtils.executeMembers(valueMembers, EqualNamedExtendingClass.class);
	}
}
