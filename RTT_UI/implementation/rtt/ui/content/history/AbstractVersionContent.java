package rtt.ui.content.history;

import java.util.Calendar;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.IHistoryManager;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public abstract class AbstractVersionContent<T extends IHistoryManager> extends
		AbstractContent implements IDecoratableContent {

	protected T manager;
	protected Version version;
	protected Calendar calendar;

	public AbstractVersionContent(IContent parent, Version version, T manager) {
		super(parent);

		this.manager = manager;
		this.version = version;
		this.calendar = version.getDate();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.VERSION;
	}

	@Override
	public Image decorateImage(ResourceManager manager, Image image,
			IContent content) {

		return image;
	}

	@Override
	public String decorateText(String text, IContent content) {
		return text + " " + version.getNr() + " ("
				+ String.format("%1$te.%1$tm.%1$tY - %1$tH:%1$tM", calendar)
				+ ")";
	}

}
