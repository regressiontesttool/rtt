package rtt.ui.viewer;

import org.eclipse.jface.viewers.LabelProvider;

import rtt.ui.content.IContent;

public class RttSimpleLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IContent) {
			return ((IContent) element).getText();
		}

		return super.getText(element);
	}

}
