package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private static final Class<? extends Annotation> NODE_ANNOTATION = rtt.annotations.Node.class;
	private static final Class<? extends Annotation> COMPARE_ANNOTATION = Compare.class;
	private static final Class<? extends Annotation> INFORMATIONAL_ANNOTATION = Informational.class;
	
	private static final String NO_AST_METHOD = "Could not find a method annotated with @Parser.AST";
	private static final String NODE_NULL = "Resulting node was null.";	
	
	private Executor executor;	

	protected DataGenerator(Executor parser) {
		this.executor = parser;
	}
	
	private Output createOutput() 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		
		Method astMethod = executor.getASTMethod();
		if (astMethod == null) {
			throw new NoSuchMethodException(NO_AST_METHOD);
		}
		
		Output outputData = new Output();
		
		astMethod.setAccessible(true);
		String astMethodName = astMethod.getName();
		Object astMethodResult = astMethod.invoke(executor.getExecutor());
		if (astMethodResult != null) {
			outputData.getNodes().addAll(createNodes(astMethodResult, astMethodName, false));
		}
		
		return outputData;
	}
	
	private List<Node> createNodes(final Object currentObject, 
			final String generatedBy, final boolean isInformational) throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		if (currentObject instanceof Object[]) {
			resultList.addAll(createNodes((Object[]) currentObject, generatedBy, isInformational));
		} else if (currentObject instanceof Iterable<?>) {
			resultList.addAll(createNodes((Iterable<?>) currentObject, generatedBy, isInformational));
		} else {
			resultList.add(createNode(currentObject, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private List<Node> createNodes(final Object[] currentObject, 
			final String generatedBy, final boolean isInformational) throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(createNodes(item, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private List<Node> createNodes(final Iterable<?> currentObject, 
			final String generatedBy, final boolean isInformational) throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(createNodes(item, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private Node createNode(final Object currentObject, 
			final String generatedBy, boolean isInformational) throws InvocationTargetException {
		
		Node resultNode = null;
		
		if (currentObject == null) {
			resultNode = new Node();
			resultNode.setIsNull(true);
			resultNode.setGeneratorName(generatedBy);
		} else {
			if (AnnotationProcessor.hasAnnotation(currentObject.getClass(), NODE_ANNOTATION)) {
				resultNode = createClassNode(currentObject, generatedBy, isInformational);
			} else {
				resultNode = createValueNode(currentObject, generatedBy, isInformational);
			}
		}
		
		if (resultNode == null) {
			throw new IllegalStateException(NODE_NULL);
		}		
		
		return resultNode;
	}

	private Node createClassNode(final Object currentObject, 
			final String generatedBy, final boolean isInformational) throws InvocationTargetException {
		
		Class<?> objectType = currentObject.getClass();		
		ClassNode resultNode = new ClassNode();
		
		resultNode.setGeneratorName(generatedBy);
		resultNode.setFullName(objectType.getName());
		resultNode.setSimpleName(objectType.getSimpleName());
		resultNode.setInformational(isInformational);
		
		resultNode.getNodes().addAll(processFields(currentObject, COMPARE_ANNOTATION, isInformational));
		resultNode.getNodes().addAll(processFields(currentObject, INFORMATIONAL_ANNOTATION, isInformational));
		
		resultNode.getNodes().addAll(processMethods(currentObject, COMPARE_ANNOTATION, isInformational));
		resultNode.getNodes().addAll(processMethods(currentObject, INFORMATIONAL_ANNOTATION, isInformational));
		
		return resultNode;
	}
	
	private List<Node> processFields(Object currentObject, Class<? extends Annotation> annotation, boolean isInformational) throws InvocationTargetException {
		List<Node> resultList = new ArrayList<>();
		List<Field> annotatedFields = AnnotationProcessor.getFields(currentObject.getClass(), annotation);
		
		for (Field field : annotatedFields) {
			field.setAccessible(true);
			
			String fieldName;
			Object fieldResult;
			try {
				fieldName = field.getName();
				fieldResult = field.get(currentObject);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new RuntimeException("Could not access field.", e);
			}
			
			resultList.addAll(createNodes(fieldResult, fieldName, isInformational(isInformational, field, annotation)));
		}
		
		return resultList;
	}
	
	private List<Node> processMethods(final Object currentObject, Class<? extends Annotation> annotation, boolean parentIsInformational) 
			throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		List<Method> annotatedMethods = AnnotationProcessor.getMethods(currentObject.getClass(), annotation);
		
		for (Method method : annotatedMethods) {
			method.setAccessible(true);
			
			
			String methodName;
			Object methodResult;
			try {
				methodName = method.getName();
				methodResult = method.invoke(currentObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("Could not invoke method.", e);
			}
			
			resultList.addAll(createNodes(methodResult, methodName, isInformational(parentIsInformational, method, annotation)));
		}
		
		return resultList;
	}
	
	private boolean isInformational(boolean parentIsInformational, AnnotatedElement element, Class<? extends Annotation> annotation) {
		return !parentIsInformational & element.isAnnotationPresent(annotation);
	}

	private Node createValueNode(final Object currentObject, final String generatedBy, final boolean isInformational) {
		ValueNode resultNode = new ValueNode();
		resultNode.setGeneratorName(generatedBy);
		resultNode.setValue(currentObject.toString());
		resultNode.setInformational(isInformational);

		return resultNode;
	}
	
	public static Output generateOutput(Input input, List<String> params, Executor parser) throws Throwable {
			
		Output outputData = new Output();

		if (parser != null) {
			
			RTTLogging.debug("Initialize parser ...");
			parser.initialize(input, params);
			
			RTTLogging.debug("Generating output data ...");
			DataGenerator generator = new DataGenerator(parser);
			outputData = generator.createOutput();
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
		return new Executor(parserClass, config.getClasspath(), baseDir);
	}

}
