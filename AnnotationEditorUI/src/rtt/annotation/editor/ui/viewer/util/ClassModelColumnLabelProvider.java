package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ModelElement;

public class ClassModelColumnLabelProvider extends ColumnLabelProvider {

	private static final Color ANNOTATED_COLOR = JFaceResources.getColorRegistry().get(AnnotationEditorPlugin.ANNOTATED_COLOR);
	private static final Color EXTENDED_COLOR = JFaceResources.getColorRegistry().get(AnnotationEditorPlugin.EXTENDED_COLOR);
	private static final Color MEMBER_COLOR = JFaceResources.getColorRegistry().get(AnnotationEditorPlugin.MEMBER_COLOR);
	
	private static final Font DEFAULT_FONT = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
	private static final Font BOLD_FONT = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
	
	@Override
	public String getText(Object element) {
		if (element instanceof ClassModel) {
			return ((ClassModel) element).toString();
		}
		
		if (element instanceof ModelElement<?>) {
			return ((ModelElement<?>) element).getName();
		}
		
		return super.getText(element);
	}

	@Override
	public Color getForeground(Object element) {
		if (element instanceof ClassElement) {
			ClassElement classElement = (ClassElement) element;
			if (classElement.hasAnnotation()) {
				return ANNOTATED_COLOR;
			}
			
			if (classElement.hasExtendedAnnotation()) {
				return EXTENDED_COLOR;
			}
			
			if (classElement.hasMemberAnnotation()) {
				return MEMBER_COLOR;
			}
		}
		
		return null;
	}

	@Override
	public Color getBackground(Object element) {
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
