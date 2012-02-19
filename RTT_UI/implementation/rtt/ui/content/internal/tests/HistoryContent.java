package rtt.ui.content.internal.tests;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.IHistoryManager;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.internal.ContentIcon;

public class HistoryContent extends AbstractContent implements IDecoratableContent {
	
	public enum VersionType {
		INPUT ("Input", ContentIcon.INPUT_HISTORY),
		REFERENCE ("Reference", ContentIcon.REFERENCE_HISTORY),
		TEST ("Test", ContentIcon.TEST_HISTORY);
		
		protected String baseText;
		protected ContentIcon icon;
		
		private VersionType(String baseText, ContentIcon icon) {
			this.baseText = baseText;
			this.icon = icon;
		}
	}
	
	private IHistoryManager manager;
	private VersionType type;

	public HistoryContent(IContent parent, IHistoryManager manager, VersionType type) {
		super(parent);
		this.manager = manager;
		this.type = type;
		
		if (manager.getHistory() != null) {
			for (Version version : manager.getHistory().getVersion()) {
				childs.add(getChild(version, type, manager));
			}
			
		}
	}
	
	private IContent getChild(Version version, VersionType type, IHistoryManager manager2) {
	
		if (type == VersionType.INPUT) {
			return new InputVersionContent(this, version, manager2);
		}
		
		return new VersionContent(this, version);
	}

	@Override
	public String getText() {
		return type.baseText;
	}
	
	@Override
	protected ContentIcon getIcon() {
		return type.icon;
	}

	@Override
	public Image decorateImage(ResourceManager manager, Image image, IContent content) {
		return image;
	}

	@Override
	public String decorateText(String text, IContent content) {
		return text + " (" + manager.getCurrentNr() + ")";
	}

}
