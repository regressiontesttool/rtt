package rtt.ui.ecore.editor.util;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

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
		if (event.getSelection() instanceof IStructuredSelection) {
			
			IStructuredSelection selection = 
					(IStructuredSelection) event.getSelection();
			
			handleObject(selection.getFirstElement());
		}
	}

	public abstract void handleObject(Object object);
}
