package rtt.core;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;

import rtt.core.manager.Manager;
import rtt.core.utils.RTTLogging;

public class RTTApplication implements IApplication {
	
	public enum MapKeys {
		PROJECT("--project"),
		SUITE("--suite"), 
		ARCHIVE("--archive"),
		ACTION("--action"),
		TYPE("--type"), 
		CONFIG("--config");
		
		private String argumentCode;

		private MapKeys(String argumentCode) {
			this.argumentCode = argumentCode;
		}
		
		public static MapKeys getKey(String argumentCode) {
			for (MapKeys key : MapKeys.values()) {
				if (key.argumentCode.equalsIgnoreCase(argumentCode)) {
					return key;
				}			
			}
			
			return null;
		}
		
		public String getArgumentCode() {
			return argumentCode;
		}
	}
	
	class BundleClassLoader extends ClassLoader {
		private Bundle bundle;

		public BundleClassLoader(Bundle target, ClassLoader parent) {
			super(parent);
			this.bundle = target;
		}

		protected Class<?> findClass(String name) throws ClassNotFoundException {
			return bundle.loadClass(name);
		}

		protected URL findResource(String name) {
			return bundle.getResource(name);
		}

		protected Enumeration<URL> findResources(String name) throws IOException {
			return bundle.getResources(name);
		}
	}
	
	public static final String JAVA_TYPE = "java";
	public static final String PLUGIN_TYPE = "plugin";
	public static final String GENERATE = "generate";
	public static final String RUN = "run";

	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		
		if (args == null || args.length == 0 || args.length < MapKeys.values().length * 2) {
			System.err.println("RTT: Not sufficient arguments were given.");
			return IApplication.EXIT_OK;
		}
		
		RTTLogging.debug(Arrays.toString(args));
		Map<MapKeys, String> argumentMap = new TreeMap<RTTApplication.MapKeys, String>();
		
		for (int index = 0; index < args.length; index += 2) {
			String argument = args[index];
			String value = args[index + 1];
			
			boolean argumentValid = (argument != null && argument.startsWith("--"));
			boolean valueValid = (value != null && !value.isEmpty());
			
			if (argumentValid && valueValid) {
				parseArgument(argumentMap, argument, value);
			}
		}
		
		// get project name		
		String projectName = getArgument(argumentMap, MapKeys.PROJECT);
		if (projectName == null) {
			return printError(MapKeys.PROJECT);
		}
		
		// get project type
		String projectType = getArgument(argumentMap, MapKeys.TYPE);
		if (projectType == null) {
			return printError(MapKeys.TYPE);
		}
		
		// get archive file
		String fileName = getArgument(argumentMap, MapKeys.ARCHIVE);
		if (fileName == null) {
			return printError(MapKeys.ARCHIVE);
		}

		IPath archivePath = new Path(fileName);
		if (archivePath == null || archivePath.isEmpty()) {
			System.err.println("The archive argument doesn't provide a proper file location.");
			return IApplication.EXIT_OK;
		}
		
		// get suite name
		String suiteName = getArgument(argumentMap, MapKeys.SUITE);
		if (suiteName == null) {
			return printError(MapKeys.SUITE);
		}
		
		// get config name
		String configName = getArgument(argumentMap, MapKeys.CONFIG);
		if (configName == null) {
			return printError(MapKeys.CONFIG);
		}
		
		// get action type
		String actionType = getArgument(argumentMap, MapKeys.ACTION);
		if (actionType == null) {
			return printError(MapKeys.ACTION);
		}
		
		// do the work
		
		// load bundle classloader for plug-in projects
		ClassLoader classLoader = this.getClass().getClassLoader();
		if (projectType.equals(PLUGIN_TYPE)) {
			Bundle bundle = Platform.getBundle(projectName);
			if (bundle == null) {
				System.err.println("Could not load bundle '" + projectName + "'.");
				return IApplication.EXIT_OK;
			}
			
			classLoader = new BundleClassLoader(bundle, classLoader);
		}
		
		// open manager
		Manager manager = new Manager(archivePath.toFile(), true, classLoader);
		manager.loadArchive(archivePath.toFile(), configName);
		
		if (actionType.equalsIgnoreCase(GENERATE)) {
			manager.generateTests(suiteName);
			manager.saveArchive(archivePath.toFile());
		} else if (actionType.equalsIgnoreCase(RUN)) {
			manager.runTests(suiteName, true);
			manager.saveArchive(archivePath.toFile());
		} else {
			System.err.println("No action argument was given. Add '--action [generate | run]' to run configuration.");
		}
		
		return IApplication.EXIT_OK;
	}
	
	private String getArgument(Map<MapKeys, String> argumentMap, MapKeys key) {
		if (argumentMap.containsKey(key)) {
			String value = argumentMap.get(key);
			if (value != null && !value.equals("")) {
				return value;
			}
		}
		
		return null;
	}
	
	private Integer printError(MapKeys key) {
		System.err.println("No " + key.toString() + " argument was given. Add '" + key.getArgumentCode() + "' to run configuration");
		return IApplication.EXIT_OK;
	}

	private void parseArgument(Map<MapKeys, String> argumentMap, String opCode, String argument) {
		MapKeys key = MapKeys.getKey(opCode);
		if (key != null) {
			argumentMap.put(key, argument);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
