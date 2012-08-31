package rtt.core.manager.data;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Configurations;
import rtt.core.archive.configuration.Path;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.utils.Debug;
import rtt.core.utils.Debug.LogType;

/**
 * This Manager manages the processing of configurations.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class ConfigurationManager extends AbstractDataManager<Configurations> {

	/**
	 * This enumeration contains the result of the setConfiguration action of
	 * the {@link ConfigurationManager} indicating the actions which have been taken to the archive.
	 * 
	 * @author Christian Oelsner <C.Oelsner@gmail.com>
	 * @see ConfigurationManager
	 * @see ConfigurationManager#setConfiguration(Configuration, boolean)
	 */
	public enum ConfigStatus {
		/**
		 * A new configuration has been added.
		 */
		ADDED,
		/**
		 * An existing configuration has been updated.
		 */
		UPDATED,
		/**
		 * The configuration has been rejected.
		 */
		SKIPPED;
		
		/**
		 * true, if a new lexer class is set.
		 */
		public boolean lexerSet = false;
		
		/**
		 * true, if a new parser class is set.
		 */
		public boolean parserSet = false;
		
		/**
		 * A list containing all new entries to the class path
		 */
		public Set<String> newEntries = new TreeSet<String>();

		/**
		 * A list containig all deleted entries of a class path
		 */
		public Set<String> deletedEntries = new TreeSet<String>();
	}

	public ConfigurationManager(ArchiveLoader loader) {
		super(loader, new SimpleFileFetching("configs.xml", ""));
	}

	@Override
	public Configurations doLoad() throws Exception {
		return unmarshall(Configurations.class);
	}

	@Override
	public void doSave(Configurations data) {
		marshall(Configurations.class, data);
	}

	/**
	 * Sets a configuration to the archive.
	 * <p>
	 * If no configuration with the same name exists, the configuration will be
	 * added. If a configuration is already existing, the overwrite flag is used
	 * to decide what should be done.
	 * <p>
	 * A {@link ConfigStatus} element will indicate the result of the operation.
	 * 
	 * @param newConfig
	 *            the configuration, which should be set
	 * @param overwrite
	 *            set true to overwrite configuration, if already existing
	 * @return a {@link ConfigStatus}, indicating the the changes to the archive
	 * @see Configuration
	 * @see ConfigStatus
	 */
	public ConfigStatus setConfiguration(Configuration newConfig, boolean overwrite) {
		List<Configuration> configList = data.getConfiguration();
		Configuration oldConfig = getConfiguration(newConfig.getName());

		// old config exists, but should not be overwritten 
		if (oldConfig != null && overwrite == false) {
			return ConfigStatus.SKIPPED;
		}
		
		if (configList.size() < 1) {
			data.setDefault(newConfig.getName());
		}
		
		ConfigStatus state = null;
		
		// no config exists, create new one
		if (oldConfig == null) {
			configList.add(newConfig);
			
			state = ConfigStatus.ADDED;
			state.lexerSet = true;
			state.parserSet = true;
			
			Classpath cPath = newConfig.getClasspath();
			
			for (Path entry : cPath.getPath()) {
				state.newEntries.add(entry.getValue());
			}
			
	    // old config exists, update the old one
		} else {
			state = ConfigStatus.UPDATED;
			
			state.lexerSet = setLexerName(oldConfig, newConfig.getLexerClass());
			state.parserSet = setParserName(oldConfig, newConfig.getParserClass());			
			
			state.newEntries = addClasspathEntries(oldConfig, newConfig);
			state.deletedEntries = removeClasspathEntries(oldConfig, newConfig);
			
			// if nothing done, return skipped 
			if (!state.lexerSet && !state.parserSet && state.newEntries.isEmpty() && state.deletedEntries.isEmpty()) {
				state = ConfigStatus.SKIPPED;
			}
		}
		
		return state;
	}
	
	private boolean setLexerName(Configuration config, String lexerName) {
		if (config != null && lexerName != null) {
			String oldClass = config.getLexerClass();

			if (oldClass == null || oldClass.equals("")
					|| !oldClass.equals(lexerName)) {
				config.setLexerClass(lexerName);
				return true;
			}
		}
		
		return false;
	}
	
	private boolean setParserName(Configuration config, String parserName) {
		if (config != null && parserName != null) {
			String oldClass = config.getParserClass();

			if (oldClass == null || oldClass.equals("")
					|| !oldClass.equals(parserName)) {
				config.setParserClass(parserName);
				return true;
			}
		}
		
		return false;
	}
	
	private Set<String> addClasspathEntries(Configuration oldConfig, Configuration newConfig) {
		Set<String> newPathEntries = new TreeSet<String>();
		
		if (oldConfig != null && newConfig != null) {
			Classpath newClasspath = newConfig.getClasspath();
			if (newClasspath != null) {
				for (Path entry : newClasspath.getPath()) {
					if (addClasspathEntry(oldConfig, entry.getValue())) {
						newPathEntries.add(entry.getValue());
					}
				}
			}
		}
		
		
		
		return newPathEntries;
	}
	
	protected boolean addClasspathEntry(Configuration config, String entry) {
		if (config != null) {
			Classpath classpath = config.getClasspath();

			if (classpath == null) {
				classpath = new Classpath();
			}

			for (Path path : classpath.getPath()) {
				if (path.getValue().equals(entry)) {
					return false;
				}
			}

			Path newPath = new Path();
			newPath.setValue(entry);

			return classpath.getPath().add(newPath);
		}

		return false;
	}
	
	private Set<String> removeClasspathEntries(Configuration oldConfig, Configuration newConfig) {
		Set<String> removedEntries = new TreeSet<String>();
		
		if (oldConfig != null && newConfig != null) {
			Classpath oldClasspath = oldConfig.getClasspath();
			if (oldClasspath != null) {
				for (Path entry : oldClasspath.getPath()) {
					if (!hasClasspathEntry(newConfig, entry.getValue())) {
						removedEntries.add(entry.getValue());			
					}
				}
			}
		}
		
		oldConfig.setClasspath(newConfig.getClasspath());
		
		return removedEntries;
	}
	
	private boolean hasClasspathEntry(Configuration config, String value) {
		if (config != null && config.getClasspath() != null) {
			Classpath cPath = config.getClasspath();
			if (cPath.getPath() != null) {
				for (Path path : cPath.getPath()) {
					if (path.getValue() != null && path.getValue().equals(value)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	protected boolean removeClasspathEntry(Configuration config, String entry) {
		if (config != null) {
			int index = -1;
			Classpath classpath = config.getClasspath();

			if (classpath != null && classpath.getPath() != null) {
				List<Path> pathList = classpath.getPath();

				for (int i = 0; i < pathList.size(); i++) {
					if (pathList.get(i).getValue().equals(entry)) {
						index = i;
					}
				}

				if (index > -1) {
					pathList.remove(index);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns a {@link Configuration} or {@code null} for the given name.
	 * 
	 * @param configName
	 *            the name of the configuration
	 * @return {@link Configuration} or {@code null}
	 * @see Configuration
	 */
	public Configuration getConfiguration(String name) {
		List<Configuration> configList = data.getConfiguration();

		for (Configuration config : configList) {
			if (config.getName().equals(name)) {
				return config;
			}
		}

		return null;
	}

	/**
	 * Returns the default configuration of the archive.
	 * 
	 * @return the default configuration
	 * @see Configuration
	 * @see #getConfiguration(String)
	 */
	public Configuration getDefaultConfig() {
		return getConfiguration(data.getDefault());
	}

	/**
	 * Sets a new default configuration for the archive.
	 * 
	 * @param configName
	 *            name of the new default configuration
	 * @return true, if configuration has been set
	 * @see Configuration
	 */
	public boolean setDefaultConfig(String configName) {
		// CHRISTIAN auskommentiert, wegen ant task fehler (create archive,
		// create config)
		// Configuration config = getConfiguration(configName);
		//
		// if (config != null
		// && !config.getName().equals(getDefaultConfig().getName())) {
		// data.setDefault(configName);
		// return true;
		// }
		// return false;

		String defaultConfig = data.getDefault();
		data.setDefault(configName);

		if (defaultConfig == null || defaultConfig.equals(configName)) {
			return false;
		}

		return true;
	}

	/**
	 * Prints informations about all configurations to the {@link Debug}.
	 * 
	 * @see Configuration
	 * @see Debug
	 */
	public void print() {
		Debug.log(LogType.ALL, "DefaultConfiguration: " + data.getDefault());

		for (Configuration c : data.getConfiguration()) {

			Debug.log("Config: " + c.getName());

			if (c.getLexerClass() != null) {
				Debug.log("\tLexer: " + c.getLexerClass());
			}

			if (c.getParserClass() != null) {
				Debug.log("\tParser: " + c.getParserClass());
			}
		}
	}

	@Override
	protected Configurations getEmptyData() {
		return new Configurations();
	}

	/**
	 * Returns a list with all configurations.
	 * 
	 * @return list with all configurations.
	 * @see Configuration
	 * @see Configurations#getConfiguration()
	 */
	public List<Configuration> getConfigurations() {
		return data.getConfiguration();
	}

}
