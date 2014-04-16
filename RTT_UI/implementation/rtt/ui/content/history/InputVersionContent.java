package rtt.ui.content.history;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.history.InputManager;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.editors.input.InputEditorInput;

public class InputVersionContent extends AbstractVersionContent<InputManager>
		implements IClickableContent {

	public InputVersionContent(IContent parent, Version version, String suiteName, String caseName){
		super(parent, version, suiteName, caseName);
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		try {
			currentPage.openEditor(
					new InputEditorInput(this, suiteName, caseName, version.getNr()),
					"org.eclipse.ui.DefaultTextEditor", true);
		} catch (PartInitException e) {
			ErrorDialog.openError(currentPage.getActivePart().getSite()
					.getShell(), "Error", "Could not open editor", new Status(
					Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}
}
