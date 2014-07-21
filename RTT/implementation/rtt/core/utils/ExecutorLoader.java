package rtt.core.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.configuration.Classpath;

public class ExecutorLoader implements AutoCloseable {
	
	private URLClassLoader classLoader;

	public Class<?> resolveClass(String className) throws ClassNotFoundException {
		if (classLoader == null) {
			throw new IllegalStateException("Class loader is not initialized.");
		}
		
		return classLoader.loadClass(className);		
	}

	public ExecutorLoader(Classpath classpath) throws MalformedURLException {
		List<URL> urls = new ArrayList<URL>();
		
		if (classpath != null && !classpath.getPath().isEmpty()) {			
			List<String> cpEntries = classpath.getPath();			
			
			for (String entry : cpEntries) {
				Path path = resolveEntry(entry);
				if (Files.exists(path)) {
					urls.add(path.toUri().toURL());
				} else {
					RTTLogging.warn("The class path '" + path + "' does not exists.");
				}
			} 
		}
		
		classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), 
				Thread.currentThread().getContextClassLoader());
	}

	private Path resolveEntry(String entry) {
		return Paths.get(entry).toAbsolutePath().normalize();
	}

	@Override
	public void close() throws Exception {
		if (classLoader != null) {
			classLoader.close();
			classLoader = null;
		}
	}

}
