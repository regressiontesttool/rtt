package rtt.ui.content.testsuite;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.editors.ReferenceEditor;
import rtt.ui.editors.input.OutputDataEditorInput;

public class ReferenceContent extends AbstractContent implements
		IClickableContent {

	private String suiteName;
	private String caseName;
	private int version;

	public ReferenceContent(IContent parent, String suiteName, String caseName,
			int version) {
		super(parent);
		this.suiteName = suiteName;
		this.caseName = caseName;
		this.version = version;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		
		IEditorInput input = new OutputDataEditorInput(getProject(), suiteName, 
				caseName, version, OutputDataType.REFERENCE);

		try {
			IDE.openEditor(currentPage, input, ReferenceEditor.ID, true);			
		} catch (PartInitException e) {
			ErrorDialog.openError(currentPage.getActivePart().getSite()
					.getShell(), "Error", "Could not open editor", new Status(
					Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

	@Override
	public String getText() {
		return "Reference (" + version + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.REFERENCE;
	}

}
