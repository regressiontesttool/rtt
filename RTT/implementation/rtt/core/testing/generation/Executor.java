package rtt.core.testing.generation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import rtt.annotations.Node.Initialize;
import rtt.annotations.processing2.AnnotationProcessor;
import rtt.annotations.processing2.InitialMember;
import rtt.core.archive.input.Input;
import rtt.core.exceptions.AnnotationException;
import rtt.core.utils.RTTLogging;

public class Executor {
	
	private static final String NO_INTERFACES = 
			"Interfaces are not allowed for output data generation.";
	private static final String NO_ANONYMOUS = 
			"Anonymous classes are not allowed for output generation.";
	private static final String NO_LOCALCLASS = 
			"Local classes are currently not supported for output generation.";
	private static final String NO_NONSTATIC_MEMBERCLASS = 
			"Non-static member classes are currently not supported for output generation.";
	
	private static final String NO_NODE_ANNOTATION = 
			"The given class doesn't have a @Node annotation.";	
	private static final String NO_INIT_MEMBER = 
			"Could not find a method or constructor annotated with @Node.Initialize.";
	private static final String NO_SINGLE_INIT_MEMBER = 
			"Found more than one method or constructor annotated with @Node.Initialize.";
	
	private static final String PARAMETER_COUNT_ERROR = 
			"The element which is annotated with @Node.Initialize must have $$ parameter(s).";
	private static final String NO_INPUTSTREAM_PARAMETER = 
			"The first parameter needs to be an InputStream.";
	private static final String NO_STRINGARRAY_PARAMETER = 
			"The second parameter needs to be an array of strings.";
	
	private Class<?> initialObjectType = null;
	private InitialMember<?> initialMember = null;
	private List<Class<? extends Throwable>> acceptedExceptions;
	
	private Initialize initAnnotation;	

	public Executor(Class<?> initialObjectType) {
		checkClass(initialObjectType);
		
		SortedSet<InitialMember<?>> initMembers = AnnotationProcessor.getInitMembers(initialObjectType);
		if (initMembers.size() == 0) {
			RTTLogging.throwException(new IllegalStateException(NO_INIT_MEMBER));
		}
		
		if (initMembers.size() > 1) {
			RTTLogging.throwException(new IllegalStateException(NO_SINGLE_INIT_MEMBER));
		}
		
		this.initialObjectType = initialObjectType;
		initialMember = initMembers.first();
		initAnnotation = initialMember.getAnnotation();
		checkParameters(initialMember.getParameterTypes(), initAnnotation.withParams());
		
		acceptedExceptions = new ArrayList<>();
		for (Class<? extends Throwable> throwable : initAnnotation.acceptedExceptions()) {
			acceptedExceptions.add(throwable);
		}		
	}

	private void checkClass(Class<?> initialNodeClass) {
		if (initialNodeClass.isInterface()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_INTERFACES));
		}
		
		if (initialNodeClass.isAnonymousClass()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_ANONYMOUS));
		}
		
		if (initialNodeClass.isLocalClass()) {
			RTTLogging.throwException(new IllegalArgumentException(NO_LOCALCLASS));
		}
		
		if (initialNodeClass.isMemberClass() && !Modifier.isStatic(initialNodeClass.getModifiers())) {
			RTTLogging.throwException(new IllegalArgumentException(NO_NONSTATIC_MEMBERCLASS));
		}
		
		if (!AnnotationProcessor.isNode(initialNodeClass)) {
			RTTLogging.throwException(new AnnotationException(NO_NODE_ANNOTATION));
		}
	}
	
	private void checkParameters(Class<?>[] parameterTypes, boolean withParams) {
		int paramSize = withParams ? 2 : 1;
		
		if (parameterTypes.length != paramSize) {
			RTTLogging.throwException(new AnnotationException(PARAMETER_COUNT_ERROR.replace("$$", "" + paramSize)));
		}
		
		if (parameterTypes[0] == null || !parameterTypes[0].equals(InputStream.class)) {
			RTTLogging.throwException(new AnnotationException(NO_INPUTSTREAM_PARAMETER));
		}
		
		if (withParams && (parameterTypes[1] == null || !parameterTypes.equals(String[].class))) {
			RTTLogging.throwException(new AnnotationException(NO_STRINGARRAY_PARAMETER));
		}
	}
	
	public Object initialize(Input input, List<String> params) throws Exception {
		Object initialObject = null;
		
		try (InputStream inputStream = new ByteArrayInputStream(input.getValue().getBytes())) {
			initialObject = initialMember.getResult(inputStream, params);
		} catch (IOException e) {
			RTTLogging.error("Could not access input stream.", e);
		} catch (Exception e) {
			RTTLogging.throwException(e);
		}
		
		return initialObject;
	}
	
	public boolean isAcceptedException(Throwable exception) {
		return acceptedExceptions.contains(exception.getClass());
	}
	
	public Class<?> getInitialObjectType() {
		return initialObjectType;
	}
}
