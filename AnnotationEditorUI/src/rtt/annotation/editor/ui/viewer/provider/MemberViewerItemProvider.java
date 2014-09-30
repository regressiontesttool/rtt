package rtt.annotation.editor.ui.viewer.provider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.ui.AnnotationEditor;

public class MemberViewerItemProvider extends ViewerItemProvider {
	
	private final class MethodElementItem extends ModelElementViewerItem<MethodElement<?>> {
		private MethodElementItem(ViewerItem parent, MethodElement<?> element) {
			super(parent, element);
		}

		@Override
		protected String getColumnText(MethodElement<?> element, int columnIndex) {
			
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
		protected ItemColor getItemColor(MethodElement<?> element) {
			Annotation annotation = element.getAnnotation();
			if (annotation != null) {
				if (annotation.getType() == AnnotationType.VALUE) {
					return ItemColor.VALUE;
				}
				
				if (annotation.getType() == AnnotationType.INITIALIZE) {
					return ItemColor.INITIALIZE;
				}
			}			
			
			return super.getItemColor(element);
		}
		
		@Override
		protected ItemFont getItemFont(MethodElement<?> element) {
			if (element.hasAnnotation()) {
				return ItemFont.BOLD_FONT;
			}
			
			return super.getItemFont(element);
		}
	}

	private final class FieldElementItem extends ModelElementViewerItem<FieldElement<?>> {
		private FieldElementItem(ViewerItem parent, FieldElement<?> element) {
			super(parent, element);
		}

		@Override
		protected String getColumnText(FieldElement<?> element, int columnIndex) {					
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
		protected ItemColor getItemColor(FieldElement<?> element) {
			Annotation annotation = element.getAnnotation();
			if (annotation != null) {
				if (annotation.getType() == AnnotationType.VALUE) {
					return ItemColor.VALUE;
				}
				
				if (annotation.getType() == AnnotationType.INITIALIZE) {
					return ItemColor.INITIALIZE;
				}
			}
			
			return super.getItemColor(element);
		}
		
		@Override
		protected ItemFont getItemFont(FieldElement<?> element) {
			if (element.hasAnnotation()) {
				return ItemFont.BOLD_FONT;
			}
			
			return super.getItemFont(element);
		}
	}

	List<ViewerItem> items = new ArrayList<>(2);
	
	ViewerItem fieldTree;
	ViewerItem methodTree;
	
	private AnnotationEditor editor;
	
	public MemberViewerItemProvider(AnnotationEditor editor) {
		fieldTree = new TextViewerItem(null, "Fields");
		methodTree = new TextViewerItem(null, "Methods");
		
		items.add(fieldTree);
		items.add(methodTree);
		
		this.editor = editor;
	}
	
	@Override
	public List<ViewerItem> setInput(Object input, ViewerItem parent) {
		ClassElement classElement = (ClassElement) input;
		
		List<FieldElement<?>> fields = new LinkedList<>();
		List<MethodElement<?>> methods = new LinkedList<>();
		
		switch (editor.getSelectedAnnotation()) {
		case INITIALIZE:
			methods.addAll(classElement.getInitializableMethods());
			break;
		case VALUE:
			fields.addAll(classElement.getValuableFields());
			methods.addAll(classElement.getValuableMethods());
			break;
		default:
			break;		
		}
		
		updateFieldItems(fields, parent);
		updateMethodItems(methods, parent);		

		return items;
	}

	private void updateFieldItems(List<FieldElement<?>> fields, ViewerItem parent) {	
		fieldTree.setParent(parent);
		fieldTree.clear();
		
		for (FieldElement<?> field : fields) {
			fieldTree.add(new FieldElementItem(fieldTree, field));		
		}
		
		if (fieldTree.isEmpty()) {
			fieldTree.add(new TextViewerItem(fieldTree, "No annotatable elements."));
		}	
	}

	private void updateMethodItems(List<MethodElement<?>> methods, ViewerItem parent) {
		methodTree.setParent(parent);
		methodTree.clear();
		
		for (MethodElement<?> method : methods) {
			methodTree.add(new MethodElementItem(methodTree, method));
		}
		
		if (methodTree.isEmpty()) {
			methodTree.add(new TextViewerItem(methodTree, "No annotatable elements."));
		}
	}

	@Override
	boolean hasElements(Object parentElement) {
		return parentElement instanceof ClassElement;
	}
}
