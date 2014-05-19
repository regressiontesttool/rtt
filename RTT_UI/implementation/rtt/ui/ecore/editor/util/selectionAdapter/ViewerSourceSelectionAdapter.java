package rtt.ui.ecore.editor.util.selectionAdapter;

import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.ecore.util.Messages;
import rtt.ui.viewer.ViewerUtils;

/**
 * A special {@link SelectionListener} which passes the
 * current selected object of a given {@link Viewer} to
 * the template method {@link #handleObject(Object)}.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public abstract class ViewerSourceSelectionAdapter extends SelectionAdapter {
	
	protected static class ToggleDialogCreator {
		public static final String DIALOG_TITLE = 
				"rtt.ecore.editor.selection.dialog.title";
		
		public static final String TOGGLE_MESSAGE = 
				"rtt.ecore.editor.selection.dialog.toggle";
		
		
		public static MessageDialogWithToggle createQuestion(
				Shell shell, String message) {
			
			return MessageDialogWithToggle.openYesNoCancelQuestion(shell, 
					Messages.getString(DIALOG_TITLE), message, 
					Messages.getString(TOGGLE_MESSAGE),	false, null, null);
		}
	}

	private Viewer viewer;
	
	/**
	 * Creates a new {@link ViewerSourceSelectionAdapter}.
	 * @param sourceViewer the viewer which will be used as source.
	 */
	public ViewerSourceSelectionAdapter(final Viewer sourceViewer) {
		
		if (sourceViewer == null) {
			throw new IllegalArgumentException("The viewer must not be null.");
		}
		
		this.viewer = sourceViewer;
	}
	
	@Override
	public final void widgetSelected(final SelectionEvent e) {
		Object selectedObject = ViewerUtils.getSelection(viewer.getSelection());
		if (selectedObject != null) {
			handleObject(selectedObject);
		}
		
		viewer.refresh();
		viewer.setSelection(viewer.getSelection());
	}
	
	/**
	 * Will be executed when the specified type of object was selected.
	 * @param object the selected object.
	 */
	public abstract void handleObject(Object object);	
}
