package rtt.annotations.processing2;

import java.lang.reflect.Field;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.Type;

public class ValueField extends ValueMember<Field>{
	
	public ValueField(Field field, Value valueAnnotation) {
		super(field, Type.FIELD, valueAnnotation);
	}

	@Override
	protected String getSignature(Field member) {
		return member.getDeclaringClass().getSimpleName() + "." + member.getName();
	}

	@Override
	public Object getResult(Field member, Object object) 
			throws ReflectiveOperationException {
		
		member.setAccessible(true);
		return member.get(object);
	}
}
