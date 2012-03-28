package rtt.ui.content.configuration;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.configuration.Configuration;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;
import rtt.ui.model.RttProject;

public class ConfigurationContent extends AbstractContent implements IDecoratableContent {

	private Configuration config;
	private boolean isActive;
	private boolean isDefault;

	public ConfigurationContent(IContent parent, Configuration config) {
		super(parent);
		this.config = config;
		this.isActive = false;	
		
		loadContent();
	}
	
	private void loadContent() {
		if (config.getLexerClass() != null) {
			childs.add(new SimpleTypedContent(this, ContentType.LEXERCLASS,
					config.getLexerClass().getValue()));
		}

		if (config.getParserClass() != null) {
			childs.add(new SimpleTypedContent(this, ContentType.PARSERCLASS,
					config.getParserClass().getValue()));
		}

		if (config.getClasspath() != null) {
			childs.add(new ClasspathContent(this, config.getClasspath()));
		}
	}
	
	public void reload() {
		childs.clear();
		loadContent();
	}

	@Override
	public String getText() {
		return config.getName();
	}

	@Override
	public String decorateText(String text, IContent content) {
		if (isDefault) {
			text += " (default)";
		}

		if (isActive) {
			text += " (active)";
		}

		return text;
	}
	
	@Override
	public Image decorateImage(ResourceManager manager, Image image, IContent content) {
		return image;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.CONFIG;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Configuration getConfiguration() {
		return config;
	}

	public void addClasspathEntry(String value) throws RTTException {
		RttProject project = this.getProject();
		project.addClassPathEntry(config.getName(), value);
		project.save();
		
		reload();
	}

	public void removeClasspathEntry(String value) throws RTTException {
		RttProject project = this.getProject();
		project.removeClasspathEntry(config.getName(), value);
		project.save();
		
		reload();
	}

	public void changeActive() {
		RttProject project = this.getProject();
		this.setActive(project.setActiveConfiguration(config.getName()));
		
		reload();
	}
}