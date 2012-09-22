package rtt.ui.viewer;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import rtt.ui.content.IColumnableContent;
import rtt.ui.content.IContent;

public class RttColumnLabelProvider extends ColumnLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		
		if (element instanceof IColumnableContent) {
			IColumnableContent content = (IColumnableContent) element;
			cell.setText(content.getText(cell.getColumnIndex()));
			cell.setImage(content.getImage(cell.getColumnIndex()));
		} else if (element instanceof IContent) {
			IContent content = (IContent) element;
			if (cell.getColumnIndex() == 0) {
				cell.setText(content.getText());
				cell.setImage(content.getImage());
			}			
		}		
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