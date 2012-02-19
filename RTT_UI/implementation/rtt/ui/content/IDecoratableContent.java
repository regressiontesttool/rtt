package rtt.ui.content;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

public interface IDecoratableContent extends IContent {
	
	Image decorateImage(ResourceManager manager, Image image, IContent content);
	String decorateText(String text, IContent content);
}
