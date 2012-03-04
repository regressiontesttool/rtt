package rtt.ui.content.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Path;
import rtt.core.archive.logging.ArchiveLog;
import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.EntryType;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.manager.data.LogManager;
import rtt.ui.content.IContent;
import rtt.ui.content.IContentObserver;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.logging.LogEntryContent;
import rtt.ui.content.logging.TestrunContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.model.RttProject;

public class ProjectContent extends AbstractContent {

	private RttProject project;
	private List<IContent> testsuiteContents;
	private List<IContent> configContents;
	private List<IContent> logContents;

	private Map<String, IContentObserver> observers;

	public ProjectContent(RttProject project) {
		super(null);

		this.observers = new HashMap<String, IContentObserver>();
		this.project = project;

		loadContents();
	}

	private void loadContents() {
		testsuiteContents = loadTestsuites();
		configContents = loadConfigs();
		logContents = loadLog();

		childs.add(new SimpleTypedContent(this,
				ContentType.CONFIGURATION_DIRECTORY, configContents));
		childs.add(new SimpleTypedContent(this,
				ContentType.TESTSUITE_DIRECTORY, testsuiteContents));
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

	private List<IContent> loadTestsuites() {
		List<Testsuite> suites = project.getArchive().getTestsuites(false);
		List<IContent> contents = new ArrayList<IContent>();

		if (suites != null) {
			for (Testsuite testsuite : suites) {
				contents.add(new TestsuiteContent(this, testsuite));
			}
		}

		return contents;
	}

	private List<IContent> loadLog() {
		List<IContent> contents = new ArrayList<IContent>();

		LogManager logManager = project.getArchive().getLogManager();
		if (logManager != null) {
			ArchiveLog log = logManager.getData();
			if (log == null || log.getEntry().isEmpty()) {
				contents.add(new EmptyContent("No log entries found."));
			} else {
				for (Entry entry : log.getEntry()) {
					if (entry.getType() == EntryType.TESTRUN) {
						contents.add(new TestrunContent(this, entry));
					} else {
						contents.add(new LogEntryContent(this, entry));
					}					
				}
			}
		} else {
			contents.add(new EmptyContent("No archive log found."));
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

	public List<IContent> getTestsuiteContents() {
		return testsuiteContents;
	}

	public List<IContent> getConfigContents() {
		return configContents;
	}

	public List<IContent> getLogContents() {
		return logContents;
	}

	public void reload() {
		reload(true);
	}

	public void reload(boolean updateObserver) {
		childs.clear();

		loadContents();

		if (updateObserver) {
			fireContentChanged();
		}
	}

	public synchronized void addObserver(IContentObserver observer) {
		observers.put(observer.getObserverID(), observer);
	}

	public synchronized void removeObserver(IContentObserver observer) {
		observers.remove(observer.getObserverID());
	}

	public synchronized void fireContentChanged() {
		for (IContentObserver observer : observers.values()) {
			observer.update(this);
		}
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

		// FIXME do reload
		// projectContent.reload();
	}

	public void addTestsuite(String suiteName) throws RTTException {

		project.addTestsuite(suiteName);
		project.save();

		// FIXME do reload
		// projectContent.reload();
	}

	public void removeTestsuite(String suiteName) throws RTTException {

		project.removeTestsuite(suiteName);
		project.save();

		// FIXME do reload
		// projectContent.reload();
	}

	public void runTest(String suiteName) throws RTTException {
		RttProject project = this.getProject();
		project.runTests(suiteName, true);
		project.save();

		// FIXME do reload
		// projectContent.reload(false);
	}

	public List<Throwable> generateTest(String suiteName) throws RTTException {
		RttProject project = this.getProject();
		List<Throwable> exceptions = project.generateTests(suiteName);
		project.save();

		// FIXME do reload

		return exceptions;
	}
}
