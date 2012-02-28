package rtt.ui.content.history;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.IHistoryManager;
import rtt.core.manager.data.ReferenceManager;
import rtt.ui.RttPluginUI;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.internal.ContentIcon;
import rtt.ui.editors.form.MasterDetailFormEditor;
import rtt.ui.editors.input.RTTEditorInput;

public class ReferenceVersionContent extends AbstractContent implements
		IContent, IClickableContent {

	private Version version;
	private ReferenceManager refManager;

	public ReferenceVersionContent(IContent parent, Version version,
			IHistoryManager manager) {
		super(parent);
		this.version = version;
		this.refManager = (ReferenceManager) manager;
	}

	@Override
	public String getText() {
		return "Reference (" + version.getNr() + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.VERSION;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		try {
			IEditorPart part = IDE.openEditor(currentPage, new RTTEditorInput(
					refManager, version.getNr()), MasterDetailFormEditor.ID,
					true);

			if (part != null && part instanceof MasterDetailFormEditor) {
				((MasterDetailFormEditor) part)
						.setActivePage(MasterDetailFormEditor.PARSER_ID);
			}

		} catch (PartInitException e) {
			ErrorDialog.openError(currentPage.getActivePart().getSite()
					.getShell(), "Error", "Could not open editor", new Status(
					Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

}
