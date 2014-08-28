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
public class NamedFieldProcessingTests {

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
	// Test: annotated fields without name attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	static class NoNameAttributeClass {		
		@Value(informational=true) private String infoC = "";
		@Value(informational=true) protected String infoB = "";
		@Value(informational=true) public String infoA = "";
		
		@Value private String compareC = "";	
		@Value protected String compareB = "";		
		@Value public String compareA = "";				
	}
	
	@Test
	public void testNonNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				NoNameAttributeClass.class);
		
		TestAnnotationUtils.checkMembers(valueMembers, 3, 3);
		checkOrder(valueMembers, 
				"NoNameAttributeClass.compareA", 
				"NoNameAttributeClass.compareB", 
				"NoNameAttributeClass.compareC",
				"NoNameAttributeClass.infoA", 
				"NoNameAttributeClass.infoB", 
				"NoNameAttributeClass.infoC");
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with name attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	static class NamedAttributeClass {		
		@Value(name="compareZ") public String compareA = "";
		@Value(name="compareY") protected String compareB = "";
		@Value(name="compareX") private String compareC = "";		

		@Value(informational=true, name="compareO") public String infoA = "";
		@Value(informational=true, name="compareN") protected String infoB = "";
		@Value(informational=true, name="compareM") private String infoC = "";		
	}

	@Test
	public void testAscendingNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				NamedAttributeClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 3, 3);
		checkOrder(valueMembers, 
				"NamedAttributeClass.compareC", 
				"NamedAttributeClass.compareB", 
				"NamedAttributeClass.compareA",				 
				"NamedAttributeClass.infoC", 
				"NamedAttributeClass.infoB",
				"NamedAttributeClass.infoA");
	}
	
	// --------------------------------------------------------------
	// Test: annotated fields with index attribute
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	static class EqualNamedAttributeClass {		
		@Value(name="compare") public String compareA = "";
		@Value(name="compare") protected String compareB = "";
		@Value(name="compare") private String compareC = "";		

		@Value(informational=true, name="compare") public String infoA = "";
		@Value(informational=true, name="compare") protected String infoB = "";
		@Value(informational=true, name="compare") private String infoC = "";		
	}

	@Test
	public void testEqualNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				EqualNamedAttributeClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 3, 3);
		checkOrder(valueMembers,
				"EqualNamedAttributeClass.compareA", 
				"EqualNamedAttributeClass.compareB", 
				"EqualNamedAttributeClass.compareC",
				"EqualNamedAttributeClass.infoA", 
				"EqualNamedAttributeClass.infoB",
				"EqualNamedAttributeClass.infoC");
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
	// Test: extended annotated fields with but changed named, 
	//	 --> compare fields count = 2 
	//	 --> informational fields count = 2
	
	static class NamedSuperClass {		
		@Value(name="compareA") public String compareField = "";
		@Value(informational=true, name="infoA") public String infoField = "";		
	}
	
	static class NamedExtendingClass extends NamedSuperClass {		
		@Value(name="compareB") public String compareField = "";
		@Value(informational=true, name="infoB") public String infoField = "";
	}
	
	@Test
	public void testExtendingNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				NamedExtendingClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 2, 2);
		checkOrder(valueMembers,
				"NamedSuperClass.compareField",
				"NamedExtendingClass.compareField",
				"NamedSuperClass.infoField",
				"NamedExtendingClass.infoField");
	}
	
	// --------------------------------------------------------------
	// Test: extended annotated fields with but changed named, 
	//	 --> compare fields count = 2
	//	 --> informational fields count = 2

	static class EqualNamedSuperClass {		
		@Value(name="field") public String compareField = "";
		@Value(informational=true, name="field") public String infoField = "";		
	}

	static class EqualNamedExtendingClass extends EqualNamedSuperClass {		
		@Value(name="field") public String compareField = "";
		@Value(informational=true, name="field") public String infoField = "";
	}

	@Test
	public void testExtendingEqualNamedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor2.getValueMembers(
				EqualNamedExtendingClass.class);

		TestAnnotationUtils.checkMembers(valueMembers, 2, 2);
		checkOrder(valueMembers,
				"EqualNamedExtendingClass.compareField",
				"EqualNamedSuperClass.compareField",
				"EqualNamedExtendingClass.infoField",
				"EqualNamedSuperClass.infoField");
	}
}
