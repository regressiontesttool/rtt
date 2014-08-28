package rtt.annotations.processing2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.Type;

public class ValueMethod extends ValueMember<Method> {

	public ValueMethod(Method method, Value valueAnnotation) {
		super(method, Type.METHOD, valueAnnotation);
	}

	@Override
	protected String getSignature(Method method) {
		if (Modifier.isPrivate(method.getModifiers())) {
			return method.getDeclaringClass().getSimpleName()
					+ "." + method.getName();
		}
		
		return method.getName();
	}

	@Override
	public Object getResult(Method member, Object object) 
			throws ReflectiveOperationException {
		
		member.setAccessible(true);
		return member.invoke(object);
	}	
}
