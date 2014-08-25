package rtt.annotations.processing2;

import java.lang.reflect.Field;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.Type;

public class AnnotatedField extends AnnotatedElement<Field>{
	
	public AnnotatedField(Field field, Value valueAnnotation) {
		super(field, valueAnnotation);
	}

	@Override
	protected String getSignature(Field member) {
		return member.toGenericString();
	}

	@Override
	protected Type getType(Field member) {
		return Type.FIELD;
	}

	@Override
	public Object getResult(Field member, Object object) throws Exception {
		member.setAccessible(true);
		return member.get(object);
	}
}
