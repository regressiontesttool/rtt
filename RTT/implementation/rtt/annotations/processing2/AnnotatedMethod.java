package rtt.annotations.processing2;

import java.lang.reflect.Method;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.Type;

public class AnnotatedMethod extends AnnotatedElement<Method> {

	public AnnotatedMethod(Method method, Value valueAnnotation) {
		super(method, valueAnnotation);
	}

	@Override
	protected String getSignature(Method method) {
		StringBuilder builder = new StringBuilder(method.getName());
		builder.append("-");
		builder.append(method.getReturnType().getSimpleName());
		builder.append("-[");
		for (Class<?> parameter : method.getParameterTypes()) {
			builder.append(parameter.getSimpleName());
			builder.append(";");
		}
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	protected Type getType(Method member) {
		return Type.METHOD;
	}

	@Override
	public Object getResult(Method member, Object object) throws Exception {
		member.setAccessible(true);
		return member.invoke(object);
	}	
}
