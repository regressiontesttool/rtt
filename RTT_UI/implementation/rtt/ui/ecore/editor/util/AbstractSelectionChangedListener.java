package rtt.ui.ecore.editor.util;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import rtt.ui.viewer.ViewerUtils;

/**
 * Used as a {@link ISelectionChangedListener}. <br />
 * This class automatically reads the data of an {@link IStructuredSelection}.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public abstract class AbstractSelectionChangedListener 
	implements ISelectionChangedListener {

	@Override
	public final void selectionChanged(final SelectionChangedEvent event) {
		Object selectedObject = ViewerUtils.getSelection(event.getSelection());
		handleObject(selectedObject);
	}

	public abstract void handleObject(Object object);
}
