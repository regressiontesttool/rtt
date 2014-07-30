package rtt.core.testing.generation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Parser;
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
	
	private static final String NO_PARSER_ANNOTATION = 
			"The given class doesn't have a @Parser annotation.";
	private static final String NO_INIT = 
			"Could not find a method or constructor annotated with @Parser.Initialize.";
	private static final String NO_SINGLE_INIT_METHOD = 
			"Found more than one method annotated with @Parser.Initialize.";
	private static final String NO_SINGLE_INIT_CONSTRUCTOR = 
			"Found more than one method annotated with @Parser.Initialize.";
	private static final String PARAMETER_COUNT_ERROR = 
			"The element which is annotated with @Parser.Initialize must have $$ parameter(s).";
	private static final String NO_INPUTSTREAM_PARAMETER = 
			"The first parameter needs to be an InputStream.";
	private static final String NO_STRINGARRAY_PARAMETER = 
			"The second parameter needs to be an array of strings.";
	
	private Class<?> executorClass = null;
	private Object executor = null;	
	private List<Class<? extends Throwable>> acceptedExceptions;
	
	private Parser parserAnnotation;	

	public Executor(Class<?> executorClass) {
		checkClass(executorClass);
		
		parserAnnotation = AnnotationProcessor.getAnnotation(executorClass, Parser.class);
		if (parserAnnotation == null) {
			RTTLogging.throwException(new RuntimeException(NO_PARSER_ANNOTATION));
		}
		
		this.executorClass = executorClass;
		acceptedExceptions = new ArrayList<>();
		for (Class<? extends Throwable> throwable : parserAnnotation.acceptedExceptions()) {
			acceptedExceptions.add(throwable);
		}
	}

	private void checkClass(Class<?> executorClass) {
		if (executorClass.isInterface()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_INTERFACES));
		}
		
		if (executorClass.isAnonymousClass()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_ANONYMOUS));
		}
		
		if (executorClass.isLocalClass()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_LOCALCLASS));
		}
		
		if (executorClass.isMemberClass() && !Modifier.isStatic(executorClass.getModifiers())) {
			RTTLogging.throwException(new IllegalArgumentException(NO_NONSTATIC_MEMBERCLASS));;
		}
	}
	
	public void initialize(Input input, List<String> params) throws Throwable {
		InputStream inputStream = new ByteArrayInputStream(input.getValue().getBytes());
		
		Method initMethod = getInitializeMethod(executorClass, parserAnnotation.withParams());
		if (initMethod != null) {
			initMethod.setAccessible(true);
			executor = invokeInitMethod(initMethod, inputStream, params);
		}
		
		Constructor<?> initConstructor = getInitializeConstructor(executorClass, parserAnnotation.withParams());
		if (initConstructor != null) {
			initConstructor.setAccessible(true);
			executor = invokeInitConstructor(initConstructor, inputStream, params);
		}
		
		if (executor == null) {
			throw new RuntimeException(NO_INIT);
		}
	}

	private Method getInitializeMethod(Class<?> executorClass, boolean withParams) {
		List<Method> annotatedMethods = AnnotationProcessor.getMethods(executorClass, Parser.Initialize.class);
		if (annotatedMethods != null && !annotatedMethods.isEmpty()) {
			if (annotatedMethods.size() > 1) {
				RTTLogging.throwException(new IllegalStateException(NO_SINGLE_INIT_METHOD));
			}
			
			Method initMethod = annotatedMethods.get(0);
			checkParameters(initMethod.getParameterTypes(), withParams);
			return initMethod;
		}
		
		return null;
	}
	
	private Constructor<?> getInitializeConstructor(Class<?> executorClass, boolean withParams) {
		List<Constructor<?>> annotatedConstructors = AnnotationProcessor.getConstructors(executorClass, Parser.Initialize.class);
		if (annotatedConstructors != null && !annotatedConstructors.isEmpty()) {
			if (annotatedConstructors.size() > 1) {
				RTTLogging.throwException(new IllegalStateException(NO_SINGLE_INIT_CONSTRUCTOR));
			}
			
			Constructor<?> initConstructor = annotatedConstructors.get(0);
			checkParameters(initConstructor.getParameterTypes(), withParams);
			return initConstructor;		
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
	
	private Object invokeInitMethod(Method initMethod, InputStream inputStream, List<String> params) throws Throwable {
		try {
			Constructor<?> constructor = executorClass.getDeclaredConstructor();
			
			constructor.setAccessible(true);
			
			Object executor = constructor.newInstance();
			if (parserAnnotation.withParams()) {
				initMethod.invoke(executor, inputStream, params);
			} else {
				initMethod.invoke(executor, inputStream);
			}
			
			return executor;
			
		} catch (IllegalAccessException | IllegalArgumentException methodException) {
			throw new RuntimeException("Could not access initializing method.", methodException);
		} catch (NoSuchMethodException constructorException) {
			throw new RuntimeException("Could not get parameter-less constructor.", constructorException);
		} catch (InvocationTargetException invocationException) {
			Throwable cause = invocationException.getCause();
			if (isAcceptedException(cause)) {
				throw new UnsupportedOperationException("Accepted exception are currently not supported.", cause);
			} else {
				throw cause;
			}
		}

	}

	private Object invokeInitConstructor(Constructor<?> initConstructor, InputStream input, List<String> params) throws Throwable {
		try {
			if (parserAnnotation.withParams()) {
				return initConstructor.newInstance(input, params);
			} else {
				return initConstructor.newInstance(input);
			}
		} catch (IllegalAccessException | IllegalArgumentException constructorException) {
			throw new RuntimeException("Could not access initializing constructor.", constructorException);			
		} catch (InvocationTargetException invocationException) {
			Throwable cause = invocationException.getCause();
			if (isAcceptedException(cause)) {
				throw new UnsupportedOperationException("Accepted exception thrown", cause);
			} else {
				throw cause;
			}
		}
	}
	
	public boolean isAcceptedException(Throwable exception) {
		return acceptedExceptions.contains(exception.getClass());
	}
	
	public Class<?> getExecutorClass() {
		return executorClass;
	}
	
	public Object getExecutor() {
		return executor;
	}
	
	public Method getASTMethod() {
		return AnnotationProcessor.getSingleMethod(executorClass, Parser.AST.class);
	}	
}
