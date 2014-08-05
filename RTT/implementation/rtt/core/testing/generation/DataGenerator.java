package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Node.Address;
import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Reference;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.utils.ExecutorLoader;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private static final Class<? extends Annotation> NODE_ANNOTATION = rtt.annotations.Node.class;
	private static final Class<? extends Annotation> COMPARE_ANNOTATION = Compare.class;
	private static final Class<? extends Annotation> INFORMATIONAL_ANNOTATION = Informational.class;
	private static final Class<? extends Annotation> ADDRESS_ANNOTATION = Address.class;
	
	private static final String NO_AST_METHOD = "Could not find a method annotated with @Parser.AST";
	private static final String ONLY_NONVOID_METHODS = "Only methods with a non-void return type allowed.";
	private static final String ONLY_PARAMETERLESS_METHODS = "Only methods without parameters allowed.";
	
	private DataGenerator() {}
	
	public static void createOutput(Executor executor, Output outputData) throws InvocationTargetException {
		
		Method astMethod = executor.getASTMethod();
		if (astMethod == null) {
			RTTLogging.throwException(new RuntimeException(NO_AST_METHOD));
		}
		
		Element astElement = new Element();
		astElement.setAddress("1");
		astElement.setName(astMethod.getName());
		astElement.setType(Type.METHOD);
		
		Object astMethodResult = null;
		try {
			astMethod.setAccessible(true);
			astMethodResult = astMethod.invoke(executor.getExecutor());
		} catch (IllegalAccessException | IllegalArgumentException exception) {
			throw new RuntimeException("Could not invoke method. ", exception);
		}	
		
		DataGenerator generator = new DataGenerator();
		outputData.setAST(generator.handleResult(astMethodResult, astElement));
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
		Node node = createNode(array, prototype);
		
		Element element = null;		
		for (int index = 0; index < Array.getLength(array); index++) {
			element = createElement(node, index);			
			node.getElement().add(handleResult(Array.get(array, index), element));
		}
		
		return node;
	}

	private Element handleIterable(final Iterable<?> iterable, Element prototype) throws InvocationTargetException {
		Node node = createNode(iterable, prototype);
		
		int index = 0;
		Element element = null;
		
		for (Object object : iterable) {
			element = createElement(node, index);
			node.getElement().add(handleResult(object, element));
			index++;
		}
		
		return node;
	}
	
	private Node createNode(final Object object, Element prototype) {
		Class<?> objectType = object.getClass();
		
		Node node = new Node();
		node.setAddress(prototype.getAddress());
		node.setName(prototype.getName());
		node.setType(prototype.getType());
		node.setInformational(prototype.isInformational());
		
		node.setFullName(objectType.getName());
		node.setSimpleName(objectType.getSimpleName());
		
		return node;
	}
	
	private Element createElement(final Element prototype, final int index) {
		int childAddress = index + 1;
		
		Element itemPrototype = new Element();
		itemPrototype.setAddress(prototype.getAddress() + "." + childAddress);
		itemPrototype.setName(prototype.getName() + "[" + index + "]");
		itemPrototype.setType(Type.ELEMENT);	
		itemPrototype.setInformational(prototype.isInformational());	
		
		return itemPrototype;
	}

	private Element handleObject(final Object object, Element prototype) throws InvocationTargetException {
		if (object != null && hasNodeAnnotation(object)) {
			return handleNode(object, prototype);
		}
		
		return createValue(object, prototype);
	}
	
	private boolean hasNodeAnnotation(final Object object) {
		if (object != null) {
			return AnnotationProcessor.hasAnnotation(
					object.getClass(), NODE_ANNOTATION);
		}
		
		return false;
	}

	private Value createValue(Object value, Element prototype) {
		Value valueElement = new Value();
		
		valueElement.setAddress(prototype.getAddress());
		valueElement.setName(prototype.getName());
		valueElement.setType(prototype.getType());
		valueElement.setInformational(prototype.isInformational());
		
		if (value != null) {
			valueElement.setValue(value.toString());
		}
		
		return valueElement;
	}

	private Element handleNode(final Object object, Element prototype) throws InvocationTargetException {
		
		Element result = null;
		String address = getAddress(object);
		
		if (address != null && !address.equals("")) {
			Reference reference = new Reference();
			reference.setTo(address);
			
			result = reference;		
		} else {
			Node resultNode = createNode(object, prototype);
			setAddress(object, resultNode.getAddress());
			
			resultNode.getElement().addAll(processFields(object, resultNode, COMPARE_ANNOTATION));
			resultNode.getElement().addAll(processFields(object, resultNode, INFORMATIONAL_ANNOTATION));
			
			resultNode.getElement().addAll(processMethods(object, resultNode, COMPARE_ANNOTATION));
			resultNode.getElement().addAll(processMethods(object, resultNode, INFORMATIONAL_ANNOTATION));
			
			result = resultNode;
		}
		
		return result;
	}

	private String getAddress(Object object) {
		Field addressField = getAddressField(object.getClass());		
		if (addressField != null) {
			try {
				addressField.setAccessible(true);
				return (String) addressField.get(object);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				RTTLogging.error("Can not access address field", e);
			}
		}
		
		return null;
	}
	
	private void setAddress(Object object, String address) {
		Field addressField = getAddressField(object.getClass());
		if (addressField != null) {
			try {
				addressField.setAccessible(true);
				addressField.set(object, address);
			} catch (Exception e) {
				RTTLogging.error("Could not access address field", e);
			}
		}
	}
	
	private Field getAddressField(Class<?> objectType) {
		Field addressField = null;
		
		for (Field field : objectType.getDeclaredFields()) {
			if (field.isAnnotationPresent(ADDRESS_ANNOTATION) 
					&& field.getType() == String.class) {
				
				addressField = field;
				break;
			}
		}
		
		return addressField;
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
			element.setName(field.getName());
			element.setType(Type.FIELD);
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
			element.setName(method.getName());
			element.setType(Type.METHOD);
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
