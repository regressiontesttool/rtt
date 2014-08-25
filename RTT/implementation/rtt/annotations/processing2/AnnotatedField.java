package rtt.annotations.processing2;

import java.lang.reflect.Field;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.core.archive.output.Type;

public class AnnotatedField extends AnnotatedElement<Field>{

	public AnnotatedField(Field member, Compare annotation) {
		super(member, annotation);
	}
	
	public AnnotatedField(Field member, Informational annotation) {
		super(member, annotation);
	}

	@Override
	protected String getName(Field member) {
		return member.toGenericString();
	}

	@Override
	protected Type getType(Field member) {
		return Type.FIELD;
	}

	@Override
	public Object getResult(Field member, Object object) throws IllegalArgumentException, IllegalAccessException {
		member.setAccessible(true);
		return member.get(object);
	}
	
	

}
