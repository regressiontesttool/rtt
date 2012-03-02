package rtt.ui.content.internal.data;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rtt.ui.content.AbstractContent;
import rtt.ui.content.ContentIcon;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.editors.input.InputEditorInput;

public class InputContent extends AbstractContent implements IContent,
		IClickableContent {

	

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
			e.printStackTrace();
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
