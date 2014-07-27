package rtt.core.testing.generation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Parser;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.input.Input;
import rtt.core.utils.ExecutorLoader;

public class Executor {
	
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
	

	public Executor(String className, Classpath classpath, String baseDir) throws Exception {
		executorClass = loadClass(className, classpath, baseDir);
		parserAnnotation = AnnotationProcessor.getAnnotation(executorClass, Parser.class);
		
		acceptedExceptions = new ArrayList<>();
		for (Class<? extends Throwable> throwable : parserAnnotation.acceptedExceptions()) {
			acceptedExceptions.add(throwable);
		}
	}
	
	public Class<?> loadClass(String className, Classpath classpath, String baseDir)
			throws Exception {
		ExecutorLoader loader = new ExecutorLoader(classpath);
		return loader.resolveClass(className);
	}
	
	public void initialize(Input input, List<String> params) throws Throwable {
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
				throw new IllegalStateException(NO_SINGLE_INIT_METHOD);
			}
			
			Method initMethod = annotatedMethods.get(0);
			if (checkParameters(initMethod.getParameterTypes(), withParams)) {
				return initMethod;
			} else {
				throw new RuntimeException(PARAMETERS_ERROR);
			}
		}
		
		return null;
	}
	
	private Constructor<?> getInitializeConstructor(Class<?> executorClass, boolean withParams) {
		List<Constructor<?>> annotatedConstructors = AnnotationProcessor.getConstructors(executorClass, Parser.Initialize.class);
		if (annotatedConstructors != null && !annotatedConstructors.isEmpty()) {
			if (annotatedConstructors.size() > 1) {
				throw new IllegalStateException(NO_SINGLE_INIT_CONSTRUCTOR);
			}
			Constructor<?> initConstructor = annotatedConstructors.get(0);
			if (checkParameters(initConstructor.getParameterTypes(), withParams)) {
				return initConstructor;
			} else {
				throw new RuntimeException(PARAMETERS_ERROR);
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
	
	private Object invokeInitMethod(Method initMethod, InputStream inputStream, List<String> params) throws Throwable {
		try {
			Object executor = executorClass.newInstance();
			if (parserAnnotation.withParams()) {
				initMethod.invoke(executor, inputStream, params);
			} else {
				initMethod.invoke(executor, inputStream);
			}
			
			return executor;
		} catch (Throwable exception) {
			if (exception instanceof InvocationTargetException) {
				exception = exception.getCause();
			}
			
			if (isAcceptedException(exception)) {
				throw new UnsupportedOperationException("Accepted exception thrown", exception);
			} else {
				throw exception;
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
		} catch (Throwable exception) {
			if (exception instanceof InvocationTargetException) {
				exception = exception.getCause();
			}
			
			if (isAcceptedException(exception)) {
				throw new UnsupportedOperationException("Accepted exception thrown", exception);
			} else {
				throw exception;
			}
		}
	}
	
	public boolean isAcceptedException(Throwable throwable) {
		return acceptedExceptions.contains(throwable.getClass());
	}

	
	public Object getExecutor() {
		return executor;
	}
	
	public Method getASTMethod() {
		return AnnotationProcessor.getSingleMethod(executorClass, Parser.AST.class);
	}
	
}
