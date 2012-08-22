package rtt.ui.content.testsuite;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rtt.ui.RttLog;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.editors.input.InputEditorInput;

public class InputContent extends AbstractContent implements IClickableContent {

	protected String caseName;
	protected String suiteName;
	protected int version;

	public InputContent(IContent parent, String suiteName, String caseName, int version) {
		super(parent);
		this.caseName = caseName;
		this.suiteName = suiteName;
		this.version = version;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		try {
			currentPage.openEditor(new InputEditorInput(suiteName, caseName, version),
					"org.eclipse.ui.DefaultTextEditor", true);
		} catch (PartInitException e) {
			RttLog.log(e);
		}
	}

	@Override
	public String getText() {
		return "Input (" + version + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.INPUT;
	}

}
