package rtt.ui.content.configuration;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;

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
		if (config.getInitialNode() != null) {
			addExecutor(ContentType.INITIALNODE, config.getInitialNode());
		}

		if (config.getClasspath() != null) {
			childs.add(new ClasspathContent(this, config.getClasspath()));
		}
	}	
	
	private void addExecutor(ContentType type, String text) {
		if (text == null || text.equals("")) {
			text = "<None>";
		}
		childs.add(new SimpleTypedContent(this, type, text));
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
	
	public boolean isDefault() {
		return isDefault;
	}

	public Configuration getConfiguration() {
		return config;
	}
}