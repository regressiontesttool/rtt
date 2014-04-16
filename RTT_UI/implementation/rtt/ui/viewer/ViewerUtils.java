package rtt.ui.viewer;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ViewerUtils {
	
	public static Object getSelection(ISelection selection)  {
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			Object object = sSelection.getFirstElement();
			if (object != null) {
				return object;
			}
		}
		
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends Object>  E getSelection(ISelection selection, Class<E> clazz) {
		Object object = getSelection(selection);
		if (object != null && object.getClass().equals(clazz)) {
			return (E) object;
		}
		
		return null;
	}

}
