package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.ui.viewer.util.ModelElementContentProvider.ItemProvider;

public class ClassElementItemProvider extends ItemProvider {
	
	List<ModelElementViewerItem> items = new ArrayList<>(2);
	
	ModelElementViewerTree fieldTree;
	ModelElementViewerTree methodTree;
	
	public ClassElementItemProvider(Object parent) {
		super(parent);
		fieldTree = createTree("Fields");
		methodTree = createTree("Methods");
		
		items.add(fieldTree);
		items.add(methodTree);
	}
	
	@Override
	public List<ModelElementViewerItem> setInput(Object input) {
		ClassElement classElement = (ClassElement) input;
		
		updateFieldItems(classElement.getFields());
		updateMethodItems(classElement.getMethods());

		return items;
	}

	private void updateFieldItems(List<FieldElement> fields) {		
		fieldTree.clear();
		
		for (FieldElement field : fields) {
			ModelElementViewerItem fieldItem = createItem(fieldTree, field.getName(), field.getType());
			fieldItem.setModelElement(field);
			
			fieldTree.addItem(fieldItem);
		}
		
		if (fieldTree.items.isEmpty()) {
			fieldTree.addItem(new ModelElementViewerItem(fieldTree, "No annotatable elements."));
		}	
	}

	private void updateMethodItems(List<MethodElement> methods) {
		methodTree.clear();
		
		for (MethodElement method : methods) {
			ModelElementViewerItem fieldItem = createItem(methodTree, method.getName(), method.getType());
			fieldItem.setModelElement(method);
			
			methodTree.addItem(fieldItem);
		}
		
		if (methodTree.items.isEmpty()) {
			methodTree.addItem(new ModelElementViewerItem(methodTree, "No annotatable elements."));
		}
	}

}
