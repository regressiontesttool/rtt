package rtt.core.testing.generation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Parser;
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
	private static final String PARAMETERS_ERROR = 
			"The method or constructor which is annotated with @Parser.Initialize does not have the corresponding parameters.";	
	
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
	
	public void initialize(Input input, List<String> params) throws InvocationTargetException, InstantiationException {
		InputStream inputStream = new ByteArrayInputStream(input.getValue().getBytes());
		
		Method initMethod = getInitializeMethod(executorClass, parserAnnotation.withParams());
		if (initMethod != null) {
			executor = invokeInitMethod(initMethod, inputStream, params);
		}
		
		Constructor<?> initConstructor = getInitializeConstructor(executorClass, parserAnnotation.withParams());
		if (initConstructor != null) {
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
			if (checkParameters(initMethod.getParameterTypes(), withParams)) {
				return initMethod;
			} else {
				RTTLogging.throwException(new RuntimeException(PARAMETERS_ERROR));
			}
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
			if (checkParameters(initConstructor.getParameterTypes(), withParams)) {
				return initConstructor;
			} else {
				RTTLogging.throwException(new RuntimeException(PARAMETERS_ERROR));
			}			
		}
		
		return null;
	}

	private boolean checkParameters(Class<?>[] parameterTypes, boolean withParams) {
		int paramSize = withParams ? 2 : 1;
		
		if (parameterTypes.length != paramSize) {
			return false;
		}
		
		if (parameterTypes[0] == null || !parameterTypes[0].equals(InputStream.class)) {
			return false;
		}
		
		if (withParams && (parameterTypes[1] == null || !parameterTypes.equals(String[].class))) {
			return false;
		}
		
		return true;	
	}
	
	private Object invokeInitMethod(Method initMethod, InputStream inputStream, List<String> params) throws InvocationTargetException, InstantiationException {
		try {
			
			Object executor = executorClass.newInstance();
			if (parserAnnotation.withParams()) {
				initMethod.invoke(executor, inputStream, params);
			} else {
				initMethod.invoke(executor, inputStream);
			}
			
			return executor;
			
		} catch (IllegalAccessException | IllegalArgumentException methodException) {
			throw new RuntimeException("Could not access initializing method.", methodException);
		} catch (InvocationTargetException invocationException) {			
			if (isAcceptedException(invocationException)) {
				throw new UnsupportedOperationException("Accepted exception thrown", invocationException);
			} else {
				throw invocationException;
			}
		}

	}

	private Object invokeInitConstructor(Constructor<?> initConstructor, InputStream input, List<String> params) throws InvocationTargetException, InstantiationException {
		try {
			if (parserAnnotation.withParams()) {
				return initConstructor.newInstance(input, params);
			} else {
				return initConstructor.newInstance(input);
			}
		} catch (IllegalAccessException | IllegalArgumentException constructorException) {
			throw new RuntimeException("Could not access initializing constructor.", constructorException);			
		} catch (InvocationTargetException invocationException) {			
			if (isAcceptedException(invocationException)) {
				throw new UnsupportedOperationException("Accepted exception thrown", invocationException);
			} else {
				throw invocationException;
			}
		}
	}
	
	public boolean isAcceptedException(InvocationTargetException exception) {
		return acceptedExceptions.contains(exception.getCause());
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
