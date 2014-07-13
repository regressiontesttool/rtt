package rtt.annotation.editor.ui.viewer.util;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.annotation.editor.ui.viewer.util.ViewerItemProvider.ViewerItem;
import rtt.annotation.editor.ui.viewer.util.ViewerItemProvider.ViewerTree;

public class ViewerItemContentProvider implements ITreeContentProvider {
	
	private ViewerItemProvider provider;
	
	private static final Object[] EMPTY_ARRAY = new Object[0];
	private ViewerTree mainItem = new ViewerTree(this);
		
	protected ViewerItemContentProvider(ViewerItemProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			mainItem.items.clear();
		} else {
			if (!newInput.equals(oldInput)) {
				mainItem.items.clear();
				createItems(newInput);
			}
		}		
	}

	private void createItems(Object newInput) {
		if (provider.hasRoot(newInput)) {
			List<ViewerItem> newData = provider.setInput(newInput);
			if (newData != null && !newData.isEmpty()) {
				mainItem.items.addAll(newData);
			}
		}		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {		
		if (parentElement instanceof ViewerTree) {
			return ((ViewerTree) parentElement).items.toArray();
		}
		
		if (provider.hasRoot(parentElement)) {
			return mainItem.items.toArray();
		}
		
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ViewerItem) {
			return ((ViewerItem) element).getParent();
		}
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
