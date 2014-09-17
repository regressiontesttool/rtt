package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class EditableViewerItem extends ViewerItem {

	private String description;
	private Object value;

	public EditableViewerItem(ViewerItem parent, String description, Object value) {
		super(parent);
		this.description = description;
		this.value = value;
	}

	@Override
	protected String getColumnText(int columnIndex) {
		if (columnIndex == 0) {
			return description;
		} else if (columnIndex == 1) {
			return String.valueOf(value);
		}
		
		return "";		
	}
	
	public String getDescription() {
		return description;
	}

	public Object getValue() {
		if (value instanceof Boolean) {
			return value;
		}
		
		return String.valueOf(value);
	}

	public void setValue(Object changedValue) {
		String newValue = String.valueOf(changedValue);
		
		try {
			if (value instanceof Boolean) {
				value = Boolean.valueOf(newValue);
			} else if (value instanceof Integer) {
				value = Integer.valueOf(newValue);
			} else {
				value = newValue;
			}
		} catch (Exception e) {}
	}

	public CellEditor getCellEditor(Composite parent) {
		if (value instanceof Boolean) {
			return new CheckboxCellEditor(parent);
		}
		
		return new TextCellEditor(parent);
	}

}
