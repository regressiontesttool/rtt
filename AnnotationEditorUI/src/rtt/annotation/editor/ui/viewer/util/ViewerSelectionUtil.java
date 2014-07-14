package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import rtt.annotation.editor.model.ModelElement;

public class ViewerSelectionUtil {
	
	public static Object getObject(ISelection selection) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).getFirstElement();
		}

		return null;
	}
	
	public static ModelElement<?> getModelElement(ISelection selection) {
		Object selectedObject = getObject(selection);
		
		if (selectedObject instanceof ModelElement<?>) {
			return (ModelElement<?>) selectedObject;
		}
		
		if (selectedObject instanceof ViewerItem) {
			return ((ViewerItem) selectedObject).getModelElement();
		}
		
		return null;
	}

}
