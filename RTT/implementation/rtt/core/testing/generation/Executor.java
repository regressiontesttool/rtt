package rtt.core.testing.generation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Node.Initialize;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.core.archive.input.Input;
import rtt.core.exceptions.AnnotationException;
import rtt.core.utils.AnnotationUtil;
import rtt.core.utils.RTTLogging;

public class Executor {
	
	private static final Class<Initialize> INIT_ANNOTATION = Initialize.class;
	
	private static final String NO_INTERFACES = 
			"Interfaces are not allowed for output data generation.";
	private static final String NO_ANONYMOUS = 
			"Anonymous classes are not allowed for output generation.";
	private static final String NO_LOCALCLASS = 
			"Local classes are currently not supported for output generation.";
	private static final String NO_NONSTATIC_MEMBERCLASS = 
			"Non-static member classes are currently not supported for output generation.";
	
	private static final String NO_NODE_ANNOTATION = 
			"The given class doesn't have a @Node annotation.";
	private static final String NO_INIT_ANNOTATION = 
			"Could not find a method or constructor annotated with @Node.Initialize.";
	private static final String NO_SINGLE_INIT_METHOD = 
			"Found more than one method annotated with @Node.Initialize.";
	private static final String NO_SINGLE_INIT_CONSTRUCTOR = 
			"Found more than one constructor annotated with @Node.Initialize.";
	private static final String PARAMETER_COUNT_ERROR = 
			"The element which is annotated with @Node.Initialize must have $$ parameter(s).";
	private static final String NO_INPUTSTREAM_PARAMETER = 
			"The first parameter needs to be an InputStream.";
	private static final String NO_STRINGARRAY_PARAMETER = 
			"The second parameter needs to be an array of strings.";
	
	private Class<?> initialObjectType = null;
	private List<Class<? extends Throwable>> acceptedExceptions;
	
	private Initialize initAnnotation;	

	public Executor(Class<?> initialObjectType) {
		checkClass(initialObjectType);
		
		Method initMethod = getInitializeMethod(initialObjectType);
		if (initMethod != null) {
			initAnnotation = initMethod.getAnnotation(INIT_ANNOTATION);
			checkParameters(initMethod.getParameterTypes(), initAnnotation.withParams());
		} else {
			Constructor<?> initConstructor = getInitializeConstructor(initialObjectType);
			if (initConstructor != null) {
				initAnnotation = initConstructor.getAnnotation(INIT_ANNOTATION);
				checkParameters(initConstructor.getParameterTypes(), initAnnotation.withParams());
			}
		}
		
		if (initAnnotation == null) {
			RTTLogging.throwException(new AnnotationException(NO_INIT_ANNOTATION));
		}
		
		acceptedExceptions = new ArrayList<>();
		for (Class<? extends Throwable> throwable : initAnnotation.acceptedExceptions()) {
			acceptedExceptions.add(throwable);
		}
		
		this.initialObjectType = initialObjectType;
	}

