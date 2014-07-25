package rtt.core.testing.generation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.utils.RTTLogging;

public class DataGenerator {
	
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
			final String generatedBy, final boolean isInformational) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
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
			final String generatedBy, final boolean isInformational) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(createNodes(item, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private List<Node> createNodes(final Iterable<?> currentObject, 
			final String generatedBy, final boolean isInformational) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(createNodes(item, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private Node createNode(final Object currentObject, 
			final String generatedBy, boolean isInformational) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Node resultNode = null;
		
		if (currentObject == null) {
			resultNode = new Node();
			resultNode.setIsNull(true);
			resultNode.setGeneratorName(generatedBy);
		} else {
			if (executor.isNode(currentObject)) {
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
			final String generatedBy, final boolean isInformational) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Class<?> objectType = currentObject.getClass();		
		ClassNode resultNode = new ClassNode();
		
		resultNode.setGeneratorName(generatedBy);
		resultNode.setFullName(objectType.getName());
		resultNode.setSimpleName(objectType.getSimpleName());
		resultNode.setInformational(isInformational);
		
		List<Method> annotatedMethods = executor.getAnnotatedMethods(objectType);
		boolean childIsInformational = isInformational;
		
		for (Method method : annotatedMethods) {
			method.setAccessible(true);
			childIsInformational = isInformational(method, isInformational);
			
			String methodName = method.getName();
			Object methodResult = method.invoke(currentObject);
			
			resultNode.getNodes().addAll(createNodes(methodResult, methodName, childIsInformational));
		}
		
		List<Field> annotatedFields = executor.getAnnotatedFields(objectType);
		
		for (Field field : annotatedFields) {
			field.setAccessible(true);
			childIsInformational = isInformational(field, isInformational);
			
			String fieldName = field.getName();
			Object fieldResult = field.get(currentObject);
			
			resultNode.getNodes().addAll(createNodes(fieldResult, fieldName, childIsInformational));
		}
		
		return resultNode;
	}
	
	private boolean isInformational(AnnotatedElement element, boolean parentIsInformational) {
		return !parentIsInformational && executor.isInformational(element);	
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
