package rtt.core.tests.junit.annotations;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class FieldProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	private void invokeFields(List<Field> fields, Class<?> classType) {
		try {
			Object object = classType.newInstance();
			for (Field field : fields) {
				field.setAccessible(true);
				field.get(object);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	// --------------------------------------------------------------
	// Test: permuted fields
	//	Only non-void and non-parameter fields 
	//	should be detected by annotation processing
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	static class PermutedFieldsClass {		
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
	public void testPermutedFields() throws Exception {
		List<Field> compareFields = AnnotationProcessor.getFields(
				PermutedFieldsClass.class, Compare.class);
		
		TestAnnotationUtils.checkElements(compareFields, Compare.class, 3);
		
		List<Field> infoFields = AnnotationProcessor.getFields(
				PermutedFieldsClass.class, Informational.class);
		
		TestAnnotationUtils.checkElements(infoFields, Informational.class, 3);
		
		invokeFields(compareFields, PermutedFieldsClass.class);
		invokeFields(infoFields, PermutedFieldsClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: shadowing of fields
	// Due to annotations within the super & extending class
	//  multiple and also shadowed fields should be detected.
	// --> fields count = 3 (from super class)
	//                  + 3 (from extending class) 
	
	static class SuperFieldClass {		
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Compare private String privateCompareField = "";
		@Compare protected String protectedCompareField = "";
		@Compare public String publicCompareField = "";
		
		@Informational private String privateInfoField = "";
		@Informational protected String protectedInfoField = "";
		@Informational public String publicInfoField = "";
	}
	
	static class ExtendingFieldClass extends SuperFieldClass {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Compare private String privateCompareField = "";
		@Compare protected String protectedCompareField = "";
		@Compare public String publicCompareField = "";
		
		@Informational private String privateInfoField = "";
		@Informational protected String protectedInfoField = "";
		@Informational public String publicInfoField = "";
	}
	
	@Test
	public void testExtendingFields() throws Exception {
		List<Field> compareFields = AnnotationProcessor.getFields(ExtendingFieldClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareFields, Compare.class, 6);
		
		List<Field> infoFields = AnnotationProcessor.getFields(ExtendingFieldClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoFields, Informational.class, 6);
		
		invokeFields(compareFields, ExtendingFieldClass.class);
		invokeFields(infoFields, ExtendingFieldClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Implementing interface fields
	//    Annotated fields should be detected.
	
	static interface FieldInterface {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Compare String interfaceCompareField = "";
		@Compare public String publicInterfaceCompareField = "";
		
		@Informational String interfaceInfoField = "";
		@Informational public String publicInterfaceInfoField = "";
	}
	
	static class ImplementingClass implements FieldInterface {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Compare private String privateCompareField = "";
		@Compare protected String protectedCompareField = "";
		@Compare public String publicCompareField = "";
		
		@Informational private String privateInfoField = "";
		@Informational protected String protectedInfoField = "";
		@Informational public String publicInfoField = "";	
	}
	
	@Test
	public void testImplementingFields() throws Exception {
		List<Field> compareFields = AnnotationProcessor.getFields(ImplementingClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareFields, Compare.class, 5);
		
		List<Field> infoFields = AnnotationProcessor.getFields(ImplementingClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoFields, Informational.class, 5);
		
		invokeFields(compareFields, ImplementingClass.class);
		invokeFields(infoFields, ImplementingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Extending implementing classes with fields 
	//    - Interface <- Abstract class <- concrete Class
	
	static interface TopInterface {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Compare String interfaceCompareField = "";
		@Compare public String publicInterfaceCompareField = "";
		
		@Informational String interfaceInfoField = "";
		@Informational public String publicInterfaceInfoField = "";
	}
	
	static abstract class ImplementingAbstractClass implements TopInterface {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Compare private String privateCompareField = "";
		@Compare protected String protectedCompareField = "";
		@Compare public String publicCompareField = "";
		
		@Informational private String privateInfoField = "";
		@Informational protected String protectedInfoField = "";
		@Informational public String publicInfoField = "";
	}
	
	static class ConcreteClass extends ImplementingAbstractClass {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Compare private String privateCompareField = "";
		@Compare protected String protectedCompareField = "";
		@Compare public String publicCompareField = "";
		
		@Informational private String privateInfoField = "";
		@Informational protected String protectedInfoField = "";
		@Informational public String publicInfoField = "";
	}
	
	@Test
	public void testImplementingAbstractFields() throws Exception {
		List<Field> compareFields = AnnotationProcessor.getFields(ConcreteClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareFields, Compare.class, 8);
		
		List<Field> infoFields = AnnotationProcessor.getFields(ConcreteClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoFields, Informational.class, 8);
		
		invokeFields(compareFields, ConcreteClass.class);
		invokeFields(infoFields, ConcreteClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: one interfaces extends another
	//    - fields from concrete class should be detected.
	
	interface InterfaceA {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Compare String interfaceCompareField = "";
		@Compare public String publicInterfaceCompareField = "";
		
		@Informational String interfaceInfoField = "";
		@Informational public String publicInterfaceInfoField = "";
	}
	
	interface InterfaceB extends InterfaceA {
		String interfaceField = "";
		public String publicInterfaceField = "";
		
		@Compare String interfaceCompareField = "";
		@Compare public String publicInterfaceCompareField = "";
		
		@Informational String interfaceInfoField = "";
		@Informational public String publicInterfaceInfoField = "";
	}
	
	static class ExtendedImplementingClass implements InterfaceB {
		private String privateField = "";
		protected String protectedField = "";
		public String publicField = "";
		
		@Compare private String privateCompareField = "";
		@Compare protected String protectedCompareField = "";
		@Compare public String publicCompareField = "";
		
		@Informational private String privateInfoField = "";
		@Informational protected String protectedInfoField = "";
		@Informational public String publicInfoField = "";
	}
	
	@Test
	public void testExtendedInterfaceFields() throws Exception {
		List<Field> compareFields = AnnotationProcessor.getFields(ExtendedImplementingClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareFields, Compare.class, 7);
		
		List<Field> infoFields = AnnotationProcessor.getFields(ExtendedImplementingClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoFields, Informational.class, 7);
		
		invokeFields(compareFields, ExtendedImplementingClass.class);
		invokeFields(infoFields, ExtendedImplementingClass.class);
	}
	
}
