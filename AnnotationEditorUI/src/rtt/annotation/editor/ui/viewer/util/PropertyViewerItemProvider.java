package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ElementReference;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.ModelElement;

public class PropertyViewerItemProvider extends ViewerItemProvider {
	
	List<ViewerItem> items = new ArrayList<>(0);

	@Override
	List<ViewerItem> setInput(Object input, ViewerItem parent) {
		items.clear();
		
		if (input instanceof ModelElement<?>) {
			ModelElement<?> element = (ModelElement<?>) input;
			items.add(createModelElementItem(element, parent));
		}
		
		if (input instanceof Annotatable<?>) {
			Annotatable<?> annotatable = (Annotatable<?>) input;
			if (annotatable.hasAnnotation()) {
				items.add(createAnnotatableItem(annotatable, parent));
			}			
		}
		
		if (input instanceof ClassElement) {
			ClassElement classElement = (ClassElement) input;
			items.add(createClassElementItems(classElement, parent));
		}
		
		if (input instanceof FieldElement) {
			FieldElement field = (FieldElement) input;
			items.add(createFieldElementItems(field, parent));
		}
		
		if (input instanceof MethodElement) {
			MethodElement method = (MethodElement) input;
			items.add(createMethodElementItems(method, parent));
		}
		
		if (items.isEmpty()) {
			items.add(new TextViewerItem(parent, "No properties found."));
		}
		
		return items;
	}

	private ViewerItem createModelElementItem(ModelElement<?> element, ViewerItem parent) {
		return new TextViewerItem(parent, "Name", element.getName());
	}

	private ViewerItem createAnnotatableItem(Annotatable<?> annotatable, ViewerItem parent) {
		Annotation annotation = annotatable.getAnnotation();
		ViewerItem properties = new TextViewerItem(parent, "Annotation", annotation.getPrettyName());
		for (Entry<String, Object> attributes : annotation.getAttributes().entrySet()) {
			properties.add(new TextViewerItem(properties, attributes.getKey(), 
					String.valueOf(attributes.getValue())));
		}
		
		return properties;
	}

	private ViewerItem createClassElementItems(ClassElement element, ViewerItem parent) {
		ViewerItem properties = new TextViewerItem(parent, "Properties");
		
		properties.add(new TextViewerItem(properties, "Package", element.getPackageName()));
		
		if (element.hasSuperClass()) {
			ViewerItem item = new TextViewerItem(properties, "Extends", element.getSuperClass().getName());
			if (element.getSuperClass().isResolved()) {
				item = createReferenceItem(properties, "Extends", element.getSuperClass());
			}
			
			properties.add(item);
		}
		
		if (element.hasInterfaces()) {
			ViewerItem interfaceItem = new TextViewerItem(properties, "Implements");
			for (ElementReference<ClassElement> reference : element.getInterfaces()) {
				ViewerItem item = new TextViewerItem(interfaceItem, "Interface", reference.getName());				
				if (reference.isResolved()) {
					item = createReferenceItem(interfaceItem, "Interface", reference);
				}
				
				interfaceItem.add(item);	
			}
			
			properties.add(interfaceItem);
		}

		return properties;
	}

	private ViewerItem createReferenceItem(ViewerItem parent, final String text, ElementReference<ClassElement> classReference) {
		return new ModelElementViewerItem<ClassElement>(parent, classReference.getReference()) {
			@Override
			protected String getColumnText(ClassElement element,
					int columnIndex) {
				if (columnIndex == ViewerItemProvider.FIRST_COLUMN) {
					return text;
				}
				
				if (columnIndex == ViewerItemProvider.SECOND_COLUMN) {
					return element.getPackageName() + "." + element.getName();
				}						
				
				return super.getColumnText(element, columnIndex);
			}
			
			@Override
			protected ItemColor getItemColor(ClassElement element) {
				if (element.hasAnnotation()) {
					return ItemColor.ANNOTATED;
				}
				
				if (element.hasExtendedAnnotation()) {
					return ItemColor.ANNOTATED_EXTEND;
				}
				
				if (element.hasAnnotatedValueMember()) {
					return ItemColor.ANNOTATED_MEMBER;
				}						
				
				return super.getItemColor(element);
			}
		};
	}

	private ViewerItem createFieldElementItems(FieldElement field, ViewerItem parent) {
		ViewerItem properties = new TextViewerItem(parent, "Properties");
		properties.add(new TextViewerItem(properties, "Type", field.getType()));

		return properties;
	}

	private ViewerItem createMethodElementItems(MethodElement method, ViewerItem parent) {
		ViewerItem properties = new TextViewerItem(parent, "Properties");
		properties.add(new TextViewerItem(properties, "Return Type", method.getType()));
		
		return properties;
	}

	@Override
	boolean hasElements(Object parentElement) {
		return parentElement instanceof ModelElement<?>;
	}

}
