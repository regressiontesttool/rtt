package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ClassElementColumnLabelProvider extends ColumnLabelProvider {
	
	public enum ColumnKey {
		KIND_COLUMN, NAME_COLUMN, TYPE_COLUMN;
	}
	
	private ColumnKey column = ColumnKey.NAME_COLUMN;
	
	public ClassElementColumnLabelProvider(ColumnKey column) {
		this.column = column;
	}
	
	@Override
	public String getText(Object element) {
		String text = null;
		switch (column) {
		case KIND_COLUMN:
			text = getKind(element);
			break;
			
		case NAME_COLUMN:
			text = getName(element);
			break;
			
		case TYPE_COLUMN:
			text = getType(element);
			break;
		}
		
		if (text != null && !text.equals("")) {
			return text;
		} else {
			return super.getText(element);
		}	
	}

	private String getKind(Object element) {
		if (element instanceof FieldElement) {
			return "Field";
		}
		
		if (element instanceof MethodElement) {
			return "Method";
		}
		
		return null;		
	}

	private String getName(Object element) {
		if (element instanceof FieldElement) {
			return ((FieldElement) element).getName();
		}
		
		if (element instanceof MethodElement) {
			return ((MethodElement) element).getName();
		}
		
		return null;
	}

	private String getType(Object element) {
		if (element instanceof FieldElement) {
			return ((FieldElement) element).getClassName();
		}
		
		if (element instanceof MethodElement) {
			return ((MethodElement) element).getClassName();
		}
		
		return null;
	}
	
	

}
