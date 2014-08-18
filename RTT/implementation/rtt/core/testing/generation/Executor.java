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

import rtt.annotations.Node;
import rtt.annotations.Node.Initialize;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.core.archive.input.Input;
import rtt.core.utils.RTTLogging;

public class Executor {
	
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
	private static final String NO_INIT_MEMBER = 
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
	
	private Class<?> executorClass = null;
	private Object initialNode = null;	
	private List<Class<? extends Throwable>> acceptedExceptions;
	
	private Initialize initAnnotation;	

	public Executor(Class<?> initialNodeClass) {
		checkClass(initialNodeClass);
		
		Method initMethod = getInitializeMethod(initialNodeClass);
		if (initMethod != null) {
			initAnnotation = initMethod.getAnnotation(Initialize.class);
			checkParameters(initMethod.getParameterTypes(), initAnnotation.withParams());
		} else {
			Constructor<?> initConstructor = getInitializeConstructor(initialNodeClass);
			if (initConstructor != null) {
				initAnnotation = initConstructor.getAnnotation(Initialize.class);
				checkParameters(initConstructor.getParameterTypes(), initAnnotation.withParams());
			}
		}
		
		if (initAnnotation == null) {
			RTTLogging.throwException(new RuntimeException(NO_INIT_MEMBER));
		}
		
		acceptedExceptions = new ArrayList<>();
		for (Class<? extends Throwable> throwable : initAnnotation.acceptedExceptions()) {
			acceptedExceptions.add(throwable);
		}
		
		this.executorClass = initialNodeClass;
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
		
		if (!initialNodeClass.isAnnotationPresent(Node.class)) {
			RTTLogging.throwException(new RuntimeException(NO_NODE_ANNOTATION));
		}
	}

	private Method getInitializeMethod(Class<?> initialNodeClass) {
		List<Method> annotatedMethods = AnnotationProcessor.getMethods(executorClass, Initialize.class);
		if (annotatedMethods != null && !annotatedMethods.isEmpty()) {
			if (annotatedMethods.size() > 1) {
				RTTLogging.throwException(new IllegalStateException(NO_SINGLE_INIT_METHOD));
			}
			
			return annotatedMethods.get(0);
		}
		
		return null;
	}
	
	private Constructor<?> getInitializeConstructor(Class<?> initialNodeClass) {		
		List<Constructor<?>> annotatedConstructors = AnnotationProcessor.getConstructors(initialNodeClass, Initialize.class);
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
			RTTLogging.throwException(new RuntimeException(PARAMETER_COUNT_ERROR.replace("$$", "" + paramSize)));
		}
		
		if (parameterTypes[0] == null || !parameterTypes[0].equals(InputStream.class)) {
			RTTLogging.throwException(new RuntimeException(NO_INPUTSTREAM_PARAMETER));
		}
		
		if (withParams && (parameterTypes[1] == null || !parameterTypes.equals(String[].class))) {
			RTTLogging.throwException(new RuntimeException(NO_STRINGARRAY_PARAMETER));
		}
	}
	
	public void initialize(Input input, List<String> params) throws InvocationTargetException {
		InputStream inputStream = new ByteArrayInputStream(input.getValue().getBytes());
		
		Method initMethod = getInitializeMethod(executorClass);
		if (initMethod != null) {
			initMethod.setAccessible(true);
			initialNode = invokeInitMethod(initMethod, inputStream, params);
		}
		
		Constructor<?> initConstructor = getInitializeConstructor(executorClass);
		if (initConstructor != null) {
			initConstructor.setAccessible(true);
			initialNode = invokeInitConstructor(initConstructor, inputStream, params);
		}
		
		if (initialNode == null) {
			throw new RuntimeException(NO_INIT_MEMBER);
		}
		
		try {
			inputStream.close();
		} catch (IOException e) {
			RTTLogging.throwException(
					new RuntimeException("Could not close input stream.", e));
		}
	}	
	
	private Object invokeInitMethod(Method initMethod, InputStream inputStream, 
			List<String> params) throws InvocationTargetException {
		
		try {
			Constructor<?> constructor = executorClass.getDeclaredConstructor();
			
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
	
	public Class<?> getExecutorClass() {
		return executorClass;
	}
	
	public Object getInitialNode() {
		return initialNode;
	}
}
