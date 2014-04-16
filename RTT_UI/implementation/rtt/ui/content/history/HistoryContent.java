package rtt.ui.content.history;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.history.IHistoryManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.content.IContent;
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

		for (Version version : manager.getHistory().getVersion()) {
			childs.add(getChild(version, type, manager.getSuiteName(), manager.getCaseName()));
			childCount++;
		}
		
	}

	private IContent getChild(Version version, VersionType type, String suiteName, String caseName) {

		switch (type) {
		case INPUT:
			return new InputVersionContent(this, version, suiteName, caseName);

		case REFERENCE:
			return new OutputVersionContent(this, version, suiteName, caseName, OutputDataType.REFERENCE);

		case TEST:
			return new OutputVersionContent(this, version, suiteName, caseName, OutputDataType.TEST);
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
