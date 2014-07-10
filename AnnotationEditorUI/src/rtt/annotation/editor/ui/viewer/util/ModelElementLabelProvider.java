package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class ModelElementLabelProvider extends ColumnLabelProvider {
	
	private int column = 0;
	
	protected ModelElementLabelProvider(int column) {
		this.column = column;
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof ModelElementViewerItem) {
			return ((ModelElementViewerItem) element).getColumnText(column);
		}
		
		return super.getText(element);
	}
	
	public static ColumnLabelProvider create(int column) {
		return new ModelElementLabelProvider(column);
	}
	
	
}
