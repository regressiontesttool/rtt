package rtt.ui.ecore.editor.util.selectionAdapter;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.ecore.EcoreAnnotation;
import rtt.ui.ecore.EcoreController;
import rtt.ui.ecore.editor.EcoreEditor;
import rtt.ui.ecore.util.Messages;

/**
 * <p> This {@link SelectionListener} is used to remove {@link EcoreAnnotation}s from
 * an {@link EModelElement} selected within the {@link EcoreEditor#getLeftTreeViewer()}. <br />
 * Every {@link EcoreAnnotation} contained by children of the selected {@link EModelElement},
 * will removed, if the origin annotation is a {@link EcoreAnnotation#PARSER} 
 * or {@link EcoreAnnotation#NODE}.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class RemoveAnnotationSelectionAdapter extends ViewerSourceSelectionAdapter {
	
	public static final String REMOVE_MESSAGE = 
			"rtt.ecore.editor.selection.removeAnnotation.message";
	
	public static final String REMOVE_ALWAYS_OPTION = 
			"removeAlways";
	
	private Shell shell;
	private EcoreController controller;
	
	public RemoveAnnotationSelectionAdapter(EcoreEditor editor) {
		
		super(editor.getRightTreeViewer());
		this.shell = editor.getSite().getShell();
		this.controller = editor.getController();
	}
	
	@Override
	public void handleObject(Object object) {
		
		if (object instanceof EAnnotation) {
			EAnnotation annotation = (EAnnotation) object;
			EcoreAnnotation rttAnnotation = 
					EcoreAnnotation.convert(annotation);
			
			if (rttAnnotation != null) {
				EModelElement element = annotation.getEModelElement();
				
				boolean doRemove = true;
				if (rttAnnotation == EcoreAnnotation.NODE
						|| rttAnnotation == EcoreAnnotation.PARSER) {
					doRemove = checkChildren((EClass) element, rttAnnotation);
				}
				
				if (doRemove) {
					controller.removeAnnotation(element, rttAnnotation);
				}
			}			
		}
	}

	private boolean checkChildren(EClass classObject, EcoreAnnotation rttAnnotation) {
		if (classObject != null && 
				EcoreAnnotation.areContainedByChilds(classObject, rttAnnotation)) {
			
			Boolean alwaysRemove = controller.getOption(REMOVE_ALWAYS_OPTION);
			
			if (alwaysRemove != null) {
				return alwaysRemove;
			}
			
			// if alwaysRemove == null then the user 
			// hasn't used the toggle option -> open dialog
			
			MessageDialogWithToggle dialog = ToggleDialogCreator.createQuestion(
					shell, Messages.getString(REMOVE_MESSAGE, rttAnnotation.getToken()));
			
			int returnCode = dialog.getReturnCode();
			boolean doRemove = returnCode == IDialogConstants.YES_ID;
			
			if (returnCode != IDialogConstants.CANCEL_ID
					&& dialog.getToggleState() == true) {
				controller.addToOptions(REMOVE_ALWAYS_OPTION, doRemove);						
			}
			
			return doRemove;			
		}
		
		return true;
	}
	

}
