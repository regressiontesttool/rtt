package rtt.ui.content.main;

import rtt.core.archive.logging.ArchiveLog;
import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.EntryType;
import rtt.core.manager.data.LogManager;
import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.logging.LogEntryContent;
import rtt.ui.content.logging.TestrunContent;

public class LogDirectory extends AbstractContent {	

	private boolean isEmpty = false;

	public LogDirectory(ProjectContent parent) {
		super(parent);
		loadContents();		
	}
	
	private void loadContents() {
		LogManager logManager = getProject().getLogManager();
		
		if (logManager != null) {
			ArchiveLog log = logManager.getData();
			
			if (log == null || log.getEntry().isEmpty()) {				
				isEmpty = true;
				childs.add(new EmptyContent("No log entries found."));
				
			} else {
				for (Entry entry : log.getEntry()) {
					if (entry.getType() == EntryType.TESTRUN) {
						childs.add(new TestrunContent(this, entry));
					} else {
						childs.add(new LogEntryContent(this, entry));
					}
				}
			}
		} else {
			childs.add(new EmptyContent("No archive log found."));
		}
	}
	
	@Override
	public void reload(ReloadInfo info) {
		if (info.contains(Content.TESTRUN) || info.contains(Content.DETAIL)) {
			for (IContent content : childs) {
				content.reload(info);
			}
		} else {
			childs.clear();
			loadContents();
		}
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
