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
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.NamedAttributeClass;
import rtt.core.tests.junit.annotations.NamedMethodProcessingTests.NoNameAttributeClass;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class IndexedFieldProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: annotated fields without index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	public static class NoIndexAttributeClass {		
		@Value(informational=true) private String infoC = "";
		@Value(informational=true) protected String infoB = "";
		@Value(informational=true) public String infoA = "";
		
		@Value private String compareC = "";	
		@Value protected String compareB = "";		
		@Value public String compareA = "";				
	}
	
	@Test
	public void testNonIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				NoIndexAttributeClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.checkOrder(valueMembers, 
				"NoIndexAttributeClass.compareA", 
				"NoIndexAttributeClass.compareB", 
				"NoIndexAttributeClass.compareC",
				"NoIndexAttributeClass.infoA", 
				"NoIndexAttributeClass.infoB", 
				"NoIndexAttributeClass.infoC");
		TestAnnotationUtils.executeMembers(valueMembers, NoIndexAttributeClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	public static class AscendingIndexAttributeClass {		
		@Value(index=6) public String compareA = "";
		@Value(index=5) protected String compareB = "";
		@Value(index=4) private String compareC = "";		

		@Value(informational=true, index=3) public String infoA = "";
		@Value(informational=true, index=2) protected String infoB = "";
		@Value(informational=true, index=1) private String infoC = "";		
	}

	@Test
	public void testAscendingIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				AscendingIndexAttributeClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.checkOrder(valueMembers, 
				"AscendingIndexAttributeClass.infoC", 
				"AscendingIndexAttributeClass.infoB",
				"AscendingIndexAttributeClass.infoA",
				"AscendingIndexAttributeClass.compareC", 
				"AscendingIndexAttributeClass.compareB", 
				"AscendingIndexAttributeClass.compareA");
		TestAnnotationUtils.executeMembers(valueMembers, AscendingIndexAttributeClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	public static class EqualIndexAttributeClass {		
		@Value(index=3) private String compareC = "";
		@Value(index=3) protected String compareB = "";
		@Value(index=3) public String compareA = "";				

		@Value(informational=true, index=2) private String infoC = "";		
		@Value(informational=true, index=2) protected String infoB = "";
		@Value(informational=true, index=2) public String infoA = "";		
	}

	@Test
	public void testEqualIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				EqualIndexAttributeClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.checkOrder(valueMembers,
				"EqualIndexAttributeClass.infoA", 
				"EqualIndexAttributeClass.infoB",
				"EqualIndexAttributeClass.infoC",
				"EqualIndexAttributeClass.compareA", 
				"EqualIndexAttributeClass.compareB", 
				"EqualIndexAttributeClass.compareC");
		TestAnnotationUtils.executeMembers(valueMembers, EqualIndexAttributeClass.class);
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
	// Test: extended annotated fields with but changed index 
	//	 --> compare fields count = 2 
	//	 --> informational fields count = 2
	
	public static class IndexedSuperClass {		
		@Value(index=22) public String compareField = "";
		@Value(informational=true, index=21) public String infoField = "";		
	}
	
	public static class IndexedExtendingClass extends IndexedSuperClass {		
		@Value(index=12) public String compareField = "";
		@Value(informational=true, index=11) public String infoField = "";
	}
	
	@Test
	public void testExtendingIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				IndexedExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"IndexedExtendingClass.infoField",
				"IndexedExtendingClass.compareField",
				"IndexedSuperClass.infoField",
				"IndexedSuperClass.compareField");
		
		TestAnnotationUtils.executeMembers(valueMembers, IndexedExtendingClass.class);
	}
}
