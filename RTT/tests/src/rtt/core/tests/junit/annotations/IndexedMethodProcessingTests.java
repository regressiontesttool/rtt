package rtt.core.tests.junit.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Value;
import rtt.annotations.processing2.AnnotationProcessor2;
import rtt.annotations.processing2.ValueMember;
import rtt.core.tests.junit.annotations.IndexedFieldProcessingTests.AscendingIndexAttributeClass;
import rtt.core.tests.junit.annotations.IndexedFieldProcessingTests.EqualIndexAttributeClass;
import rtt.core.tests.junit.annotations.IndexedFieldProcessingTests.ExtendingClass;
import rtt.core.tests.junit.annotations.IndexedFieldProcessingTests.IndexedExtendingClass;
import rtt.core.tests.junit.annotations.IndexedFieldProcessingTests.NoIndexAttributeClass;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class IndexedMethodProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: annotated fields without index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	public static class NoIndexAttributeClass {		
		@Value(informational=true) private String infoC() { return ""; };
		@Value(informational=true) protected String infoB() { return ""; };
		@Value(informational=true) public String infoA() { return ""; };
		
		@Value private String compareC() { return ""; };	
		@Value protected String compareB() { return ""; };		
		@Value public String compareA() { return ""; };				
	}
	
	@Test
	public void testNonIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
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
		@Value(index=6) public String compareA() { return ""; };
		@Value(index=5) protected String compareB() { return ""; };
		@Value(index=4) private String compareC() { return ""; };		

		@Value(informational=true, index=3) public String infoA() { return ""; };
		@Value(informational=true, index=2) protected String infoB() { return ""; };
		@Value(informational=true, index=1) private String infoC() { return ""; };		
	}

	@Test
	public void testAscendingIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
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
		@Value(index=3) private String compareC() { return ""; };
		@Value(index=3) protected String compareB() { return ""; };
		@Value(index=3) public String compareA() { return ""; };				

		@Value(informational=true, index=2) private String infoC() { return ""; };		
		@Value(informational=true, index=2) protected String infoB() { return ""; };
		@Value(informational=true, index=2) public String infoA() { return ""; };		
	}

	@Test
	public void testEqualIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
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
		@Value public String compareMethod() { return ""; };
		@Value(informational=true) public String infoMethod() { return ""; };		
	}

	public static class ExtendingClass extends SuperClass {		
		@Value public String compareMethod() { return ""; };
		@Value(informational=true) public String infoMethod() { return ""; };
	}

	@Test
	public void testExtendingMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
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
	// Test: extended annotated fields with but changed index 
	//	 --> compare fields count = 2 
	//	 --> informational fields count = 2
	
	public static class IndexedSuperClass {		
		@Value(index=22) public String compareMethod() { return ""; };
		@Value(informational=true, index=21) public String infoMethod() { return ""; };		
	}
	
	public static class IndexedExtendingClass extends IndexedSuperClass {		
		@Value(index=12) public String compareMethod() { return ""; };
		@Value(informational=true, index=11) public String infoMethod() { return ""; };
	}
	
	@Test
	public void testExtendingIndexedMethods() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				IndexedExtendingClass.class);

		TestAnnotationUtils.countMembers(valueMembers, 2, 2);
		TestAnnotationUtils.checkOrder(valueMembers,
				"IndexedExtendingClass.infoMethod",
				"IndexedExtendingClass.compareMethod",
				"IndexedSuperClass.infoMethod",
				"IndexedSuperClass.compareMethod");
		TestAnnotationUtils.executeMembers(valueMembers, IndexedExtendingClass.class);
	}
}
