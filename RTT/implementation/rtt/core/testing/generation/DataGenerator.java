package rtt.core.testing.generation;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rtt.annotations.processing.AnnotationProcessor;
import rtt.annotations.processing.ValueMember;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Type;
import rtt.core.utils.ExecutorLoader;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private Map<Object, String> objectAddresses;
	
	private DataGenerator() {
		objectAddresses = new Hashtable<>();
	}
	
	private Element handleObject(final Object object, 
				Element prototype) throws Exception {
		
		if (AnnotationProcessor.isNode(object)) {
			return handleNode(object, prototype);
		}
		
		return GeneratorUtil.createValue(object, prototype);
	}
	
	private Element handleNode(final Object object, 
				Element prototype) throws Exception {
		
		Element result = null;
		
		String address = null;
		if (objectAddresses.containsKey(object)) {
			address = objectAddresses.get(object);
		}		
		
		if (address != null && !address.equals("")) {
			result = GeneratorUtil.createReference(address, prototype);
		} else {
			Node resultNode = GeneratorUtil.createNode(object, prototype);
			objectAddresses.put(object, resultNode.getAddress());
			
			int childAddress = 1;
			
			Set<ValueMember<?>> annotatedElements = 
					AnnotationProcessor.getValueMembers(object.getClass());
			
			Element element = null;
			for (ValueMember<?> annotatedElement : annotatedElements) {
				element = new Element();
				element.setAddress(resultNode.getAddress() + "." + childAddress);
				element.setName(annotatedElement.getName());
				element.setElementType(annotatedElement.getType());
				element.setInformational(resultNode.isInformational() 
						|| annotatedElement.isInformational());
				
				try {
					resultNode.getElements().add(handleResult(
							annotatedElement.getResult(object), element));
					childAddress++;
				} catch (Exception e) {
					RTTLogging.throwException(e);
				}				
			}
			
			result = resultNode;
		}
		
		return result;
	}
	
	private Element handleResult(final Object object, 
				Element prototype) throws Exception {
		
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
	
	private Element handleArray(final Object array, 
				Element prototype) throws Exception {
		
		Node arrayNode = GeneratorUtil.createNode(array, prototype);
		
		Element element = null;		
		for (int index = 0; index < Array.getLength(array); index++) {
			element = GeneratorUtil.createChildElement(arrayNode, index);			
			arrayNode.getElements().add(handleResult(Array.get(array, index), element));
		}
		
		return arrayNode;
	}

	private Element handleIterable(final Iterable<?> iterable, 
				Element prototype) throws Exception {
		
		Node iterableNode = GeneratorUtil.createNode(iterable, prototype);
		
		int index = 0;
		Element element = null;
		
		for (Object object : iterable) {
			element = GeneratorUtil.createChildElement(iterableNode, index);
			iterableNode.getElements().add(handleResult(object, element));
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
		initPrototype.setName("Initial Node");
		initPrototype.setElementType(Type.OBJECT);
		
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
		
		String initialNode = config.getInitialNode();
		if (initialNode == null || initialNode.trim().isEmpty()) {
			throw new IllegalStateException("The given configuration contains no initial node.");
		}
		
		RTTLogging.info("Initial Node: " + initialNode);
		ExecutorLoader loader = new ExecutorLoader(config.getClasspath());		
		return new Executor(loader.resolveClass(initialNode.trim()));
	}

}
