package rtt.core.testing.generation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Type;
import rtt.core.utils.AnnotationUtil;
import rtt.core.utils.ExecutorLoader;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private static final String ONLY_NONVOID_METHODS = "Only methods with a non-void return type allowed.";
	private static final String ONLY_PARAMETERLESS_METHODS = "Only methods without parameters allowed.";
	
	private DataGenerator() {}
	
	private Element handleObject(final Object object, Element prototype) throws InvocationTargetException {
		if (AnnotationUtil.isNode(object)) {
			return handleNode(object, prototype);
		}
		
		return GeneratorUtil.createValue(object, prototype);
	}
	
	private Element handleNode(final Object object, Element prototype) throws InvocationTargetException {
		
		Element result = null;
		String address = GeneratorUtil.getObjectAddress(object);
		
		if (address != null && !address.equals("")) {
			result = GeneratorUtil.createReference(address, prototype);
		} else {
			Node resultNode = GeneratorUtil.createNode(object, prototype);
			GeneratorUtil.setObjectAddress(object, resultNode.getAddress());
			
			resultNode.getElement().addAll(processFields(object, resultNode, AnnotationUtil.getCompareFields(object)));
			resultNode.getElement().addAll(processFields(object, resultNode, AnnotationUtil.getInformationalFields(object)));
			
			resultNode.getElement().addAll(processMethods(object, resultNode, AnnotationUtil.getCompareMethods(object)));
			resultNode.getElement().addAll(processMethods(object, resultNode, AnnotationUtil.getInformationalMethods(object)));
			
			result = resultNode;
		}
		
		return result;
	}
	
	private List<Element> processFields(final Object nodeObject, final Node parentNode, 
			List<Field> annotatedFields) throws InvocationTargetException {
		
		List<Element> resultList = new ArrayList<>();
		
		int fieldAddress = parentNode.getElement().size() + 1;
		Element element = null;
		
		for (Field field : annotatedFields) {
			element = new Element();
			element.setAddress(parentNode.getAddress() + "." + fieldAddress);
			element.setGeneratorName(field.getName());
			element.setGeneratorType(Type.FIELD);
			element.setInformational(parentNode.isInformational() 
					|| AnnotationUtil.isInformational(field));
			
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
			List<Method> annotatedMethods) throws InvocationTargetException {
		
		List<Element> resultList = new ArrayList<>();
		
		Element element = null;
		int methodAddress = parentNode.getElement().size() + 1;
		
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

			element.setInformational(parentNode.isInformational() 
					|| AnnotationUtil.isInformational(method));
			
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
		if (input == null || params == null || executor == null) {
			throw new IllegalArgumentException("One argument was null.");
		}
		
		Output outputData = new Output();
		
		Element initPrototype = new Element();
		initPrototype.setAddress("1");
		initPrototype.setGeneratorName("Initial Node");
		initPrototype.setGeneratorType(Type.OBJECT);
		
		Object initObject = null;			
		try {
			RTTLogging.debug("Initial object type: " + 
					executor.getInitialObjectType().getSimpleName());				
			initObject = executor.initialize(input, params);
			
		} catch (InvocationTargetException invocationException) {
			Throwable cause = invocationException.getCause();
			if (executor.isAcceptedException(cause)) {
				throw new UnsupportedOperationException(
						"Accepted exception are currently not supported.", cause);
			} else {
				throw cause;
			}
		}
		
		RTTLogging.debug("Generating output data ...");
		DataGenerator generator = new DataGenerator();
		outputData.setInitialElement(generator.handleObject(initObject, initPrototype));

		return outputData;
	}

	/**
	 * <p>Tries to locate the {@link Executor} via the class loader</p>
	 * 
	 * <p>Instantiate the executor through
	 * {@link Executor#generateInitialObject(Input, List)} before use!</p>
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
