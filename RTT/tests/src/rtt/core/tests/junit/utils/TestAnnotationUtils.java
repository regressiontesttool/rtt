package rtt.core.tests.junit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.List;

public class TestAnnotationUtils {
	
	public static void checkElements(
			List<? extends AnnotatedElement> elements, 
			Class<? extends Annotation> annotation, int itemCount) {
		
		assertNotNull("Elements was null.", elements);
		assertFalse("Elements was empty.", elements.isEmpty());
		assertEquals(itemCount, elements.size());
	}
	
	public static void checkMember(
			List<? extends Member> members, 
			Class<?> fromClass, 
			String... memberNames) {
		
		if (memberNames.length > 0) {
			Arrays.sort(memberNames);
		}
				
		for (Member member : members) {
			
			boolean checkMethod = true;
			if (memberNames.length > 0) {
				checkMethod = Arrays.binarySearch(
						memberNames, member.getName()) >= 0;
			}
			
			if (checkMethod && !member.getDeclaringClass().equals(fromClass)) {
				String fromClassName = fromClass.getSimpleName();
				String declaringClassName = member.getDeclaringClass().getSimpleName();
				String memberName = member.getClass().getSimpleName() + " " + member.getName();

				fail("The member '" + memberName + "' was declared by " + declaringClassName + " and not by " + fromClassName);
			}
		}
	}
}
