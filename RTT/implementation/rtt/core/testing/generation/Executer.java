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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Path;
import rtt.core.archive.input.Input;
import rtt.core.utils.DebugLog;


/**
 * 
 * @author Peter Mucha
 * 
 */
public abstract class Executer {

	public Class<?> loadClass(String className, Classpath cp, String baseDir)
			throws Exception {
		String path = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
		if (!(cp == null || cp.getPath().size() == 0)) {
			URL[] urls = new URL[cp.getPath().size()];
			int idx = 0;
			for (Path p : cp.getPath()) {
				File f = new File(p.getValue()); // test, if absolut
				if (!f.exists()) {
					f = new File(path + File.separator + p.getValue());					
					if (!f.exists()) {
						DebugLog.log("Classpath does not exist: "
								+ p.getValue());
						continue;
					}
				}
				urls[idx++] = (f.toURI().toURL());
			}

			URLClassLoader ucl = new URLClassLoader(urls, this.getClass()
					.getClassLoader());
			try {
				// cpModifier.addPath(urls);
				// Class c = Class.forName(className);
				Class<?> c = ucl.loadClass(className);
				return c;
			} catch (Exception c) {
				throw c;
			}

		}

		return Class.forName(className);
	}

	public abstract void loadInput(Input i) throws Exception;

	protected Object loadInputImpl(Input i, Class inputAnnotation,
			AnnotationProcessor proc) throws Exception {
		Object r;
		InputStream in = new ByteArrayInputStream(i.getValue().getBytes());

		List<Constructor> cs = proc
				.getConstructorsWithAnnotation(inputAnnotation);

		if (cs.size() > 0) {
			
			if (cs.size() > 1) {
				throw new Exception(
						"More than one Constructors are annotated with "
						+ inputAnnotation.toString()
				);
			}

			r = cs.get(0).newInstance(in);
		} else {

			try {
				Method input = getSingleMethod(inputAnnotation, proc);
				r = proc.getNewInstance();
				input.invoke(r, in);
			} catch (Exception e) {
				// no method to initialize, try to invoke Constructor
				try {
					r = proc.getNewInstance(InputStream.class, in);
				} catch (Exception e2) {
					System.err.println("Cant find method to initialize");
					return null;
				}
			}
		}
		return r;
	}

	protected Method getSingleMethod(Class annotationClass,
			AnnotationProcessor proc) throws Exception {
		List<Method> methods = proc.getMethodsWithAnnotation(annotationClass);

		if (methods.size() == 1) {
			return methods.get(0);
		} else {
			// size == 0
			throw new Exception("Cant specify a method annotated with "
					+ annotationClass.toString());
		}

	}

}
