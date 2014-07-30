package rtt.core.tests.junit.annotations;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.NewAnnotationProcessor;
import rtt.core.tests.junit.utils.AnnotationUtils;

public class FieldProcessingTests {

	@Before
	public void setUp() throws Exception {
	}

	// --------------------------------------------------------------
	// Test: permuted fields
	//	 Only annotated fields should be 
	//	 detected by annotation processing
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3

	static class PermutedFieldsClass {
		private String privateString;
		protected String protectedString;
		public String publicString;

		@Compare private String privateCompareString;
		@Compare protected String protectedCompareString;
		@Compare public String publicCompareString;

		@Informational private String privateInfoString;
		@Informational protected String protectedInfoString;
		@Informational public String publicInfoString;
	}

	@Test
	public void testPermutedFields() throws Exception {
		Collection<Field> compareFields = NewAnnotationProcessor.getFields(
				PermutedFieldsClass.class, Compare.class);

		AnnotationUtils.checkElements(compareFields, Compare.class, 3);

		Collection<Field> infoFields = NewAnnotationProcessor.getFields(
				PermutedFieldsClass.class, Informational.class);

		AnnotationUtils.checkElements(infoFields, Informational.class, 3);
	}

}
