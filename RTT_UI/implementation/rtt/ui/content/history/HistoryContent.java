package rtt.ui.content.history;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.history.IHistoryManager;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public class HistoryContent extends AbstractContent {

	public enum VersionType {
		INPUT("Input data", ContentIcon.INPUT_HISTORY), REFERENCE("Reference data",
				ContentIcon.REFERENCE_HISTORY), TEST("Test data",
				ContentIcon.TEST_HISTORY);

		protected String baseText;
		protected ContentIcon icon;

		private VersionType(String baseText, ContentIcon icon) {
			this.baseText = baseText;
			this.icon = icon;
		}
	}

	private VersionType type;
	private int childCount;

	public HistoryContent(IContent parent, IHistoryManager manager,
			VersionType type) {

		super(parent);
		this.type = type;
		this.childCount = 0;

		if (manager.getHistory() != null) {
			for (Version version : manager.getHistory().getVersion()) {
				childs.add(getChild(version, type, manager));
				childCount++;
			}
		}
	}

	private IContent getChild(Version version, VersionType type,
			IHistoryManager manager) {

		switch (type) {
		case INPUT:
			return new InputVersionContent(this, version, manager);

		case REFERENCE:
			return new OutputVersionContent(this, version, manager);

		case TEST:
			return new OutputVersionContent(this, version, manager);
		}
		
		return null;
	}

	@Override
	public String getText() {
		return type.baseText + " (" + childCount + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return type.icon;
	}

}
