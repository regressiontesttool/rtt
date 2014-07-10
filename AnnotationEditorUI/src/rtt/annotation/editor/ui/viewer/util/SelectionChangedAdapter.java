package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import rtt.annotation.editor.model.ModelElement;

public abstract class SelectionChangedAdapter implements
		ISelectionChangedListener {

	protected Object getSelection(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).getFirstElement();
		}

		return null;
	}
	
	protected ModelElement<?> getSelectedElement(SelectionChangedEvent event) {
		Object selection = getSelection(event);
		if (selection instanceof ModelElementViewerItem) {
			return ((ModelElementViewerItem) selection).getModelElement();
		}
		
		return null;
	}
}
