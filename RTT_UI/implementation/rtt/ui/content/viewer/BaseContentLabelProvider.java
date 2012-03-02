package rtt.ui.content.viewer;

import org.eclipse.jface.viewers.LabelProvider;

import rtt.ui.content.IContent;

public class BaseContentLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IContent) {
			return ((IContent) element).getText();
		}

		return super.getText(element);
	}

}