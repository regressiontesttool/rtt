package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider.Detail;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider.MultipleDetail;

public class ClassElementColumnLabelProvider extends ColumnLabelProvider {
	
	private static final Color COMPARE_COLOR = JFaceResources.getColorRegistry().get(AnnotationEditorPlugin.COMPARE_COLOR);
	private static final Color INFORMATIONAL_COLOR = JFaceResources.getColorRegistry().get(AnnotationEditorPlugin.INFORMATIONAL_COLOR);
	
	private static final Font DEFAULT_FONT = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
	private static final Font BOLD_FONT = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
	
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

		if (element instanceof ModelElement<?>) {
			return ((ModelElement<?>) element).getName();
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
	
	@Override
	public Color getForeground(Object element) {
		if (element instanceof Annotatable<?>) {
			Annotatable<?> annotatable = (Annotatable<?>) element;
			if (annotatable.getAnnotation() == Annotation.COMPARE) {
				return COMPARE_COLOR;
			}
			
			if (annotatable.getAnnotation() == Annotation.INFORMATIONAL) {
				return INFORMATIONAL_COLOR;
			}
		}
		
		return null;
	}
	
	@Override
	public Font getFont(Object element) {
		if (element instanceof Annotatable<?>) {
			Annotatable<?> annotatable = (Annotatable<?>) element;
			if (annotatable.hasAnnotation()) {
				return BOLD_FONT;
			}
		}
		
		return DEFAULT_FONT;
	}
}
