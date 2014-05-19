package rtt.ui.viewer;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ViewerUtils {
	
	public static Object getSelection(ISelection selection)  {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();
			
			if (selectedObject != null) {
				return selectedObject;
			}		
		}
		
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSelection(ISelection selection, Class<T> elementClass) {
		Object object = getSelection(selection);
		if (object != null && elementClass.isInstance(object)) {
			return (T) object;
		}
		
		return null;
	}

}
