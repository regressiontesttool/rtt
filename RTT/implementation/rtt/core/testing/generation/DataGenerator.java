package rtt.core.testing.generation;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rtt.annotations.processing.AnnotationProcessor;
import rtt.annotations.processing.ValueMember;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Output;
import rtt.core.utils.ExecutorLoader;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private Map<Object, String> objectAddresses;
	
	private DataGenerator() {
		objectAddresses = new Hashtable<>();		
	}
	
	private Element createElement(String address, String name, 
			GeneratorType generatedBy, boolean informational) {
		
		Element element = new Element();
		element.setAddress(address);
		element.setName(name);
		element.setInformational(informational);
		element.setGeneratedBy(generatedBy);
		
		return element;
	}
	
	private void handleResult(final Object object, Element element) 
			throws ReflectiveOperationException {
		
		if (object != null) {
			if (objectAddresses.containsKey(object)) {
				element.setElementType(ElementType.REFERENCE);
				element.setValue(objectAddresses.get(object));
			} else {
				Class<?> objectType = object.getClass();
				
				if (AnnotationProcessor.isNode(object)) {
					objectAddresses.put(object, element.getAddress());
					
					element.setElementType(ElementType.NODE);
					element.setValue(objectType.getName());

					handleNode(object, element);
				}
				
				if (element.getElements().isEmpty()) {
					if (objectType.isArray()) {
						element.setElementType(ElementType.NODE);
						element.setValue(objectType.getSimpleName());						
						handleArray(object, element);
					} else if (object instanceof Iterable<?>) {
						element.setElementType(ElementType.NODE);
						element.setValue(objectType.getName());						
						handleIterable((Iterable<?>) object, element);
					} else if (object instanceof Map<?, ?>) {
						element.setElementType(ElementType.NODE);
						element.setValue(objectType.getName());
						element.setGeneratedBy(GeneratorType.MAP);
						
						handleMap((Map<?, ?>) object, element);
					}						
				}
				
				if (element.getValue() == null) {
					element.setElementType(ElementType.VALUE);
					element.setValue(object.toString());
				}				
			}
		}
	}
	
	private void handleNode(final Object object, Element element) 
			throws ReflectiveOperationException {	
		
		Set<ValueMember<?>> annotatedElements = 
				AnnotationProcessor.getValueMembers(object.getClass());
		
		Element childElement = null;
		String address = null;
		int childAddress = 1;
		
		for (ValueMember<?> annotatedElement : annotatedElements) {
			address = element.getAddress() + "." + childAddress;			
			childElement = createElement(address, 
					annotatedElement.getName(), 
					annotatedElement.getType(), 
					element.isInformational() || annotatedElement.isInformational());
			
			element.getElements().add(childElement);
			handleResult(annotatedElement.getResult(object), childElement);

			childAddress++;			
		}
	}

	private void handleArray(final Object array, Element element) 
			throws ReflectiveOperationException {
		
		Element childElement = null;
		String address = null;
		String name = null;
		
		for (int index = 1; index - 1 < Array.getLength(array); index++) {
			address = element.getAddress() + "." + index;
			name = element.getName() + "[" + index + "]";
			
			childElement = createElement(address, name, 
					GeneratorType.ARRAY, element.isInformational());
			element.getElements().add(childElement);
			
			handleResult(Array.get(array, index - 1), childElement);
		}
	}

	private void handleIterable(final Iterable<?> iterable, Element element) 
			throws ReflectiveOperationException {

		int index = 1;
		
		Element childElement = null;
		String address = null;
		String name = null;				
		
		for (Object object : iterable) {
			address = element.getAddress() + "." + index;
			name = element.getName() + "[" + index + "]";
			
			childElement = createElement(address, name, 
					GeneratorType.ITERABLE, element.isInformational());			
			element.getElements().add(childElement);
			
			handleResult(object, childElement);			
			index++;			
		}
	}
	
	private void handleMap(final Map<?, ?> map, Element element) {
		
	}
	
	public static Output generateOutput(Input input, List<String> params, 
			Executor executor) throws Throwable {
		
		if (input == null || params == null || executor == null) {
			throw new IllegalArgumentException("One argument was null.");
		}
		
		Output outputData = new Output();
		
		Object initObject = null;
		try {			
			RTTLogging.debug("Initial object type: " + 
					executor.getInitialObjectType().getSimpleName());				
			initObject = executor.initialize(input, params);			
		} catch (ReflectiveOperationException exception) {
			Throwable cause = exception.getCause();
			if (!executor.isAcceptedException(cause)) {
				throw cause;
			} else {
				throw new UnsupportedOperationException(
						"Accepted exception are currently not supported.", cause);
			}
		}
		
		RTTLogging.debug("Generating output data ...");
		DataGenerator generator = new DataGenerator();
		
		Element initElement = generator.createElement(
				"1", "Initial Node", GeneratorType.OBJECT, false);
		try {
			generator.handleResult(initObject, initElement);
		} catch (ReflectiveOperationException exception) {
			Throwable cause = exception.getCause();
			if (!executor.isAcceptedException(cause)) {
				throw cause;
			} else {
				throw new UnsupportedOperationException(
						"Accepted exception are currently not supported.", cause);
			}
		}	
		
		outputData.setInitialElement(initElement);		

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
		ExecutorLoader loader = new ExecutorLoader(config.getClasspath(), baseDir);		
		return new Executor(loader.resolveClass(initialNode.trim()));
	}

}
