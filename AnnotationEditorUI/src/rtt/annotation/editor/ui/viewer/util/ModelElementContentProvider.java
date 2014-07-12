package rtt.annotation.editor.ui.viewer.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ModelElement;

public class ModelElementContentProvider implements ITreeContentProvider {
	
	public static abstract class ItemProvider {
		Object parent;
		
		public ItemProvider(Object parent) {
			this.parent = parent;
		}
		
		protected ModelElementViewerItem createItem(Object parent, String... columns) {
			return new ModelElementViewerItem(parent, columns);
		}
		
		protected ModelElementViewerItem createItem(String... columns) {
			return createItem(parent, columns);
		}
		
		protected ModelElementViewerTree createTree(Object parent, String... columns) {
			return new ModelElementViewerTree(parent, columns);
		}
		
		protected ModelElementViewerTree createTree(String... columns) {
			return createTree(parent, columns);
		}	
		
		abstract List<ModelElementViewerItem> setInput(Object input);
	}
	
	private static final Object[] EMPTY_ARRAY = new Object[0];
	private ModelElementViewerTree mainItem = new ModelElementViewerTree(this);
	
	private Map<Class<?>, ItemProvider> providerMap;
	
	
	public ModelElementContentProvider() {
		providerMap = new HashMap<>();
		providerMap.put(ClassModel.class, new ClassModelItemProvider(mainItem));
		providerMap.put(ClassElement.class, new ClassElementItemProvider(mainItem));
	}	
	
	public void addProvider(Class<ModelElement<?>> modelClass, ItemProvider provider) {
		providerMap.put(modelClass, provider);
	}

	@Override
	public void dispose() {
		
	}

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
		if (providerMap.containsKey(newInput.getClass())) {
			ItemProvider provider = providerMap.get(newInput.getClass());
			List<ModelElementViewerItem> newData = provider.setInput(newInput);
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
		if (providerMap.containsKey(parentElement.getClass())) {
			return mainItem.items.toArray();
		}
		
		if (parentElement instanceof ModelElementViewerTree) {
			return ((ModelElementViewerTree) parentElement).items.toArray();
		}
		
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ModelElementViewerItem) {
			return ((ModelElementViewerItem) element).getParent();
		}
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
