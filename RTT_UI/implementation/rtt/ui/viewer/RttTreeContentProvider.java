package rtt.ui.viewer;

import org.eclipse.jface.viewers.ITreeContentProvider;

import rtt.ui.content.IContent;

public class RttTreeContentProvider extends RttStructuredContentProvider implements
		ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IContent) {
			return ((IContent) element).getParent();
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IContent) {
			return ((IContent) element).hasChildren();
		}

		return false;
	}
}