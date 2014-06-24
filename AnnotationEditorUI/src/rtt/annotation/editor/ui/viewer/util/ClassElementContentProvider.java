package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.ModelElement;

public class ClassElementContentProvider implements ITreeContentProvider {
	
	protected class Detail {
		protected String label;
		
		public Detail(String label) {
			this.label = label;
		}
	}
	
	protected class MultipleDetail<T extends ModelElement<?>> extends Detail {

		protected String label;
		protected List<T> items;

		public MultipleDetail(String label, List<T> items) {
			super(label);
			this.items = items;
		}
	}
	
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
		Collection<? extends Object> results = new ArrayList<Object>(0);
		
		if (parentElement instanceof ClassElement) {
			List<Detail> details = new ArrayList<Detail>();			
			ClassElement classElement = (ClassElement) parentElement;
			
			details.add(createFieldElements(classElement.getFields()));
			details.add(createMethodElements(classElement.getMethods()));
			
			results = details;			
		}
		
		if (parentElement instanceof MultipleDetail) {
			results = ((MultipleDetail<?>) parentElement).items;			
		}
		
		return results.toArray();
	}

	private Detail createFieldElements(List<FieldElement> fields) {		
		if (fields != null && !fields.isEmpty()) {
			return new MultipleDetail<FieldElement>("Fields", fields);
		} else {
			return new Detail("No annotatable fields found.");
		}
	}

	private Detail createMethodElements(List<MethodElement> methods) {
		if (methods != null && !methods.isEmpty()) {
			return new MultipleDetail<MethodElement>("Methods", methods);
		} else {
			return new Detail("No annotatable methods found.");
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof MultipleDetail<?>) {
			return this;
		}	
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
