package rtt.core.utils;

import java.io.File;

import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;

/**
 * This is an utility class for configurations.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 */
public class ConfigurationUtils {	
	
	/**
	 * Adds a new configuration to the given rtt archive. 
	 * 
	 * @see rtt.core.utils.ConfigParameter
	 * @param archiveFile the file handler of the archive
	 * @param params the parameter for the new configuration
	 * @throws RTTException 
	 */
	public static void addConfiguration(File archiveFile, ConfigParameter params) throws RTTException {
		if (!params.isInitialized()) {
			throw new RTTException(Type.CONFIGURATION, "The configuration parameters are not fully initialized.");
		}
		
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.addConfiguration(
				params.lexerClass, 
				params.parserClass, 
				params.configName, 
				params.defaultConfig, 
				params.overwrite, 
				params.getClassPathEntries()
		);
		container.save();
	}
	
	/**
	 * Sets a configuration as the default configuration within the given rtt archive.
	 * 
	 * @param archiveFile the file handler of the archive
	 * @param configName the name of the configuration, which should be default
	 * @throws RTTException 
	 */
	public static void setConfigurationDefault(File archiveFile, String configName) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.setConfigurationDefault(configName);
		container.save();
	}

}
