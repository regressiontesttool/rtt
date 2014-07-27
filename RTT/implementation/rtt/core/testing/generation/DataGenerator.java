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
import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
	private static final Class<? extends Annotation> NODE_ANNOTATION = rtt.annotations.Node.class;
	private static final Class<? extends Annotation> COMPARE_ANNOTATION = Compare.class;
	private static final Class<? extends Annotation> INFORMATIONAL_ANNOTATION = Informational.class;
	
	private static final String NO_AST_METHOD = "Could not find a method annotated with @Parser.AST";
	
	private Executor executor;	

	protected DataGenerator(Executor parser) {
		this.executor = parser;
	}
	
	private Output createOutput() throws InvocationTargetException {
		
		Output outputData = new Output();
		
		Method astMethod = executor.getASTMethod();
		if (astMethod == null) {
			RTTLogging.warn(NO_AST_METHOD);
			return outputData;
		}		
		
		Object astMethodResult = null;
		try {
			astMethod.setAccessible(true);
			astMethodResult = astMethod.invoke(executor.getExecutor());
		} catch (IllegalAccessException | IllegalArgumentException exception) {
			throw new RuntimeException("Could not invoke method. ", exception);
		}
		
		if (astMethodResult != null) {
			Node astNode = new Node();
			astNode.setGeneratorName(astMethod.getName());
			astNode.setGeneratorType(GeneratorType.METHOD);
			
			outputData.getNodes().addAll(handleObject(astMethodResult, astNode));
		}
		
		return outputData;
	}
	
	private List<Node> handleObject(final Object currentObject, final Node templateNode) throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		if (currentObject instanceof Object[]) {
			resultList.addAll(handleArray((Object[]) currentObject, templateNode));
		} else if (currentObject instanceof Iterable<?>) {
			resultList.addAll(handleIterable((Iterable<?>) currentObject, templateNode));
		} else {
			resultList.add(createNode(currentObject, templateNode));
		}
		
		return resultList;
	}
	
	private List<Node> handleArray(final Object[] currentObject, final Node templateNode) throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(handleObject(item, templateNode));
		}
		
		return resultList;
	}
	
	private List<Node> handleIterable(final Iterable<?> currentObject, final Node templateNode) throws InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(handleObject(item, templateNode));
		}
		
		return resultList;
	}
	
	private Node createNode(final Object currentObject, final Node templateNode) throws InvocationTargetException {
		
		if (currentObject == null) {
			templateNode.setIsNull(true);
			return templateNode;
		}
		
		if (AnnotationProcessor.hasAnnotation(currentObject.getClass(), NODE_ANNOTATION)) {
			return createClassNode(currentObject, templateNode);
		} 

		return createValueNode(currentObject, templateNode);
	}

	private Node createClassNode(final Object currentObject, final Node parentNode) throws InvocationTargetException {
		
		Class<?> objectType = currentObject.getClass();		
		ClassNode resultNode = new ClassNode();
		
		resultNode.setGeneratorName(parentNode.getGeneratorName());
		resultNode.setGeneratorType(parentNode.getGeneratorType());
		resultNode.setInformational(parentNode.isInformational());
		
		resultNode.setFullName(objectType.getName());
		resultNode.setSimpleName(objectType.getSimpleName());		
		
		resultNode.getNodes().addAll(processFields(currentObject, COMPARE_ANNOTATION, parentNode.isInformational()));
		resultNode.getNodes().addAll(processFields(currentObject, INFORMATIONAL_ANNOTATION, parentNode.isInformational()));
		
		resultNode.getNodes().addAll(processMethods(currentObject, COMPARE_ANNOTATION, parentNode.isInformational()));
		resultNode.getNodes().addAll(processMethods(currentObject, INFORMATIONAL_ANNOTATION, parentNode.isInformational()));
		
		return resultNode;
	}
	
	private List<Node> processFields(Object currentObject, Class<? extends Annotation> annotation, boolean parentIsInformational) throws InvocationTargetException {
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
			
			Node fieldNode = new Node();
			fieldNode.setGeneratorName(fieldName);
			fieldNode.setGeneratorType(GeneratorType.FIELD);
			fieldNode.setInformational(isInformational(parentIsInformational, field));
			
			resultList.addAll(handleObject(fieldResult, fieldNode));
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
			
			Node methodNode = new Node();
			methodNode.setGeneratorName(methodName);
			methodNode.setGeneratorType(GeneratorType.METHOD);
			methodNode.setInformational(isInformational(parentIsInformational, method));
			
			resultList.addAll(handleObject(methodResult, methodNode));
		}
		
		return resultList;
	}
	
	private boolean isInformational(boolean parentIsInformational, AnnotatedElement element) {
		return parentIsInformational || element.isAnnotationPresent(INFORMATIONAL_ANNOTATION);
	}

	private Node createValueNode(final Object currentObject, final Node templateNode) {
		ValueNode resultNode = new ValueNode();
		
		resultNode.setGeneratorName(templateNode.getGeneratorName());
		resultNode.setGeneratorType(templateNode.getGeneratorType());
		resultNode.setInformational(templateNode.isInformational());
		
		resultNode.setValue(currentObject.toString());	

		return resultNode;
	}
	
	public static Output generateOutput(Input input, List<String> params, Executor parser) throws InvocationTargetException {
			
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
