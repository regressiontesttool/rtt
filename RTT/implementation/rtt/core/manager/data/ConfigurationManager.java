package rtt.core.manager.data;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Configurations;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.utils.DebugLog;
import rtt.core.utils.DebugLog.LogType;

public class ConfigurationManager extends DataManager<Configurations> {

	public enum ConfigStatus {
		CREATED, REPLACED, SKIPPED;
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

	public Configuration getConfiguration(String name) {
		List<Configuration> configList = data.getConfiguration();

		for (Configuration config : configList) {
			if (config.getName().equals(name)) {
				return config;
			}
		}

		// DebugLog.log(LogType.VERBOSE, "Configuration [" + name
		// + "] cannot be found");

		return null;
	}

	public Configuration getDefaultConfig() {
		return getConfiguration(data.getDefault());
	}

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

	public List<Configuration> getConfigurations() {
		return data.getConfiguration();
	}

}
