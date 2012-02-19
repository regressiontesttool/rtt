package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Configuration;
import rtt.ui.core.archive.IConfiguration;
import rtt.ui.core.archive.ILexerClass;
import rtt.ui.core.archive.IParserClass;
import rtt.ui.core.archive.IPath;

public class XMLConfiguration implements IConfiguration {
	
	private String name;
	private String description;
	private ILexerClass lexerClass;
	private IParserClass parserClass;
	private List<IPath> classPath;
	private boolean isDefault;
	private boolean isActive;
	
	protected XMLConfiguration(final Configuration configuration, boolean isDefault) {
		this.name = configuration.getName();
		this.description = configuration.getDescription();
		this.isDefault = isDefault;
		
		this.lexerClass = new ILexerClass() {
			@Override
			public String getName() {
				return configuration.getLexerClass().getValue();
			}
		};
		
		this.parserClass = new IParserClass() {
			
			@Override
			public String getName() {
				return configuration.getParserClass().getValue();
			}
		};
		
		classPath = XMLPath.getList(configuration);		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean isDefault() {
		return isDefault;
	}

	@Override
	public ILexerClass getLexerClass() {
		return lexerClass;
	}

	@Override
	public IParserClass getParserClass() {
		return parserClass;
	}

	@Override
	public List<IPath> getClasspathEntries() {
		return classPath;
	}

	public static List<IConfiguration> getList(Archive archive) {
		List<IConfiguration> list = new LinkedList<IConfiguration>();
		String activeConfig = "";
		String defaultConfig = "";
		
		if (archive.getActiveConfiguration() != null) {
			activeConfig = archive.getActiveConfiguration().getName(); 
		}
		
		if (archive.getDefaultConfiguration() != null) {
			defaultConfig = archive.getDefaultConfiguration().getName();
		}
		
		
		for (Configuration config : archive.getConfigurations()) {
			XMLConfiguration item = new XMLConfiguration(config, config.getName().equals(defaultConfig));
			item.setActive(config.getName().equals(activeConfig));
			list.add(item);
		}
		
		return list;
	}
	
	@Override
	public boolean isActive() {
		return isActive;
	}
	
	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
