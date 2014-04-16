package rtt.ui.content.history;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.editors.ReferenceEditor;
import rtt.ui.editors.input.OutputDataEditorInput;

public class OutputVersionContent extends AbstractVersionContent<OutputDataManager> implements IClickableContent {

	private OutputDataType type;

	public OutputVersionContent(IContent parent, Version version, String suiteName, String caseName, OutputDataType type) {
		super(parent, version, suiteName, caseName);
		this.type = type;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		IEditorInput input = new OutputDataEditorInput(
				getProject(), suiteName, caseName, version.getNr(), type);
		
		try {
			IDE.openEditor(currentPage, input, ReferenceEditor.ID, true);
		} catch (PartInitException e) {
			ErrorDialog.openError(currentPage.getActivePart().getSite()
					.getShell(), "Error", "Could not open editor", new Status(
					Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

}
