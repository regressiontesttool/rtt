package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider.Detail;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider.MultipleDetail;

public class ClassElementColumnLabelProvider extends ColumnLabelProvider {
	
	public static final int DESCRIPTION_COLUMN = 0;
	public static final int TYPE_COLUMN = 1;
	
	private int columnIndex = DESCRIPTION_COLUMN;
	
	public ClassElementColumnLabelProvider(int column) {
		this.columnIndex = column;
	}
	
	@Override
	public String getText(Object element) {
		if (columnIndex == DESCRIPTION_COLUMN) {
			return getDescriptionColumn(element);
		}
		
		if (columnIndex == TYPE_COLUMN) {
			return getTypeColumn(element);
		}

		return super.getText(element);
	}

	private String getDescriptionColumn(Object element) {
		if (element instanceof Detail) {
			return ((Detail) element).label;
		}
		
		if (element instanceof MultipleDetail<?>) {
			return ((MultipleDetail<?>) element).label;
		}

		if (element instanceof FieldElement) {
			return ((FieldElement) element).getName();
		}
		
		if (element instanceof MethodElement) {
			return ((MethodElement) element).getName();
		}
		
		return "";
	}

	private String getTypeColumn(Object element) {
		if (element instanceof FieldElement) {
			return ((FieldElement) element).getType();
		}
		
		if (element instanceof MethodElement) {
			return ((MethodElement) element).getType();
		}
		
		return "";
	}

}
