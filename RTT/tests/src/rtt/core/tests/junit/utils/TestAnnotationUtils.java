package rtt.core.tests.junit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import rtt.annotations.processing2.ValueMember;

public class TestAnnotationUtils {
	
	public static void countMembers(Set<ValueMember<?>> members, 
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
	
	public static void checkOrder(Set<ValueMember<?>> valueMembers, String... signatures) {
		assertEquals(signatures.length, valueMembers.size());
		
		int index = 0;
		Iterator<ValueMember<?>> iterator = valueMembers.iterator();
		while (iterator.hasNext()) {
			assertEquals(signatures[index], iterator.next().getSignature());
			index++;
		}		
	}
	
	public static void executeMembers(Set<ValueMember<?>> members, Class<?> classType) {
		try {
			Object object = classType.newInstance();
			for (ValueMember<?> member : members) {
				member.getResult(object);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public static void checkMember(
			Set<ValueMember<?>> members, 
			Class<?> fromClass, 
			String... memberNames) {
		
		Arrays.sort(memberNames);
		boolean checkNames = memberNames.length > 0;
		
		if (checkNames) {
			for (String memberName : memberNames) {
				boolean memberFound = false;
				for (ValueMember<?> valueMember : members) {
					if (valueMember.getMember().getName().equals(memberName)) {
						Member member = valueMember.getMember();
						if (member.getDeclaringClass().equals(fromClass)) {
							memberFound = true;
							break;
						}
					}
				}
				
				if (!memberFound) {
					String fromClassName = fromClass.getSimpleName();
					fail("The member '" + memberName + "' from '" + fromClassName + "' could not be found.");
				}
			}
		} else {
			for (ValueMember<?> valueMember : members) {
				Member member = valueMember.getMember();
				if (!member.getDeclaringClass().equals(fromClass)) {
					String fromClassName = fromClass.getSimpleName();
					String declaringClassName = member.getDeclaringClass().getSimpleName();
					String memberName = member.getClass().getSimpleName() + " " + member.getName();

					fail("The member '" + memberName + "' was declared by " + declaringClassName + " and not by " + fromClassName);
				}
			}			
		}
	}
}
