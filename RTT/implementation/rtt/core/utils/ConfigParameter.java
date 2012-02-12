package rtt.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be used by {@link ConfigurationUtils} to parameterize the creation
 * of a new configuration.
 *  
 * @author C.Oelsner <C.Oelsner@web.de>
 */
public class ConfigParameter {
	
	private List<String> cpEntries;
	
	/**
	 * The fully qualified name of the lexer class.
	 */
	public String lexerClass;
	
	/**
	 * The fully qualified name of the parser class.
	 */
	public String parserClass;
	
	/**
	 * The name of the configuration
	 */
	public String configName;	
	
	/**
	 * Makes the new configuration the default configuration for the archive
	 * The default value is true
	 */
	public boolean defaultConfig = true;
	
	/**
	 * Overwrites an old configuration with the same name of this configuration.
	 * The default value is true
	 */
	public boolean overwrite = true;
	
	/**
	 * Creates a new configuration parameter. The new parameter object will be initialized with an Class path entries need to be added after creation.
	 * 
	 * Configurations with same name will be overwritten. (see {@link #overwrite})
	 * The new configuration will be the default configuration of the archive. (see {@link #configName})
	 * 
	 * @param lexerClass the fully qualified name of the lexer class
	 * @param parserClass the fully qualified name of the parser class
	 * @param configName the name of the new configuration
	 */
	public ConfigParameter(String lexerClass, String parserClass, String configName) {
		this.lexerClass = lexerClass;
		this.parserClass = parserClass;
		this.configName = configName;
		
		defaultConfig = true;
		overwrite = true;
		cpEntries = new ArrayList<String>();
	}
	
	/**
	 * Add class path entries to the parameter object.
	 * 
	 * @param classPathEntries list of class path entries
	 * @param clear when true, stored list will be cleared before adding new entries
	 */
	public void addClassPathEntries(List<String> classPathEntries, boolean clear) {
		if (clear) {
			cpEntries.clear();
		}
		
		for (String entry : classPathEntries) {
			cpEntries.add(entry);
		}
	}
	
	/**
	 * Returns the entries of the class path
	 * @return entries of class path
	 */
	public List<String> getClassPathEntries() {
		return cpEntries;
	}
	
	/**
	 * Checks, if parameter object is fully initialized.
	 *
	 * @return true, if initialized
	 */
	public boolean isInitialized() {
		if (lexerClass == null || lexerClass.equals("")) {
			return false;
		}
		
		if (parserClass == null || parserClass.equals("")) {
			return false;
		}
		
		if (configName == null || configName.equals("")) {
			return false;
		}
		
		return true;
	}		
}
