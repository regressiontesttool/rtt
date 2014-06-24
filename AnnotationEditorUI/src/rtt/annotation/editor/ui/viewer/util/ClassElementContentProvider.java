package rtt.annotation.editor.ui.viewer.util;

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
		
		public MultipleDetail(String label) {
			super(label);
		}
		
	}

	private static final Object[] EMPTY_RESULT = new Object[0];
	
	private MultipleDetail<FieldElement> fields = new MultipleDetail<FieldElement>("Fields");
	private MultipleDetail<MethodElement> methods = new MultipleDetail<MethodElement>("Methods");
	private Detail[] details = new Detail[] {fields, methods};
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof ClassElement) {
			ClassElement classElement = (ClassElement) newInput;
			fields.items = classElement.getFields();
			methods.items = classElement.getMethods();
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ClassElement) {
			return details;			
		}
		
		if (parentElement instanceof MultipleDetail<?>) {
			List<?> items = ((MultipleDetail<?>) parentElement).items;
			if (items == null || items.isEmpty()) {
				return new Object[] {new Detail("No annotatable element found.")};
			} else {
				return ((MultipleDetail<?>) parentElement).items.toArray();
			}						
		}
		
		return EMPTY_RESULT;
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
