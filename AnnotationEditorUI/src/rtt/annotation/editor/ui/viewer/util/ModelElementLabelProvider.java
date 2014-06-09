package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import rtt.annotation.editor.model.ModelElement;

public class ModelElementLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof ModelElement) {
			return ((ModelElement) element).getLabel();
		}

		return super.getText(element);
	}

}
