package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

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
	
	@Override
	public Color getForeground(Object element) {
		if (element instanceof ViewerItem) {
			return ((ViewerItem) element).getForeground();
		}
		
		return super.getForeground(element);
	}
	
	@Override
	public Font getFont(Object element) {
		if (element instanceof ViewerItem) {
			return ((ViewerItem) element).getFont();
		}
		
		return super.getFont(element);
	}
	
}
