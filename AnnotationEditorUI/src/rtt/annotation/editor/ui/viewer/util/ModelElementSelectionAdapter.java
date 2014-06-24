package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import rtt.annotation.editor.model.ModelElement;

public abstract class ModelElementSelectionAdapter extends SelectionAdapter {
	
	private Viewer viewer;
	
	public ModelElementSelectionAdapter(Viewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		ISelection selection = viewer.getSelection();
		Object selectedObject = getSelection(selection);
		
		if (selectedObject != null && selectedObject instanceof ModelElement<?>) {
			handleElement((ModelElement<?>) selectedObject);
		}
	}
	
	private Object getSelection(ISelection selection) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).getFirstElement();
		}

		return null;
	}
	
	protected abstract void handleElement(ModelElement<?> selectedObject);

}