	private void checkClass(Class<?> initialNodeClass) {
		if (initialNodeClass.isInterface()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_INTERFACES));
		}
		
		if (initialNodeClass.isAnonymousClass()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_ANONYMOUS));
		}
		
		if (initialNodeClass.isLocalClass()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_LOCALCLASS));
		}
		
		if (initialNodeClass.isMemberClass() && !Modifier.isStatic(initialNodeClass.getModifiers())) {
			RTTLogging.throwException(new IllegalArgumentException(NO_NONSTATIC_MEMBERCLASS));;
		}
		
		if (!AnnotationUtil.isNode(initialNodeClass)) {
			RTTLogging.throwException(new AnnotationException(NO_NODE_ANNOTATION));
		}
	}

	private Method getInitializeMethod(Class<?> initialNodeClass) {
		List<Method> annotatedMethods = AnnotationProcessor.getMethods(initialNodeClass, INIT_ANNOTATION);
		if (annotatedMethods != null && !annotatedMethods.isEmpty()) {
			if (annotatedMethods.size() > 1) {
				RTTLogging.throwException(new IllegalStateException(NO_SINGLE_INIT_METHOD));
			}
			
			return annotatedMethods.get(0);
		}
		
		return null;
	}
	
	private Constructor<?> getInitializeConstructor(Class<?> initialNodeClass) {		
		List<Constructor<?>> annotatedConstructors = AnnotationProcessor.getConstructors(initialNodeClass, INIT_ANNOTATION);
		if (annotatedConstructors != null && !annotatedConstructors.isEmpty()) {
			if (annotatedConstructors.size() > 1) {
				RTTLogging.throwException(new IllegalStateException(NO_SINGLE_INIT_CONSTRUCTOR));
			}
			
			return annotatedConstructors.get(0);
		}
		
		return null;
	}
	
	private void checkParameters(Class<?>[] parameterTypes, boolean withParams) {
		int paramSize = withParams ? 2 : 1;
		
		if (parameterTypes.length != paramSize) {
			RTTLogging.throwException(new AnnotationException(PARAMETER_COUNT_ERROR.replace("$$", "" + paramSize)));
		}
		
		if (parameterTypes[0] == null || !parameterTypes[0].equals(InputStream.class)) {
			RTTLogging.throwException(new AnnotationException(NO_INPUTSTREAM_PARAMETER));
		}
		
		if (withParams && (parameterTypes[1] == null || !parameterTypes.equals(String[].class))) {
			RTTLogging.throwException(new AnnotationException(NO_STRINGARRAY_PARAMETER));
		}
	}
	
	public Object initialize(Input input, List<String> params) throws InvocationTargetException {
		Object initialObject = null;
		
		try (InputStream inputStream = new ByteArrayInputStream(input.getValue().getBytes())) {
			Method initMethod = getInitializeMethod(initialObjectType);
			if (initMethod != null) {
				initMethod.setAccessible(true);
				initialObject = invokeInitMethod(initMethod, inputStream, params);
			}
			
			Constructor<?> initConstructor = getInitializeConstructor(initialObjectType);
			if (initConstructor != null) {
				initConstructor.setAccessible(true);
				initialObject = invokeInitConstructor(initConstructor, inputStream, params);
			}
			
			if (initialObject == null) {
				throw new RuntimeException(NO_INIT_ANNOTATION);
			}
		} catch (IOException e) {
			RTTLogging.error("Could not access input stream.", e);
		}
		
		return initialObject;
	}	
	
	private Object invokeInitMethod(Method initMethod, InputStream inputStream, 
			List<String> params) throws InvocationTargetException {
		
		try {
			Constructor<?> constructor = initialObjectType.getDeclaredConstructor();			
			constructor.setAccessible(true);
			
			Object executor = constructor.newInstance();
			if (initAnnotation.withParams()) {
				initMethod.invoke(executor, inputStream, params);
			} else {
				initMethod.invoke(executor, inputStream);
			}
			
			return executor;
			
		} catch (IllegalAccessException | IllegalArgumentException methodException) {
			throw new RuntimeException("Could not access initializing method.", methodException);
		} catch (NoSuchMethodException | InstantiationException constructorException) {
			throw new RuntimeException("Could not get parameter-less constructor.", constructorException);
		}
	}

	private Object invokeInitConstructor(Constructor<?> initConstructor, 
			InputStream input, List<String> params) throws InvocationTargetException {
		
		try {
			if (initAnnotation.withParams()) {
				return initConstructor.newInstance(input, params);
			} else {
				return initConstructor.newInstance(input);
			}
		} catch (IllegalAccessException | IllegalArgumentException 
				| InstantiationException constructorException) {
			
			throw new RuntimeException("Could not access initializing constructor.", 
					constructorException);			
		}
	}
	
	public boolean isAcceptedException(Throwable exception) {
		return acceptedExceptions.contains(exception.getClass());
	}
	
	public Class<?> getInitialObjectType() {
		return initialObjectType;
	}
}
