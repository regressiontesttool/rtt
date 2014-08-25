package rtt.annotations.processing2;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import rtt.annotations.Node.Initialize;
import rtt.core.archive.output.Type;

public class InitialMethod extends InitialMember<Method> {

	public InitialMethod(Method method, Initialize initAnnotation) {
		super(method, initAnnotation);
	}

	@Override
	protected String getSignature(Method method) {
		return method.toGenericString();
	}

	@Override
	protected Type getType(Method method) {
		return Type.METHOD;
	}

	@Override
	protected Class<?>[] getParameterTypes(Method method) {
		return method.getParameterTypes();
	}

	@Override
	public Object getResult(Method method, InputStream input, 
			List<String> params) throws Exception {

		method.setAccessible(true);
		Class<?> declaringClass = method.getDeclaringClass();
		
		try {
			Constructor<?> constructor = declaringClass.getDeclaredConstructor();			
			constructor.setAccessible(true);
			
			Object initialObject = constructor.newInstance();
			if (isWithParams()) {
				method.invoke(initialObject, input, params);
			} else {
				method.invoke(initialObject, input);
			}
			
			return initialObject;
			
		} catch (IllegalAccessException | IllegalArgumentException methodException) {
			throw new RuntimeException("Could not access initializing method.", methodException);
		} catch (NoSuchMethodException | InstantiationException constructorException) {
			throw new RuntimeException("Could not get parameter-less constructor.", constructorException);
		}
	}
}
