package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ClassElementItemProvider extends ViewerItemProvider {
	
	List<ViewerItem> items = new ArrayList<>(2);
	
	ViewerTree fieldTree;
	ViewerTree methodTree;
	
	public ClassElementItemProvider() {
		fieldTree = new ViewerTree(this, "Fields");
		methodTree = new ViewerTree(this, "Methods");
		
		items.add(fieldTree);
		items.add(methodTree);
	}
	
	@Override
	public List<ViewerItem> setInput(Object input) {
		ClassElement classElement = (ClassElement) input;
		
		updateFieldItems(classElement.getFields());
		updateMethodItems(classElement.getMethods());

		return items;
	}

	private void updateFieldItems(List<FieldElement> fields) {		
		fieldTree.clear();
		
		for (FieldElement field : fields) {
			ViewerItem fieldItem = createItem(fieldTree, field.getName(), field.getType());
			fieldItem.setModelElement(field);
			
			fieldTree.addItem(fieldItem);
		}
		
		if (fieldTree.items.isEmpty()) {
			fieldTree.addItem(new ViewerItem(fieldTree, "No annotatable elements."));
		}	
	}

	private void updateMethodItems(List<MethodElement> methods) {
		methodTree.clear();
		
		for (MethodElement method : methods) {
			ViewerItem fieldItem = createItem(methodTree, method.getName(), method.getType());
			fieldItem.setModelElement(method);
			
			methodTree.addItem(fieldItem);
		}
		
		if (methodTree.items.isEmpty()) {
			methodTree.addItem(new ViewerItem(methodTree, "No annotatable elements."));
		}
	}

	@Override
	boolean hasRoot(Object parentElement) {
		return parentElement.getClass().equals(ClassElement.class);
	}
}
