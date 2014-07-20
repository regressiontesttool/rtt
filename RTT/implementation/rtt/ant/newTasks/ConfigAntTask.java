package rtt.ant.newTasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import rtt.core.manager.Manager;
import rtt.core.manager.data.ConfigurationManager.ConfigStatus;

public class ConfigAntTask extends RTTAntTask {
	
	public static class ClassPathElement extends Task {
		private String path = "";
		public ClassPathElement() {}
		public String getPath() {return path;}
		public void setPath(String path) {this.path = path;}
	}

	private static final String NO_CONFIG_NAME = 
			"The name attribute must be used to specify a configuration.";

	private String name;
	private String parser = "";
	private List<ClassPathElement> classpath;
	private boolean defaultConfig = false;
	private boolean overwrite = false;
	
	public ConfigAntTask() {
		classpath = new ArrayList<ConfigAntTask.ClassPathElement>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setParser(String parser) {
		this.parser = parser;
	}
	
	public void setMakeDefault(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}
	
	public void addConfiguredClasspath(ClassPathElement classpathElement) {
		classpath.add(classpathElement);
	}
	
	@Override
	public void execute(Manager manager) {
		List<String> cpEntries = new ArrayList<String>();
//		for (ClassPathElement element : classpath) {
//			cpEntries.add(element.getPath());
//		}
		
		System.out.println(manager);
		
		manager.setConfiguration("", "", new ArrayList<String>(), false, false);
		
//		logStatus(status);		
	}

	private void logStatus(ConfigStatus status) {
		switch (status) {
		case ADDED:
			info("The config '" + name + "' has been added.");
			break;
		case SKIPPED:
			info("The config '" + name + "' has been skipped.");
			break;
		case UPDATED:
			info("The config '" + name + "' has been updated.");
			break;		
		}
	}

	@Override
	public void checkIntegrity() throws BuildException {
		if (name == null || name.equals("")) {
			error(NO_CONFIG_NAME);
			throw new BuildException(NO_CONFIG_NAME);
		}
	}

	@Override
	public boolean hasArchiveChanged() {
		// TODO Auto-generated method stub
		return false;
	}

}
