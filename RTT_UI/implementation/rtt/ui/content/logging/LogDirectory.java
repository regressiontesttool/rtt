package rtt.ui.content.logging;

import java.util.List;

import rtt.core.archive.Archive;
import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.EntryType;
import rtt.core.manager.Manager;
import rtt.core.manager.data.LogManager;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.EmptyContent;
import rtt.ui.content.main.ProjectContent;

public class LogDirectory extends AbstractContent {	

	private boolean isEmpty = false;

	public LogDirectory(ProjectContent parent) {
		super(parent);		
	}
	
	private void loadContents(Archive archive) {
		LogManager logManager = archive.getLogManager();
		List<Entry> entries = logManager.getLogEntries();
		
		if (entries == null || entries.isEmpty()) {
			isEmpty = true;
			childs.add(new EmptyContent("No log entries found."));
		} else {
			for (Entry entry : entries) {
				if (entry.getType() == EntryType.TESTRUN) {
					childs.add(new TestrunContent(this, entry));
				} else {
					childs.add(new LogEntryContent(this, entry));
				}
			}
		}
	}
	
	public void reload(ReloadInfo info, Manager manager) {
		childs.clear();
		loadContents(manager.getArchive());
		
//		TODO nur entsprechende Elemente updaten, statt alles
//		if (info.contains(Content.TESTRUN) || info.contains(Content.DETAIL)) {
//			for (IContent content : childs) {
//				content.reload(info);
//			}
//		} else {
//			childs.clear();
//			loadContents();
//		}
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}

	@Override
	public String getText() {
		return "LogDirectory";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PLACEHOLDER;
	}

}
