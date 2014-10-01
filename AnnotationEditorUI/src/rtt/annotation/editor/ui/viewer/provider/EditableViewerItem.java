package rtt.annotation.editor.ui.viewer.provider;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
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
		return value;
	}
	
	public void setValue(Object value) {
		
		
		
		this.value = value;
	}

	public CellEditor getCellEditor(Composite parent) {
		if (value instanceof Boolean) {
			return new CheckboxCellEditor(parent);
		} 
		
		if (value instanceof Integer) {
			TextCellEditor editor = new TextCellEditor(parent) {
				@Override
				protected void doSetValue(Object value) {
					super.doSetValue(String.valueOf(value));
				}
				
				@Override
				protected Object doGetValue() {
					return Integer.parseInt(text.getText());				
				}
			};
			
			editor.setValidator(new ICellEditorValidator() {
				
				@Override
				public String isValid(Object value) {
					if (value instanceof Integer) {
						return null;
					}
					
					if (value instanceof String) {
						try {
							Integer.parseInt((String) value);
							return null;
						} catch (NumberFormatException e) {}
					}
					
					return "Not a valid number.";
				}
			});
			
			return editor;
		} 
		
		return new TextCellEditor(parent);
	}

}
