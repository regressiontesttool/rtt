package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import rtt.annotation.editor.ui.viewer.util.PropertyContentProvider.Property;

public class PropertyColumnLabelProvider extends ColumnLabelProvider {
	
	public static final int DESCRIPTION_COLUMN = 0;
	public static final int VALUE_COLUMN = 1;
	
	private int columnIndex = DESCRIPTION_COLUMN;
	
	public PropertyColumnLabelProvider(int column) {
		if (column < 0) {
			throw new IllegalArgumentException("Index must be greater than zero.");
		}
		
		this.columnIndex = column;
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof Property) {
			Property property = (Property) element;
			return property.getColumn(columnIndex);
		}

		return super.getText(element);
	}

}
