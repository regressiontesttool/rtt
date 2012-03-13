package rtt.ui.content.main;

import rtt.core.archive.logging.ArchiveLog;
import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.EntryType;
import rtt.core.manager.data.LogManager;
import rtt.ui.content.IContent;
import rtt.ui.content.logging.LogEntryContent;
import rtt.ui.content.logging.TestrunContent;

public class LogDirectoryContent extends AbstractContent implements IContent {	

	private boolean isEmpty;

	public LogDirectoryContent(ProjectContent parent) {
		super(parent);
		loadContents();		
	}
	
	private void loadContents() {
		isEmpty = false;
		
		LogManager logManager = getProject().getArchive().getLogManager();
		if (logManager != null) {
			ArchiveLog log = logManager.getData();
			if (log == null || log.getEntry().isEmpty()) {
				isEmpty = true;
				childs.add(new EmptyContent("No log entries found."));
			} else {
				for (Entry entry : log.getEntry()) {
					long start = System.currentTimeMillis();
					if (entry.getType() == EntryType.TESTRUN) {
						childs.add(new TestrunContent(this, entry));
					} else {
						childs.add(new LogEntryContent(this, entry));
					}
					System.out.println(entry.getType().toString() + ": " + (System.currentTimeMillis() - start));
				}
			}
		} else {
			childs.add(new EmptyContent("No archive log found."));
		}
	}
	
	protected void reload() {
		childs.clear();
		loadContents();
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
