package rtt.ui.content.history;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.IHistoryManager;
import rtt.core.manager.data.InputManager;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.editors.input.InputEditorInput;

public class InputVersionContent extends AbstractVersionContent<InputManager>
		implements IClickableContent {

	public InputVersionContent(IContent parent, Version version,
			IHistoryManager manager) {
		super(parent, version, (InputManager) manager);
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		try {
			currentPage.openEditor(
					new InputEditorInput(manager, version.getNr()),
					"org.eclipse.ui.DefaultTextEditor", true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getText() {
		return "Version";
	}
}
