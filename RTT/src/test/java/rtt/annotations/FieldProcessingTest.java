package rtt.annotations;

import static org.junit.Assert.*;

import java.lang.reflect.Member;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Value;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.annotations.processing.ValueMember;

@SuppressWarnings("unused")
public class FieldProcessingTest {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: permuted fields
	//	Only non-void and non-parameter fields 
	//	should be detected by annotation processing
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	public static class PermutedFieldsClass {		
		private String privateStringField = "";
		protected String protectedStringField = "";
		public String publicStringField = "";
		
		@Value private String privateCompareStringField = "";
		@Value protected String protectedCompareStringField = "";
		@Value public String publicCompareStringField = "";
		
		@Value(informational=true) private String privateInfoStringField = "";
		@Value(informational=true) protected String protectedInfoStringField = "";
		@Value(informational=true) public String publicInfoStringField = "";
	}
	
	@Test
	public void testPermutedFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				PermutedFieldsClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 3, 3);
		TestAnnotationUtils.executeMembers(valueMembers, PermutedFieldsClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: shadowing of fields
	// Due to annotations within the super & extending class
	//  multiple and also shadowed fields should be detected.
	// --> fields count = 3 (from super class)
	//                  + 3 (from extending class) 
	
	public static class SuperFieldClass {		
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Value private String privateCompareField = "";
		@Value protected String protectedCompareField = "";
		@Value public String publicCompareField = "";
		
		@Value(informational=true) private String privateInfoField = "";
		@Value(informational=true) protected String protectedInfoField = "";
		@Value(informational=true) public String publicInfoField = "";
	}
	
	public static class ExtendingFieldClass extends SuperFieldClass {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Value private String privateCompareField = "";
		@Value protected String protectedCompareField = "";
		@Value public String publicCompareField = "";
		
		@Value(informational=true) private String privateInfoField = "";
		@Value(informational=true) protected String protectedInfoField = "";
		@Value(informational=true) public String publicInfoField = "";
	}
	
	@Test
	public void testExtendingFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				ExtendingFieldClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 6, 6);
		TestAnnotationUtils.executeMembers(valueMembers, ExtendingFieldClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Implementing interface fields
	//    Annotated fields should be detected.
	
	static interface FieldInterface {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Value String interfaceCompareField = "";
		@Value public String publicInterfaceCompareField = "";
		
		@Value(informational=true) String interfaceInfoField = "";
		@Value(informational=true) public String publicInterfaceInfoField = "";
	}
	
	public static class ImplementingClass implements FieldInterface {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Value private String privateCompareField = "";
		@Value protected String protectedCompareField = "";
		@Value public String publicCompareField = "";
		
		@Value(informational=true) private String privateInfoField = "";
		@Value(informational=true) protected String protectedInfoField = "";
		@Value(informational=true) public String publicInfoField = "";	
	}
	
	@Test
	public void testImplementingFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				ImplementingClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 5, 5);
		TestAnnotationUtils.executeMembers(valueMembers, ImplementingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Extending implementing classes with fields 
	//    - Interface <- Abstract class <- concrete Class
	
	static interface TopInterface {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Value String interfaceCompareField = "";
		@Value public String publicInterfaceCompareField = "";
		
		@Value(informational=true) String interfaceInfoField = "";
		@Value(informational=true) public String publicInterfaceInfoField = "";
	}
	
	static abstract class ImplementingAbstractClass implements TopInterface {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Value private String privateCompareField = "";
		@Value protected String protectedCompareField = "";
		@Value public String publicCompareField = "";
		
		@Value(informational=true) private String privateInfoField = "";
		@Value(informational=true) protected String protectedInfoField = "";
		@Value(informational=true) public String publicInfoField = "";
	}
	
	public static class ConcreteClass extends ImplementingAbstractClass {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Value private String privateCompareField = "";
		@Value protected String protectedCompareField = "";
		@Value public String publicCompareField = "";
		
		@Value(informational=true) private String privateInfoField = "";
		@Value(informational=true) protected String protectedInfoField = "";
		@Value(informational=true) public String publicInfoField = "";
	}
	
	@Test
	public void testImplementingAbstractFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				ConcreteClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 8, 8);
		TestAnnotationUtils.executeMembers(valueMembers, ConcreteClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: one interfaces extends another
	//    - fields from concrete class should be detected.
	
	interface InterfaceA {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Value String interfaceCompareField = "";
		@Value public String publicInterfaceCompareField = "";
		
		@Value(informational=true) String interfaceInfoField = "";
		@Value(informational=true) public String publicInterfaceInfoField = "";
	}
	
	interface InterfaceB extends InterfaceA {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Value String interfaceCompareField = "";
		@Value public String publicInterfaceCompareField = "";
		
		@Value(informational=true) String interfaceInfoField = "";
		@Value(informational=true) public String publicInterfaceInfoField = "";
	}
	
	public static class ExtendedImplementingClass implements InterfaceB {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Value private String privateCompareField = "";
		@Value protected String protectedCompareField = "";
		@Value public String publicCompareField = "";
		
		@Value(informational=true) private String privateInfoField = "";
		@Value(informational=true) protected String protectedInfoField = "";
		@Value(informational=true) public String publicInfoField = "";
	}
	
	@Test
	public void testExtendedInterfaceFields() throws Exception {
		Set<ValueMember<?>> valueMembers = AnnotationProcessor.getValueMembers(
				ExtendedImplementingClass.class);
		
		TestAnnotationUtils.countMembers(valueMembers, 7, 7);
		TestAnnotationUtils.executeMembers(valueMembers, ExtendedImplementingClass.class);		
	}
	
}
