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
	
	protected class MultipleDetail<T extends ModelElement<?>> {

		protected String label;
		protected List<T> items;

		public MultipleDetail(String label, List<T> items) {
			this.label = label;	
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
			List<MultipleDetail<?>> details = new ArrayList<MultipleDetail<?>>();			
			ClassElement classElement = (ClassElement) parentElement;
			
			details.add(new MultipleDetail<FieldElement>("Fields", classElement.getFields()));
			details.add(new MultipleDetail<MethodElement>("Methods", classElement.getMethods()));
			
			results = details;			
		}
		
		if (parentElement instanceof MultipleDetail) {
			results = ((MultipleDetail<?>) parentElement).items;			
		}
		
		return results.toArray();
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
