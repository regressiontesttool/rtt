package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassModel.PackageElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class PropertyContentProvider implements ITreeContentProvider {
	
	protected class Property {
		protected Object parent = null;
		protected List<String> columns;
		
		public Property(String... columns) {
			this.columns = new ArrayList<String>();
			
			for (String column : columns) {
				this.columns.add(column);
			}
		}
		
		public String getColumn(int index) {
			if (index >= 0 && index < columns.size()) {
				return columns.get(index);
			}
			
			return null;
		}
	}
	
	protected class MultipleProperty extends Property {

		protected List<Property> items;

		public MultipleProperty(String label, List<Property> items) {
			super(label);	
			this.items = items;
		}
	}

	private static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Collection<Property> results = new ArrayList<Property>(0);
		
		if (parentElement instanceof PackageElement) {
			results = createPackageDetails((PackageElement) parentElement);
		}
		
		if (parentElement instanceof ClassElement) {
			results = createClassElementDetails((ClassElement) parentElement);
		}
		
		if (parentElement instanceof FieldElement) {
			results = createFieldElementDetails((FieldElement) parentElement);
		}
		
		if (parentElement instanceof MethodElement) {
			results = createMethodElementDetails((MethodElement) parentElement);
		}
		
		if (parentElement instanceof MultipleProperty) {
			results = ((MultipleProperty) parentElement).items;
		}
		
		if (results != null) {
			return results.toArray();
		} else {
			return EMPTY_ARRAY;
		}
	}

	private Collection<Property> createPackageDetails(
			PackageElement parentElement) {
		List<Property> results = new ArrayList<PropertyContentProvider.Property>();
		
		results.add(new Property("Name", parentElement.getName()));
		
		return results;
	}

	private Collection<Property> createClassElementDetails(
			ClassElement parentElement) {
		List<Property> results = new ArrayList<PropertyContentProvider.Property>();
		
		if (parentElement.hasAnnotation()) {
			results.add(new Property("Annotation", parentElement.getAnnotation().name()));
		}
		
		List<Property> classProperties = new ArrayList<Property>();
		classProperties.add(createClassNameProperty(parentElement));
		classProperties.add(new Property("Package", parentElement.getPackageName()));		
		
		if (parentElement.hasSuperClass()) {
			classProperties.add(new Property("Extends", parentElement.getSuperClass()));
		}
		
		if (parentElement.hasInterfaces()) {
			StringBuilder builder = null;
			for (String interfaceString : parentElement.getInterfaces()) {
				if (builder == null) {
					builder = new StringBuilder(interfaceString);
				} else {
					builder.append(",");
					builder.append(interfaceString);
				}				
			}
			classProperties.add(new Property("Implements", builder.toString()));
		}
		
		results.add(new MultipleProperty("Properties", classProperties));
		
		return results;
	}

	private Property createClassNameProperty(ClassElement parentElement) {
		StringBuilder builder = new StringBuilder();
		builder.append(parentElement.getName());
		
		if (parentElement.getType() != ClassType.CONCRETE) {
			builder.append(" (");
			builder.append(parentElement.getType().getText());
			builder.append(")");
		}
		
		return new Property("Type", builder.toString());
	}
	
	private Collection<Property> createFieldElementDetails(
			FieldElement parentElement) {
		
		List<Property> results = new ArrayList<PropertyContentProvider.Property>();
		
		if (parentElement.hasAnnotation()) {
			results.add(new Property("Annotation", parentElement.getAnnotation().name()));
		}
		
		List<Property> properties = new ArrayList<PropertyContentProvider.Property>();
		
		properties.add(new Property("Name", parentElement.getName()));
		properties.add(new Property("Type", parentElement.getType()));
		
		results.add(new MultipleProperty("Properties", properties));
		
		return results;
	}
	
	private Collection<Property> createMethodElementDetails(
			MethodElement parentElement) {
		
		List<Property> results = new ArrayList<PropertyContentProvider.Property>();
		
		if (parentElement.hasAnnotation()) {
			results.add(new Property("Annotation", parentElement.getAnnotation().name()));
		}
		
		List<Property> properties = new ArrayList<PropertyContentProvider.Property>();
		
		properties.add(new Property("Name", parentElement.getName()));
		properties.add(new Property("Return Type", parentElement.getType()));
		
		results.add(new MultipleProperty("Properties", properties));
		
		return results;
	}


	@Override
	public Object getParent(Object element) {
		if (element instanceof Property) {
			return ((Property) element).parent;
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
