package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public abstract class ModelElementSelectionChangedListener implements
		ISelectionChangedListener {

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object selectedObject = getSelection(event.getSelection());
		if (selectedObject != null) {
			handleSelection(selectedObject);
		}
	}

	private Object getSelection(ISelection selection) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).getFirstElement();
		}

		return null;
	}
	
	protected abstract void handleSelection(Object selectedObject);
}
