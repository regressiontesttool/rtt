package rtt.ui.content.history;

import java.util.Calendar;

import org.eclipse.swt.graphics.Image;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.history.IHistoryManager;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public abstract class AbstractVersionContent<T extends IHistoryManager> extends
		AbstractContent implements IColumnableContent {

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
	public String getText() {
		return "Version " + version.getNr();
	}
	
	@Override
	public String getText(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getText();
			
		case 1:
			if (version.getInputBase() != null) {
				return "Version " + version.getInputBase();
			} else {
				return "";
			}
			
		case 2:
			return getDateString();
			
					

		default:
			return "";
		}
	}
	
	@Override
	public Image getImage(int columnIndex) {
		if (columnIndex == 0) {
			return getImage();
		}
		
		return null;
	}
	
	private String getDateString() {
		return String.format("%1$te.%1$tm.%1$tY - %1$tH:%1$tM:%1$tS", calendar);
	}
	
	@Override
	public String getToolTip() {
		if (version.getInputBase() != null) {
			return "Generated with Input data: Version " + version.getInputBase();
		}
		
		return "";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.VERSION;
	}
}
