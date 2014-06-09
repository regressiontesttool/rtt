package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;
import rtt.annotation.editor.model.ModelElement;

public class ClassViewerContentProvider implements ITreeContentProvider {

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
		Collection<? extends Object> results = new ArrayList<Object>(0);
		
		if (parentElement instanceof ClassModel) {
			results = ((ClassModel) parentElement).getPackages();
		}
		
		if (parentElement instanceof PackageElement) {
			PackageElement packageElement = (PackageElement) parentElement;
			results = packageElement.getParent().getClassElement(packageElement.getName());
		}
		
		if (parentElement instanceof ClassElement) {
			ClassElement classElement = (ClassElement) parentElement;
			
			List<String> details = new ArrayList<String>();
			details.add("Say Hello");

			results = details;
		}
		
		if (results != null) {
			return results.toArray();
		} else {
			return EMPTY_ARRAY;
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ModelElement<?>) {
			return ((ModelElement<?>) element).getParent();
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
