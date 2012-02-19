package rtt.ui.content.internal.log;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.Testrun;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.internal.ContentIcon;

public class TestrunContent extends AbstractContent implements IContent {

	Testrun testrun;
	Calendar calendar;
	String suiteName;
	
	public TestrunContent(IContent parent, Entry entry) {
		super(parent);
		
		if (entry instanceof Testrun) {
			this.testrun = (Testrun) entry;
			this.calendar = DatatypeConverter.parseDate(testrun.getDate().toXMLFormat());
			this.suiteName = testrun.getTestsuite();
			
			for (Result result : ((Testrun) entry).getResult()) {
				childs.add(new TestResultContent(this, result));
			}
		}
	}

	@Override
	public String getText() {
		return "Testrun: " + getFormatedDate() + " [" + testrun.getConfiguration() + "]";
	}
	
	private String getFormatedDate() {
		return String.format("%1$te.%1$tm.%1$tY %1$tH:%1$tM:%1$tS", calendar);
	}
	
	public String getTestsuite() {
		return suiteName;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTRUN;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public String getConfiguration() {
		return testrun.getConfiguration();
	}

}
