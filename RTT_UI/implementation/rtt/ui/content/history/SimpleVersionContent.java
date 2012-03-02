package rtt.ui.content.history;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.history.Version;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.ContentIcon;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;

public class SimpleVersionContent extends AbstractContent implements IDecoratableContent {
	
	private Version version;
	
	public SimpleVersionContent(IContent parent, Version version) {
		super(parent);
		this.version = version;
	}

	@Override
	public String getText() {
		return "Version";
	}
	
	@Override
	public String decorateText(String text, IContent content) {
		return text + " " + version.getNr() + " (" + String.format("%1$te.%1$tm.%1$tY", version.getDate()) + ")";
	}
	
	@Override
	public Image decorateImage(ResourceManager manager, Image image, IContent content) {
		return image;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.VERSION;
	}	
}
