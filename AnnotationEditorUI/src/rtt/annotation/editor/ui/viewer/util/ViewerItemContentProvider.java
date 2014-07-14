package rtt.annotation.editor.ui.viewer.util;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ViewerItemContentProvider implements ITreeContentProvider {
	
	private ViewerItemProvider provider;
	
	private static final Object[] EMPTY_ARRAY = new Object[0];
	private ViewerItem mainItem = new TextViewerItem(null);
		
	protected ViewerItemContentProvider(ViewerItemProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			mainItem.clear();
		} else {
			if (!newInput.equals(oldInput)) {
				mainItem.clear();
				createItems(newInput);
			}
		}		
	}

	private void createItems(Object newInput) {
		if (provider.hasRoot(newInput)) {
			List<ViewerItem> newData = provider.setInput(newInput, mainItem);
			if (newData != null && !newData.isEmpty()) {
				mainItem.addAll(newData);
			}
		}		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {		
		if (parentElement instanceof ViewerItem) {
			return ((ViewerItem) parentElement).getChildren().toArray();
		}
		
		if (provider.hasRoot(parentElement)) {
			return mainItem.getChildren().toArray();
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
