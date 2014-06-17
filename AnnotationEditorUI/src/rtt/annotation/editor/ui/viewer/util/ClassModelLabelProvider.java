package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;
import rtt.annotation.editor.ui.viewer.util.ClassModelContentProvider.Detail;

public class ClassModelLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof ClassModel) {
			return ((ClassModel) element).toString();
		}
		
		if (element instanceof PackageElement) {
			return ((PackageElement) element).getName();
		}
		
		if (element instanceof ClassElement) {
			ClassElement classElement = (ClassElement) element;
			
			StringBuilder builder = new StringBuilder();
			builder.append(classElement.getName());
			
			switch (classElement.getType()) {
			case INTERFACE:
				builder.append(" [I]");
				break;
				
			case ABSTRACT:
				builder.append(" [A]");
				break;
				
			default:
				break;
			}
	
			return builder.toString();
		}
		
		if (element instanceof Detail) {
			return ((Detail) element).label;
		}
		
		return super.getText(element);
	}
}
