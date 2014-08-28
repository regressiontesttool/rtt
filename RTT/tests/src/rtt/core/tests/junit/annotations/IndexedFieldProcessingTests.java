package rtt.core.tests.junit.annotations;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Value;
import rtt.annotations.processing2.AnnotationProcessor2;
import rtt.annotations.processing2.ValueMember;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class IndexedFieldProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	private void invokeMembers(Set<ValueMember<?>> members, Class<?> classType) {
		try {
			Object object = classType.newInstance();
			for (ValueMember<?> member : members) {
				member.getResult(object);
				System.out.println(member.getSignature());
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	private void checkOrder(Set<ValueMember<?>> valueMembers, String... signatures) {
		assertEquals(signatures.length, valueMembers.size());
		
		int index = 0;
		Iterator<ValueMember<?>> iterator = valueMembers.iterator();
		while (iterator.hasNext()) {
			assertEquals(signatures[index], iterator.next().getSignature());
			index++;
		}		
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields without index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	static class NoIndexAttributeClass {		
		@Value(informational=true) private String infoC = "";
		@Value(informational=true) protected String infoB = "";
		@Value(informational=true) public String infoA = "";
		
		@Value private String compareC = "";	
		@Value protected String compareB = "";		
		@Value public String compareA = "";				
	}
	
	@Test
	public void testNonIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				NoIndexAttributeClass.class);
		
		TestAnnotationUtils.checkMembers(valueMembers, 3, 3);
		checkOrder(valueMembers, 
				"NoIndexAttributeClass.compareA", 
				"NoIndexAttributeClass.compareB", 
				"NoIndexAttributeClass.compareC",
				"NoIndexAttributeClass.infoA", 
				"NoIndexAttributeClass.infoB", 
				"NoIndexAttributeClass.infoC");
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	static class AscendingIndexAttributeClass {		
		@Value(index=3) public String compareA = "";
		@Value(index=2) protected String compareB = "";
		@Value(index=1) private String compareC = "";		

		@Value(informational=true, index=6) public String infoA = "";
		@Value(informational=true, index=5) protected String infoB = "";
		@Value(informational=true, index=4) private String infoC = "";		
	}

	@Test
	public void testAscendingIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				AscendingIndexAttributeClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 3, 3);
		checkOrder(valueMembers, 
				"AscendingIndexAttributeClass.compareC", 
				"AscendingIndexAttributeClass.compareB", 
				"AscendingIndexAttributeClass.compareA",				 
				"AscendingIndexAttributeClass.infoC", 
				"AscendingIndexAttributeClass.infoB",
				"AscendingIndexAttributeClass.infoA");
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	static class EqualIndexAttributeClass {		
		@Value(index=3) private String compareC = "";
		@Value(index=3) protected String compareB = "";
		@Value(index=3) public String compareA = "";				

		@Value(informational=true, index=2) private String infoC = "";		
		@Value(informational=true, index=2) protected String infoB = "";
		@Value(informational=true, index=2) public String infoA = "";		
	}

	@Test
	public void testEqualIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				EqualIndexAttributeClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 3, 3);
		checkOrder(valueMembers,
				"EqualIndexAttributeClass.infoA", 
				"EqualIndexAttributeClass.infoB",
				"EqualIndexAttributeClass.infoC",
				"EqualIndexAttributeClass.compareA", 
				"EqualIndexAttributeClass.compareB", 
				"EqualIndexAttributeClass.compareC");
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with but changed index 
	//	 --> compare fields count = 2 
	//	 --> informational fields count = 2

	static class SuperClass {		
		@Value public String compareField = "";
		@Value(informational=true) public String infoField = "";		
	}

	static class ExtendingClass extends SuperClass {		
		@Value public String compareField = "";
		@Value(informational=true) public String infoField = "";
	}

	@Test
	public void testExtendingFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				ExtendingClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 2, 2);
		checkOrder(valueMembers,
				"ExtendingClass.compareField",
				"SuperClass.compareField",
				"ExtendingClass.infoField",
				"SuperClass.infoField");
	}
	
	// --------------------------------------------------------------
	// Test: extended annotated fields with but changed index 
	//	 --> compare fields count = 2 
	//	 --> informational fields count = 2
	
	static class IndexedSuperClass {		
		@Value(index=22) public String compareField = "";
		@Value(informational=true, index=21) public String infoField = "";		
	}
	
	static class IndexedExtendingClass extends IndexedSuperClass {		
		@Value(index=12) public String compareField = "";
		@Value(informational=true, index=11) public String infoField = "";
	}
	
	@Test
	public void testExtendingIndexedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				IndexedExtendingClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 2, 2);
		checkOrder(valueMembers,
				"IndexedExtendingClass.infoField",
				"IndexedExtendingClass.compareField",
				"IndexedSuperClass.infoField",
				"IndexedSuperClass.compareField");
	}
}
