package rtt.ui.viewer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.ui.content.IContent;

public class RttStructuredContentProvider implements IStructuredContentProvider {

	public static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[]) {
			return ((Object[]) inputElement);
		}

		if (inputElement instanceof IContent) {
			return ((IContent) inputElement).getChildren();
		}

		return EMPTY_ARRAY;
	}
}
