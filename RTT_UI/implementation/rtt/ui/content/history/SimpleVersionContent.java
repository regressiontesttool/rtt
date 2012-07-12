package rtt.ui.content.history;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.history.IHistoryManager;
import rtt.ui.content.IContent;

public class SimpleVersionContent extends AbstractVersionContent<IHistoryManager> {
	
	public SimpleVersionContent(IContent parent, Version version, IHistoryManager manager) {
		super(parent, version, manager);
	}

	@Override
	public String getText() {
		return "Version";
	}	
}
