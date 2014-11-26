package rtt.annotation.editor.ui.viewer.provider;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ElementReference;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public class PropertyViewerItemProvider extends ViewerItemProvider {
	
	List<ViewerItem> items = new ArrayList<>(0);

	@Override
	List<ViewerItem> setInput(Object input, ViewerItem parent) {
		items.clear();
		
		if (input instanceof ModelElement) {
			ModelElement element = (ModelElement) input;
			items.add(createModelElementItem(element, parent));
		}
		
		if (input instanceof Annotatable) {
			Annotatable<?> annotatable = (Annotatable<?>) input;
			if (annotatable.hasAnnotation()) {
				items.add(createAnnotatableItem(annotatable, parent));
			}			
		}
		
		if (input instanceof ClassElement) {
			ClassElement classElement = (ClassElement) input;
			items.addAll(createClassElementItems(classElement, parent));
		}
		
		if (input instanceof FieldElement) {
			FieldElement<?> field = (FieldElement<?>) input;
			items.add(createFieldElementItems(field, parent));
		}
		
		if (input instanceof MethodElement) {
			MethodElement<?> method = (MethodElement<?>) input;
			items.add(createMethodElementItems(method, parent));
		}
		
		if (items.isEmpty()) {
			items.add(new TextViewerItem(parent, "No properties found."));
		}
		
		return items;
	}

	private ViewerItem createModelElementItem(ModelElement element, ViewerItem parent) {
		return new TextViewerItem(parent, "Name", element.getName());
	}

	private ViewerItem createAnnotatableItem(final Annotatable<?> annotatable, ViewerItem parent) {
		final Annotation annotation = annotatable.getAnnotation();
		ViewerItem properties = new TextViewerItem(parent, "Annotation", annotation.getName());
		for (final String key : annotation.getKeys()) {
			EditableViewerItem viewerItem = new EditableViewerItem(
					properties, key, annotation.getAttribute(key)) {
				
				@Override
				public void setValue(Object changedValue) {
					if (!changedValue.equals(getValue())) {
						super.setValue(changedValue);
						annotation.setAttribute(key, getValue());
						annotatable.setChanged();
					}
				}
			};
			
			properties.add(viewerItem);
		}
		
		return properties;
	}

	private List<ViewerItem> createClassElementItems(ClassElement element, ViewerItem parent) {
		List<ViewerItem> items = new ArrayList<>();
		
		items.add(new TextViewerItem(parent, "Package", element.getPackageName()));
		
		if (element.hasSuperClass()) {
			ViewerItem item = new TextViewerItem(parent, "Extends", element.getSuperClass().getName());
			if (element.getSuperClass().isResolved()) {
				item = createReferenceItem(parent, "Extends", element.getSuperClass());
			}
			
			items.add(item);
		}
		
		if (element.hasInterfaces()) {
			ViewerItem interfaceItem = new TextViewerItem(parent, "Implements");
			for (ElementReference<ClassElement> reference : element.getInterfaces()) {
				ViewerItem item = new TextViewerItem(interfaceItem, "Interface", reference.getName());				
				if (reference.isResolved()) {
					item = createReferenceItem(interfaceItem, "Interface", reference);
				}
				
				interfaceItem.add(item);	
			}
			
			items.add(interfaceItem);
		}

		return items;
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
				
				if (element.hasAnnotatedValueMembers()) {
					return ItemColor.ANNOTATED_MEMBER;
				}						
				
				return super.getItemColor(element);
			}
		};
	}

	private ViewerItem createFieldElementItems(FieldElement<?> field, ViewerItem parent) {
		return new TextViewerItem(parent, "Type", field.getType());
	}

	private ViewerItem createMethodElementItems(MethodElement<?> method, ViewerItem parent) {
		return new TextViewerItem(parent, "Return Type", method.getType());
	}

	@Override
	boolean hasElements(Object parentElement) {
		return parentElement instanceof ModelElement;
	}

}
