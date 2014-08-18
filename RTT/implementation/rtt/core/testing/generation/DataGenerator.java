package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
		Element initElement = new Element();
		initElement.setAddress("1");
		initElement.setGeneratorName(executor.getExecutorClass().getName());
		initElement.setGeneratorType(Type.OBJECT);
		
		DataGenerator generator = new DataGenerator();		
		outputData.setInitialNode(generator.handleObject(executor.getInitialNode(), initElement));
	}
	
	private Element handleObject(final Object object, Element prototype) throws InvocationTargetException {
		if (object != null && hasNodeAnnotation(object.getClass())) {
			return handleNode(object, prototype);
		}
		
		return GeneratorUtil.createValue(object, prototype);
	}
	
	private boolean hasNodeAnnotation(final Class<?> objectType) {
		return AnnotationProcessor.hasAnnotation(objectType, NODE_ANNOTATION);
	}
	
	private Element handleNode(final Object object, Element prototype) throws InvocationTargetException {
		
		Element result = null;
		String address = GeneratorUtil.getObjectAddress(object);
		
		if (address != null && !address.equals("")) {
			result = GeneratorUtil.createValue(address, prototype);
		} else {
			Node resultNode = GeneratorUtil.createNode(object, prototype);
			GeneratorUtil.setObjectAddress(object, resultNode.getAddress());
			
			resultNode.getElement().addAll(processFields(object, resultNode, COMPARE_ANNOTATION));
			resultNode.getElement().addAll(processFields(object, resultNode, INFORMATIONAL_ANNOTATION));
			
			resultNode.getElement().addAll(processMethods(object, resultNode, COMPARE_ANNOTATION));
			resultNode.getElement().addAll(processMethods(object, resultNode, INFORMATIONAL_ANNOTATION));
			
			result = resultNode;
		}
		
		return result;
	}
	
	private List<Element> processFields(final Object nodeObject, final Node parentNode, 
			Class<? extends Annotation> annotation) throws InvocationTargetException {
		
		List<Element> resultList = new ArrayList<>();
		List<Field> annotatedFields = AnnotationProcessor.getFields(nodeObject.getClass(), annotation);
		
		int fieldAddress = 1;
		Element element = null;
		
		for (Field field : annotatedFields) {
			element = new Element();
			element.setAddress(parentNode.getAddress() + "." + fieldAddress);
			element.setGeneratorName(field.getName());
			element.setGeneratorType(Type.FIELD);
			element.setInformational(isInformational(parentNode, field));
			
			try {
				field.setAccessible(true);
				resultList.add(handleResult(field.get(nodeObject), element));
				fieldAddress++;
			} catch (IllegalAccessException | IllegalArgumentException e) {
				RTTLogging.throwException(
						new RuntimeException("Could not access field.", e));
			}
		}
		
		return resultList;
	}
	
	private List<Element> processMethods(final Object nodeObject, final Node parentNode,
			Class<? extends Annotation> annotation) throws InvocationTargetException {
		
		List<Element> resultList = new ArrayList<>();
		List<Method> annotatedMethods = AnnotationProcessor.getMethods(nodeObject.getClass(), annotation);
		
		Element element = null;
		int methodAddress = 1;
		
		for (Method method : annotatedMethods) {
			if (method.getReturnType() == Void.TYPE) {
				RTTLogging.warn(ONLY_NONVOID_METHODS);
				continue;
			}
			
			if (method.getParameterTypes().length > 0) {
				RTTLogging.warn(ONLY_PARAMETERLESS_METHODS);
				continue;
			}
			
			element = new Element();
			element.setAddress(parentNode.getAddress() + "." + methodAddress);
			element.setGeneratorName(method.getName());
			element.setGeneratorType(Type.METHOD);
			element.setInformational(isInformational(parentNode, method));
			
			try {
				method.setAccessible(true);
				resultList.add(handleResult(method.invoke(nodeObject), element));
				methodAddress++;				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				RTTLogging.throwException(
						new RuntimeException("Could not invoke method.", e));
			}
		}
		
		return resultList;
	}
	
	private static boolean isInformational(Element parent, AnnotatedElement element) {
		return parent.isInformational() || element.isAnnotationPresent(INFORMATIONAL_ANNOTATION);
	}
	
	private Element handleResult(final Object object, Element prototype) throws InvocationTargetException {
		if (object != null) {
			if (object.getClass().isArray()) {				
				return handleArray(object, prototype);
			}
			
			if (object instanceof Iterable<?>) {
				return handleIterable((Iterable<?>) object, prototype);
			}
		}	
		
		return handleObject(object, prototype);
	}
	
	private Element handleArray(final Object array, Element prototype) throws InvocationTargetException {
		Node arrayNode = GeneratorUtil.createNode(array, prototype);
		
		Element element = null;		
		for (int index = 0; index < Array.getLength(array); index++) {
			element = GeneratorUtil.createChildElement(arrayNode, index);			
			arrayNode.getElement().add(handleResult(Array.get(array, index), element));
		}
		
		return arrayNode;
	}

	private Element handleIterable(final Iterable<?> iterable, Element prototype) throws InvocationTargetException {
		Node iterableNode = GeneratorUtil.createNode(iterable, prototype);
		
		int index = 0;
		Element element = null;
		
		for (Object object : iterable) {
			element = GeneratorUtil.createChildElement(iterableNode, index);
			iterableNode.getElement().add(handleResult(object, element));
			index++;
		}
		
		return iterableNode;
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
	 * <p>Tries to locate the {@link Executor} via the class loader</p>
	 * 
	 * <p>Instantiate the executor through
	 * {@link Executor#initialize(Input, List)} before use!</p>
	 * 
	 * @param config the {@link Configuration}
	 * @param baseDir the base directory for searching
	 * @return a {@link Executor} or null (if config is empty)
	 * @throws MalformedURLException mainly exceptions during class loading
	 * @throws ClassNotFoundException mainly exceptions during class loading             
	 */
	public static Executor locateInitialNode(Configuration config,
			String baseDir) throws MalformedURLException, ClassNotFoundException {
		
		String initialNode = config.getParserClass();
		if (initialNode == null || initialNode.trim().isEmpty()) {
			throw new IllegalStateException("The given configuration contains no initial node.");
		}
		
		RTTLogging.info("Initial Node: " + initialNode);
		ExecutorLoader loader = new ExecutorLoader(config.getClasspath());		
		return new Executor(loader.resolveClass(initialNode.trim()));
	}

}
