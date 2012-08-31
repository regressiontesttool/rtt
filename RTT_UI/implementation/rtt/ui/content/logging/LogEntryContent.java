package rtt.ui.content.logging;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import rtt.core.archive.logging.Detail;
import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.EntryType;
import rtt.ui.content.IContent;
import rtt.ui.content.main.ContentIcon;

public class LogEntryContent extends AbstractLogContent {

	private Entry entry;
	protected Calendar calendar;
	
	public LogEntryContent(IContent parent, Entry entry) {
		super(parent);
		this.entry = entry;
		
		calendar = DatatypeConverter.parseDate(entry.getDate().toXMLFormat());
		
		if (entry.getDetail() != null && !entry.getDetail().isEmpty()) {
			for (Detail detail : entry.getDetail()) {
				childs.add(new LogDetailContent(this, detail));
			}
		}
	}

	private ContentIcon getContentIcon(Entry entry) {
		switch (entry.getType()) {
		case INFO:
			return ContentIcon.INFO;
			
		case GENERATION:
			return ContentIcon.GENERATION;
			
		case TESTRUN:
			return ContentIcon.TESTRUN;
			
		case ARCHIVE:
			return ContentIcon.ARCHIVE;
			
		default:
			return ContentIcon.PLACEHOLDER;
		}
	}

	public EntryType getType() {
		return entry.getType();
	}

	@Override
	public ContentIcon getIcon() {
		return getContentIcon(entry);
	}

	@Override
	public String getText(int columnIndex) {
		if (columnIndex == 2) {
			return getFormatedDate();
		}
		
		return super.getText(columnIndex);
	}
	
	public Calendar getCalendar() {
		return calendar;
	}
	
	public String getFormatedDate() {
		return String.format("%1$te.%1$tm.%1$tY %1$tH:%1$tM:%1$tS", calendar);
	}
	
	@Override
	public String getTitle() {
		return entry.getType().toString();
	}
	
	@Override
	public String getMessage() {
		return entry.getMsg() + " " + entry.getSuffix();
	}

	@Override
	public int compareTo(AbstractLogContent o) {
		if (o instanceof LogEntryContent) {
			LogEntryContent entry = (LogEntryContent) o;
			return -(this.getCalendar().compareTo(entry.getCalendar()));	
		}
		
		return 0;				
	}
}
