package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ElementViewerItemProvider extends ViewerItemProvider {
	
	private final class MethodElementItem extends
			ModelElementViewerItem<MethodElement> {
		private MethodElementItem(ViewerItem parent, MethodElement element) {
			super(parent, element);
		}

		@Override
		protected String getColumnText(MethodElement element, int columnIndex) {
			
			switch (columnIndex) {
			case FIRST_COLUMN:
				return element.getName();
				
			case SECOND_COLUMN:
				return element.getType();
				
			default:
				return super.getColumnText(element, columnIndex);
			}
		}

		@Override
		protected ItemColor getItemColor(MethodElement element) {
			if (element.getAnnotation() == Annotation.COMPARE) {
				return ItemColor.COMPARE;
			}
			
			if (element.getAnnotation() == Annotation.INFORMATIONAL) {
				return ItemColor.INFORMATIONAL;
			}
			
			return super.getItemColor(element);
		}
		
		@Override
		protected ItemFont getItemFont(MethodElement element) {
			if (element.hasAnnotation()) {
				return ItemFont.BOLD_FONT;
			}
			
			return super.getItemFont(element);
		}
	}

	private final class FieldElementItem extends
			ModelElementViewerItem<FieldElement> {
		private FieldElementItem(ViewerItem parent, FieldElement element) {
			super(parent, element);
		}

		@Override
		protected String getColumnText(FieldElement element, int columnIndex) {					
			switch (columnIndex) {
				case FIRST_COLUMN:
					return element.getName();
					
				case SECOND_COLUMN:
					return element.getType();
					
				default:
					return super.getColumnText(element, columnIndex);
			}
		}

		@Override
		protected ItemColor getItemColor(FieldElement element) {
			if (element.getAnnotation() == Annotation.COMPARE) {
				return ItemColor.COMPARE;
			}
			
			if (element.getAnnotation() == Annotation.INFORMATIONAL) {
				return ItemColor.INFORMATIONAL;
			}
			
			return super.getItemColor(element);
		}
		
		@Override
		protected ItemFont getItemFont(FieldElement element) {
			if (element.hasAnnotation()) {
				return ItemFont.BOLD_FONT;
			}
			
			return super.getItemFont(element);
		}
	}

	List<ViewerItem> items = new ArrayList<>(2);
	
	ViewerItem fieldTree;
	ViewerItem methodTree;
	
	public ElementViewerItemProvider() {
		fieldTree = new TextViewerItem(null, "Fields");
		methodTree = new TextViewerItem(null, "Methods");
		
		items.add(fieldTree);
		items.add(methodTree);
	}
	
	@Override
	public List<ViewerItem> setInput(Object input, ViewerItem parent) {
		ClassElement classElement = (ClassElement) input;
		
		updateFieldItems(classElement.getFields(), parent);
		updateMethodItems(classElement.getMethods(), parent);

		return items;
	}

	private void updateFieldItems(List<FieldElement> fields, ViewerItem parent) {		
		fieldTree.setParent(parent);
		fieldTree.clear();	
		
		for (FieldElement field : fields) {
			fieldTree.add(new FieldElementItem(fieldTree, field));		
		}
		
		if (fieldTree.isEmpty()) {
			fieldTree.add(new TextViewerItem(fieldTree, "No annotatable elements."));
		}	
	}

	private void updateMethodItems(List<MethodElement> methods, ViewerItem parent) {
		methodTree.setParent(parent);
		methodTree.clear();
		
		for (MethodElement method : methods) {
			methodTree.add(new MethodElementItem(methodTree, method));
		}
		
		if (methodTree.isEmpty()) {
			methodTree.add(new TextViewerItem(methodTree, "No annotatable elements."));
		}
	}

	@Override
	boolean hasRoot(Object parentElement) {
		return parentElement.getClass().equals(ClassElement.class);
	}
}
