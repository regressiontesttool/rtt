package rtt.annotations.processing2;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.List;

import rtt.annotations.Node.Initialize;
import rtt.core.archive.output.Type;

public class InitialConstructor extends InitialMember<Constructor<?>> {

	public InitialConstructor(Constructor<?> member, Initialize initAnnotation) {
		super(member, initAnnotation);
	}

	@Override
	protected String getSignature(Constructor<?> constructor) {
		return constructor.toGenericString();
	}

	@Override
	protected Type getType(Constructor<?> constructor) {
		return Type.METHOD;
	}

	@Override
	protected Class<?>[] getParameterTypes(Constructor<?> constructor) {
		return constructor.getParameterTypes();
	}

	@Override
	public Object getResult(Constructor<?> constructor, InputStream input,
			List<String> params) throws Exception {
		
		constructor.setAccessible(true);
		try {
			if (isWithParams()) {
				return constructor.newInstance(input, params);
			} else {
				return constructor.newInstance(input);
			}
		} catch (IllegalAccessException | IllegalArgumentException 
				| InstantiationException constructorException) {
			
			throw new RuntimeException("Could not access initializing constructor.", 
					constructorException);			
		}
	}

}
