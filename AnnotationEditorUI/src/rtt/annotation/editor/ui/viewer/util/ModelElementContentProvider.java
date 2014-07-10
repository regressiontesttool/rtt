package rtt.annotation.editor.ui.viewer.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ModelElement;

public class ModelElementContentProvider implements ITreeContentProvider {
	
	public static interface ItemProvider {
		List<ModelElementViewerItem> setInput(Object input, Object parent);
	}
	
	private static final Object[] EMPTY_ARRAY = new Object[0];
	private ModelElementViewerTree mainItem = new ModelElementViewerTree(this);
	
	private Map<Class<?>, ItemProvider> providerMap;
	
	
	public ModelElementContentProvider() {
		providerMap = new HashMap<>();
		providerMap.put(ClassModel.class, new ClassModelItemProvider());
	}	
	
	public void addProvider(Class<ModelElement<?>> modelClass, ItemProvider provider) {
		providerMap.put(modelClass, provider);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		mainItem.items.clear();
		
		if (newInput != null && providerMap.containsKey(newInput.getClass())) {
			ItemProvider provider = providerMap.get(newInput.getClass());
			List<ModelElementViewerItem> newData = provider.setInput(newInput, mainItem);
			if (newData != null && !newData.isEmpty()) {
				mainItem.items = newData;
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
