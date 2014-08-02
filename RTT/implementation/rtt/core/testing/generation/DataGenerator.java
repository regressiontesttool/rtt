package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
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
import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
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
		
		DataGenerator generator = new DataGenerator();
		outputData.setAST(generator.handleResult(
				astMethodResult, astMethod.getName(), Type.METHOD));
	}
	
	private Element handleResult(final Object object, String name, Type type) throws InvocationTargetException {
		if (object != null) {
			if (object.getClass().isArray()) {				
				return handleArray(object, name, type);
			}
			
			if (object instanceof Iterable<?>) {
				return handleIterable((Iterable<?>) object, name, type);
			}
		}	
		
		return handleObject(object, name, type);
	}
	
	private Element handleArray(final Object array, String name, Type type) throws InvocationTargetException {
		Node node = createNode(array, name, type);
		
		String elementName = null;		
		for (int index = 0; index < Array.getLength(array); index++) {
			elementName = name + "[" + index + "]";
			Element element = handleResult(Array.get(array, index), 
					elementName, Type.ELEMENT);
			
			node.getElement().add(element);
		}
		
		return node;
	}

	private Element handleIterable(final Iterable<?> iterable, String name, Type type) throws InvocationTargetException {
		Node node = createNode(iterable, name, type);
		
		int index = 0;
		String elementName = null;
		
		for (Object object : iterable) {
			elementName = name + "[" + index + "]";
			Element element = handleResult(object, elementName, Type.ELEMENT);
			node.getElement().add(element);
			index++;
		}
		
		return node;
	}
	
	private Node createNode(final Object object, String name, Type type) {
		Class<?> objectType = object.getClass();
		
		Node node = new Node();
		node.setName(name);
		node.setType(type);
		
		node.setFullName(objectType.getName());
		node.setSimpleName(objectType.getSimpleName());
		
		return node;
	}

	private Element handleObject(final Object object, String name, Type type) throws InvocationTargetException {
		if (object != null && hasNodeAnnotation(object)) {
			return handleNode(object, name, type);
		}
		
		return createValue(object, name, type);
	}

	private Value createValue(Object value, String name, Type type) {
		Value valueElement = new Value();
		
		valueElement.setName(name);
		valueElement.setType(type);
		if (value != null) {
			valueElement.setValue(value.toString());
		}
		
		return valueElement;
	}
	
	private boolean hasNodeAnnotation(final Object object) {
		if (object != null) {
			return AnnotationProcessor.hasAnnotation(
					object.getClass(), NODE_ANNOTATION);
		}
		
		return false;
	}

	private Element handleNode(final Object object, String name, Type type) throws InvocationTargetException {
		Node resultNode = createNode(object, name, type);
		
		resultNode.getElement().addAll(processFields(object, COMPARE_ANNOTATION, false));
		resultNode.getElement().addAll(processFields(object, INFORMATIONAL_ANNOTATION, true));
		
		resultNode.getElement().addAll(processMethods(object, COMPARE_ANNOTATION, false));
		resultNode.getElement().addAll(processMethods(object, INFORMATIONAL_ANNOTATION, true));
		
		return resultNode;
	}
	
	private List<Element> processFields(final Object parentObject, Class<? extends Annotation> annotation, boolean parentIsInformational) throws InvocationTargetException {
		List<Element> resultList = new ArrayList<>();
		List<Field> annotatedFields = AnnotationProcessor.getFields(parentObject.getClass(), annotation);
		
		for (Field field : annotatedFields) {
			try {
				field.setAccessible(true);
				resultList.add(handleResult(
						field.get(parentObject), field.getName(), Type.FIELD));
				
			} catch (IllegalAccessException | IllegalArgumentException e) {
				RTTLogging.throwException(
						new RuntimeException("Could not access field.", e));
			}
		}
		
		return resultList;
	}
	
	private List<Element> processMethods(final Object parentObject, Class<? extends Annotation> annotation, boolean parentIsInformational) 
			throws InvocationTargetException {
		
		List<Element> resultList = new ArrayList<>();
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
			
			
			try {
				method.setAccessible(true);
				resultList.add(handleResult(
						method.invoke(parentObject), method.getName(), Type.METHOD));
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("Could not invoke method.", e);
			}
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
