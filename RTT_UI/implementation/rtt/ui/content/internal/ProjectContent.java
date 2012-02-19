package rtt.ui.content.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IContentObserver;
import rtt.ui.content.internal.SimpleTypedContent.ContentType;
import rtt.ui.content.internal.configuration.ConfigurationContent;
import rtt.ui.content.internal.data.TestsuiteContent;
import rtt.ui.model.RttProject;

public class ProjectContent extends AbstractContent {

	private RttProject project;
	private List<IContent> testsuiteContents;
	private List<IContent> configContents;
	
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
		
		childs.add(new SimpleTypedContent(this, ContentType.CONFIGURATION_DIRECTORY, configContents));
		childs.add(new SimpleTypedContent(this, ContentType.TESTSUITE_DIRECTORY, testsuiteContents));
	}
	
	@Override
	public void load() {
		childs.clear();
		loadContents();
	}

	private List<IContent> loadConfigs() {
		List<Configuration> configs = project.getArchive().getConfigurations();
		Configuration activeConfig = project.getArchive().getActiveConfiguration();
		Configuration defaultConfig = project.getArchive().getDefaultConfiguration();
		
		List<IContent> contents = new ArrayList<IContent>();

		if (configs != null) {
			for (Configuration config : configs) {
				ConfigurationContent content = new ConfigurationContent(this, config);
				
				if (activeConfig != null) {
					content.setActive(config.getName().equals(activeConfig.getName()));
				}
				
				if (defaultConfig != null) {
					content.setDefault(config.getName().equals(defaultConfig.getName()));
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
	
	public void reload() {
		reload(true);	
	}
	
	public void reload(boolean updateObserver) {
		childs.clear();
		
		loadContents();
		
		if (updateObserver) {
			updateObserver();
		}
	}
	
	public void updateObserver() {
		fireContentChanged();
	}
	
	public synchronized void addObserver(IContentObserver observer) {
		observers.put(observer.getObserverID(), observer);
	}
	
	public synchronized void removeObserver(IContentObserver observer) {
		observers.remove(observer.getObserverID());
	}
	
	protected synchronized void fireContentChanged() {
		for (IContentObserver observer : observers.values()) {
			observer.update(this);
		}
	}
}
