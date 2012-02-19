package rtt.ui.core.internal.treeItem;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.ui.core.IModelObserver;
import rtt.ui.core.ITreeItem;
import rtt.ui.core.ProjectRegistry;

public class TreeItemContentProvider implements ITreeContentProvider {
	
	private IModelObserver observer;
	
	public TreeItemContentProvider(IModelObserver observer) {
		this.observer = observer;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		
		if (parentElement instanceof ITreeItem) {
			return ((ITreeItem) parentElement).getChildren();
		}			
		
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		
		if (element instanceof ITreeItem) {
			ITreeItem treeItem = (ITreeItem) element;
			return treeItem.getParent();
		}
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (observer != null) {
			if (oldInput == null && newInput != null) {				
				ProjectRegistry.addObserver(observer);
			}
			
			if (newInput == null && oldInput != null) {
				ProjectRegistry.removeObserver(observer);
			}
		}
		
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

}
