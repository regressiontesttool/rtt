package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import rtt.annotation.editor.ui.viewer.util.ViewerItemProvider.ViewerItem;

public class ViewerItemLabelProvider extends ColumnLabelProvider {
	
	private int column = 0;
	
	protected ViewerItemLabelProvider(int column) {
		this.column = column;
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof ViewerItem) {
			return ((ViewerItem) element).getColumnText(column);
		}
		
		return super.getText(element);
	}
	
	public static ColumnLabelProvider create(int column) {
		return new ViewerItemLabelProvider(column);
	}
	
	
}
