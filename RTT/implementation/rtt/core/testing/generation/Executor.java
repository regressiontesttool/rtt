/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core.testing.generation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.input.Input;
import rtt.core.utils.RTTLogging;


/**
 * 
 * @author Peter Mucha
 * 
 */
public abstract class Executor {
	
	protected Class<?> executorClass = null;
	protected AnnotationProcessor processor = null;
	protected String[] params = null;
	protected List<Class<? extends Throwable>> acceptedList;

	public Executor(String className, Classpath cp, String baseDir) throws Exception {
		executorClass = loadClass(className, cp, baseDir);
		processor = new AnnotationProcessor(executorClass);
		
		acceptedList = new ArrayList<Class<? extends Throwable>>();
	}
	 
	public abstract void initialize(Input input, List<String> params) throws Throwable;

	public Class<?> loadClass(String className, Classpath cp, String baseDir)
			throws Exception {
		String path = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
		if (!(cp == null || cp.getPath().size() == 0)) {
			URL[] urls = new URL[cp.getPath().size()];
			int idx = 0;
			for (String pathEntry : cp.getPath()) {
				File f = new File(pathEntry); // test, if absolut
				if (!f.exists()) {
					f = new File(path + File.separator + pathEntry);					
					if (!f.exists()) {
						RTTLogging.warn("Classpath does not exist: "
								+ pathEntry);
						continue;
					}
				}
				urls[idx++] = (f.toURI().toURL());
			}
			
//			System.out.println(Thread.currentThread().getContextClassLoader());

			URLClassLoader ucl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
			try {
				return ucl.loadClass(className);
			} catch (ClassNotFoundException exception) {
				RTTLogging.error("Executing class not found.", exception);
			}
			

		}

		return Class.forName(className);
	}
	
	public void setParams(List<String> params) {
		if (params != null) {
			this.params = params.toArray(new String[params.size()]);
		}
	}
	
	public void setAcceptedExceptions(Class<? extends Throwable>[] acceptedExceptions) {
		acceptedList.clear();
		for (Class<? extends Throwable> throwable : acceptedExceptions) {
			acceptedList.add(throwable);
		}
	}
	
	public boolean isAcceptedException(Throwable throwable) {
		return acceptedList.contains(throwable.getClass());
	}

	protected <A extends Annotation> Object initializeClass(Input input, Class<A> annotation) throws Exception {
		
		InputStream inputStream = new ByteArrayInputStream(input.getValue().getBytes());

		// try to find constructors with the initialize annotation
		List<Constructor<?>> cs = processor.getConstructorsWithAnnotation(annotation);

		if (cs.size() > 0) {
			// one ore more constructors found
			
			if (cs.size() > 1) {
				throw new Exception(
						"More than one constructor is annotated with "
						+ annotation.toString()
				);
			}

			if (params != null) {
				return cs.get(0).newInstance(inputStream, params);
			} else {
				return cs.get(0).newInstance(inputStream);
			}
		}
		
		// TODO annotation verarbeitung der methode überdenken
		List<Method> methodList = processor.getMethodsWithAnnotation(annotation);
		if (methodList.size() > 0) {
			if (methodList.size() > 1) {
				throw new Exception(
						"More than one method is annotated with "
						+ annotation.toString()
				);
			}			
			
			Object executorObject = null;
			try {
				executorObject = processor.getObjectInstance();
			} catch (Exception ex) {
				executorObject = executorClass.getConstructor(InputStream.class).newInstance(inputStream);
			}
			
			if (params != null) {
				methodList.get(0).invoke(executorObject, inputStream, params);
			} else {
				methodList.get(0).invoke(executorObject, inputStream);
			}
			
			return executorObject;
		}		
		
		// try to invoke a constructor without a annotation
		Constructor<?> constructor = null;
		if (params != null)  {
			constructor = executorClass.getConstructor(InputStream.class, String[].class);
			return constructor.newInstance(inputStream, params);
		} else {
			constructor = executorClass.getConstructor(InputStream.class);
			return constructor.newInstance(inputStream);
		}
	}
}
