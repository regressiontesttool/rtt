package rtt.core.tests.junit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Set;

import rtt.annotations.processing2.ValueMember;

public class TestAnnotationUtils {
	
	public static void checkMembers(Set<ValueMember<?>> members, 
			int compareCount, int infoCount) {
		
		assertNotNull("Members was null.", members);
		assertFalse("Members was empty.", members.size() == 0);
		
		int realInfoCount = 0;
		for (ValueMember<?> valueMember : members) {
			if (valueMember.isInformational()) {
				realInfoCount++;
			}
		}
		
		assertEquals(infoCount, realInfoCount);
		assertEquals(compareCount, members.size() - infoCount);
	}
	
	public static void checkMember(
			Set<ValueMember<?>> members, 
			Class<?> fromClass, 
			String... memberNames) {
		
		if (memberNames.length > 0) {
			Arrays.sort(memberNames);
		}
				
		for (ValueMember<?> valueMember : members) {
			Member member = valueMember.getMember();
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
