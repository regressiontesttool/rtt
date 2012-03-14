package rtt.ui.content.main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Path;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.IContent;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;
import rtt.ui.model.RttProject;

public class ProjectContent extends AbstractContent {

	private RttProject project;
	
	private List<IContent> configContents;
	
	private LogDirectory logDirectory;
	private TestsuiteDirectory testsuiteDirectory;

	public ProjectContent(RttProject project) {
		super(null);
		
		this.project = project;

		logDirectory = new LogDirectory(this);
		testsuiteDirectory = new TestsuiteDirectory(this);
		
		loadContents();		
	}

	private void loadContents() {
		configContents = loadConfigs();

		childs.add(new SimpleTypedContent(this,
				ContentType.CONFIGURATION_DIRECTORY, configContents));
		
		childs.add(testsuiteDirectory);
	}

	@Override
	public void load() {
		childs.clear();
		loadContents();
	}

	private List<IContent> loadConfigs() {
		List<Configuration> configs = project.getArchive().getConfigurations();
		Configuration activeConfig = project.getArchive()
				.getActiveConfiguration();
		Configuration defaultConfig = project.getArchive()
				.getDefaultConfiguration();		
		
		List<IContent> contents = new ArrayList<IContent>();

		if (configs != null) {
			for (Configuration config : configs) {
				ConfigurationContent content = new ConfigurationContent(this,
						config);

				if (activeConfig != null) {
					content.setActive(config.getName().equals(
							activeConfig.getName()));
				}

				if (defaultConfig != null) {
					content.setDefault(config.getName().equals(
							defaultConfig.getName()));
				}

				contents.add(content);
			}
		}

		return contents;
	}

	public String getText() {
		return project.getName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PROJECT;
	}

	@Override
	public final RttProject getProject() {
		return project;
	}

	public List<IContent> getConfigContents() {
		return configContents;
	}

	public LogDirectory getLogDirectory() {
		return logDirectory;
	}
	
	public TestsuiteDirectory getTestsuiteDirectory() {
		return testsuiteDirectory;
	}

	public void reload() {
		logDirectory.reload();
		testsuiteDirectory.reload();
		
		childs.clear();
		loadContents();		
	}

	public void addConfiguration(Configuration config, boolean makeDefault)
			throws RTTException {
		String lexerClass = config.getLexerClass().getValue();
		String parserClass = config.getParserClass().getValue();
		String configName = config.getName();

		List<String> cp = new LinkedList<String>();
		if (config.getClasspath() != null) {
			for (Path path : config.getClasspath().getPath()) {
				if (path.getValue() != null) {
					cp.add(path.getValue());
				}
			}
		}

		project.addConfiguration(lexerClass, parserClass, configName,
				makeDefault, cp);
		project.save();
		
		reload();
	}

	public void addTestsuite(String suiteName) throws RTTException {
		project.addTestsuite(suiteName);
		project.save();
		
		reload();
	}

	public void removeTestsuite(String suiteName) throws RTTException {

		project.removeTestsuite(suiteName);
		project.save();
		
		reload();
	}

	public void runTest(String suiteName) throws RTTException {
		project.runTests(suiteName, true);
		project.save();
	}

	public List<Throwable> generateTest(String suiteName) throws RTTException {
		List<Throwable> exceptions = project.generateTests(suiteName);
		project.save();

		return exceptions;
	}

	public Configuration createEmptyConfiguration() {		
		return project.createEmptyConfiguration();
	}
}
