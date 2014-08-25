package rtt.annotations.processing2;

import java.lang.reflect.Method;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.core.archive.output.Type;

public class AnnotatedMethod extends AnnotatedElement<Method> {

	public AnnotatedMethod(Method member, Compare annotation) {
		super(member, annotation);
	}

	public AnnotatedMethod(Method member, Informational annotation) {
		super(member, annotation);
	}

	@Override
	protected String getName(Method member) {
		// TODO Auto-generated method stub
		return null;
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
