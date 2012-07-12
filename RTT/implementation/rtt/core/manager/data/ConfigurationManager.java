package rtt.core.manager.data;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Configurations;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.utils.DebugLog;
import rtt.core.utils.DebugLog.LogType;

/**
 * This Manager manages the processing of configurations.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class ConfigurationManager extends AbstractDataManager<Configurations> {

	/**
	 * This enumeration contains the result of the setConfiguration action of
	 * the {@link ConfigurationManager}
	 * 
	 * @author Christian Oelsner <C.Oelsner@gmail.com>
	 * @see ConfigurationManager
	 * @see ConfigurationManager#setConfiguration(Configuration, boolean)
	 */
	public enum ConfigStatus {
		/**
		 * A new configuration has been created.
		 */
		CREATED, 
		/**
		 * A configuration has been replaced.
		 */
		REPLACED, 
		/**
		 * The configuration has been rejected.
		 */
		SKIPPED;
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
	 * @param config
	 *            the configuration, which should be set
	 * @param overwrite
	 *            set true to overwrite configuration, if already existing
	 * @return a {@link ConfigStatus}, indicating the the changes to the archive
	 * @see Configuration
	 * @see ConfigStatus
	 */
	public ConfigStatus setConfiguration(Configuration config, boolean overwrite) {
		List<Configuration> configList = data.getConfiguration();
		Configuration oldConfig = getConfiguration(config.getName());

		if (oldConfig != null && overwrite == false) {
			return ConfigStatus.SKIPPED;
		}

		// create or update config

		if (configList.size() < 1) {
			data.setDefault(config.getName());
		}

		if (oldConfig == null) {
			configList.add(config);
			return ConfigStatus.CREATED;
		} else {
			int index = configList.indexOf(oldConfig);
			configList.set(index, config);
			return ConfigStatus.REPLACED;
		}

	}

	/**
	 * Returns the configuration of the given name or null, if not found.
	 * 
	 * @param name
	 *            the name of the configuration
	 * @return a configuration or null
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
	 * @return true, if configuration is set
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
	 * Prints informations about all configurations to the {@link DebugLog}.
	 * 
	 * @see Configuration
	 * @see DebugLog
	 */
	public void print() {
		DebugLog.log(LogType.ALL, "DefaultConfiguration: " + data.getDefault());

		for (Configuration c : data.getConfiguration()) {

			DebugLog.log("Config: " + c.getName());

			if (c.getLexerClass() != null) {
				DebugLog.log("\tLexer: " + c.getLexerClass().getValue());
			}

			if (c.getParserClass() != null) {
				DebugLog.log("\tParser: " + c.getParserClass().getValue());
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
