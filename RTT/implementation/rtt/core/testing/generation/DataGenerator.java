package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.utils.ExecutorLoader;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private static final Class<? extends Annotation> NODE_ANNOTATION = rtt.annotations.Node.class;
	private static final Class<? extends Annotation> COMPARE_ANNOTATION = Compare.class;
	private static final Class<? extends Annotation> INFORMATIONAL_ANNOTATION = Informational.class;
	
	private static final String NO_AST_METHOD = "Could not find a method annotated with @Parser.AST";
	private static final String ONLY_NONVOID_METHODS = "Only methods with a non-void return type allowed.";
	private static final String ONLY_PARAMETERLESS_METHODS = "Only methods without parameters allowed.";
	
	private DataGenerator() {}
	
	public static void createOutput(Executor executor, Output outputData) throws InvocationTargetException {
		
		Method astMethod = executor.getASTMethod();
		if (astMethod == null) {
			RTTLogging.throwException(new RuntimeException(NO_AST_METHOD));
		}		
		
		Object astMethodResult = null;
		try {
			astMethod.setAccessible(true);
			astMethodResult = astMethod.invoke(executor.getExecutor());
		} catch (IllegalAccessException | IllegalArgumentException exception) {
			throw new RuntimeException("Could not invoke method. ", exception);
		}
		
		Value astValue = new Value();
		astValue.setName(astMethod.getName());
		astValue.setType(Type.METHOD);
		handleResult(astMethodResult, astValue);
		
		outputData.setAST(astValue);
	}
	
	private static void handleResult(final Object currentObject, final Value member) throws InvocationTargetException {
		
		if (currentObject instanceof Object[]) {
			handleArray((Object[]) currentObject, member);
		} else if (currentObject instanceof Iterable<?>) {
			handleIterable((Iterable<?>) currentObject, member);			
		} else if (currentObject != null) {
			handleObject(currentObject, member);
		} else {
			member.setIsNull(true);
		}
	}
	
	private static void handleArray(final Object[] objectArray, final Value member) throws InvocationTargetException {
		for (Object object : objectArray) {
			handleObject(object, member);
		}
	}
	
	private static void handleIterable(final Iterable<?> iterable, final Value member) throws InvocationTargetException {		
		for (Object object : iterable) {
			handleObject(object, member);
		}
	}
	
	private static void handleObject(final Object object, final Value member) throws InvocationTargetException {
		if (AnnotationProcessor.hasAnnotation(object.getClass(), NODE_ANNOTATION)) {
			handleNode(object, member);
		} else {
			member.setValue(object.toString());
		}
	}

	private static void handleNode(final Object currentObject, final Value member) throws InvocationTargetException {
		
		Class<?> objectType = currentObject.getClass();		
		
		Node resultNode = new Node();
		resultNode.setFullName(objectType.getName());
		resultNode.setSimpleName(objectType.getSimpleName());	
		
		resultNode.getValues().addAll(processFields(currentObject, COMPARE_ANNOTATION, member.isInformational()));
		resultNode.getValues().addAll(processFields(currentObject, INFORMATIONAL_ANNOTATION, member.isInformational()));
		
		resultNode.getValues().addAll(processMethods(currentObject, COMPARE_ANNOTATION, member.isInformational()));
		resultNode.getValues().addAll(processMethods(currentObject, INFORMATIONAL_ANNOTATION, member.isInformational()));
		
		member.getNode().add(resultNode);
	}
	
	private static List<Value> processFields(Object parentObject, Class<? extends Annotation> annotation, boolean parentIsInformational) throws InvocationTargetException {
		List<Value> resultList = new ArrayList<>();
		List<Field> annotatedFields = AnnotationProcessor.getFields(parentObject.getClass(), annotation);
		
		for (Field field : annotatedFields) {
			Value fieldValue = new Value();
			fieldValue.setName(field.getName());
			fieldValue.setType(Type.FIELD);
			fieldValue.setInformational(isInformational(parentIsInformational, field));
			
			field.setAccessible(true);
			try {
				handleResult(field.get(parentObject), fieldValue);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				RTTLogging.throwException(
						new RuntimeException("Could not access field.", e));
			}
			
			resultList.add(fieldValue);
		}
		
		return resultList;
	}
	
	private static List<Value> processMethods(final Object parentObject, Class<? extends Annotation> annotation, boolean parentIsInformational) 
			throws InvocationTargetException {
		
		List<Value> resultList = new ArrayList<>();
		List<Method> annotatedMethods = AnnotationProcessor.getMethods(parentObject.getClass(), annotation);
		
		for (Method method : annotatedMethods) {
			if (method.getReturnType() == Void.TYPE) {
				RTTLogging.warn(ONLY_NONVOID_METHODS);
				continue;
			}
			
			if (method.getParameterTypes().length > 0) {
				RTTLogging.warn(ONLY_PARAMETERLESS_METHODS);
				continue;
			}
			
			Value methodValue = new Value();
			methodValue.setName(method.getName());
			methodValue.setType(Type.METHOD);
			methodValue.setInformational(isInformational(parentIsInformational, method));
			
			method.setAccessible(true);			
			try {
				handleResult(method.invoke(parentObject), methodValue);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("Could not invoke method.", e);
			}
			
			resultList.add(methodValue);
		}
		
		return resultList;
	}
	
	private static boolean isInformational(boolean parentIsInformational, AnnotatedElement element) {
		return parentIsInformational || element.isAnnotationPresent(INFORMATIONAL_ANNOTATION);
	}
	
	public static Output generateOutput(Input input, List<String> params, 
			Executor executor) throws Throwable {
			
		Output outputData = new Output();

		if (executor != null) {
			
			try {
				RTTLogging.debug("Initializing parser: " + 
						executor.getExecutorClass().getSimpleName());
				
				executor.initialize(input, params);
				
				RTTLogging.debug("Generating output data ...");
				DataGenerator.createOutput(executor, outputData);
				
			} catch (InvocationTargetException invocationException) {
				Throwable cause = invocationException.getCause();
				if (executor.isAcceptedException(cause)) {
					throw new UnsupportedOperationException(
							"Accepted exception are currently not supported.", cause);
				} else {
					throw cause;
				}
			}
		}

		return outputData;
	}

	/**
	 * <p>Tries to locate the {@link ParserExecutor} via the class loader</p>
	 * 
	 * <p>Instantiate the parser through
	 * {@link Executor#initialize(Input, List)} before use!</p>
	 * 
	 * @param config the {@link Configuration}
	 * @param baseDir the base directory for searching
	 * @return a {@link ParserExecutor} or null (if config is empty)
	 * @throws Exception
	 *             mainly exceptions during class loading
	 */
	public static Executor locateParserExecutor(Configuration config,
			String baseDir) throws Exception {
		
		String parserClass = config.getParserClass();
		if (parserClass == null || parserClass.trim().isEmpty()) {
			throw new IllegalStateException("The given configuration contains no parser.");
		}
		
		RTTLogging.info("Parser: " + parserClass);
		ExecutorLoader loader = new ExecutorLoader(config.getClasspath());		
		return new Executor(loader.resolveClass(parserClass.trim()));
	}

}
