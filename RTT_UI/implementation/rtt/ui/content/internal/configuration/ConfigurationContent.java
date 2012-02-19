package rtt.ui.content.internal.configuration;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.internal.ContentIcon;
import rtt.ui.content.internal.SimpleTypedContent;
import rtt.ui.content.internal.SimpleTypedContent.ContentType;

public class ConfigurationContent extends AbstractContent implements IDecoratableContent {

	private String name;
	private boolean isActive;
	private boolean isDefault;

	public ConfigurationContent(IContent parent, Configuration config) {
		super(parent);
		this.name = config.getName();
		this.isActive = false;

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

	@Override
	public String getText() {
		return name;
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
}