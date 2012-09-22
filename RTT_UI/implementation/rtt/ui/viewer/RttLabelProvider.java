package rtt.ui.viewer;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import rtt.ui.content.IContent;

public class RttLabelProvider extends ColumnLabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof IContent) {
			IContent content = (IContent) element;
			return content.getText();
		}
		
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof IContent) {
			IContent content = (IContent) element;
			return content.getImage();
		}
		
		return super.getImage(element);
	}
	
	@Override
	public String getToolTipText(Object element) {
		if (element instanceof IContent) {
			return ((IContent) element).getToolTip();
		}
		
		return super.getToolTipText(element);
	}
	
	@Override
	public boolean useNativeToolTip(Object object) {
		return true;
	}
}