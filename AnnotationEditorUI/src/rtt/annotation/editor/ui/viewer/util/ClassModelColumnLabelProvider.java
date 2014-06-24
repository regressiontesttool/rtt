package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;

public class ClassModelColumnLabelProvider extends ColumnLabelProvider {

	private static final Color ANNOTATED_COLOR = JFaceResources.getColorRegistry().get(AnnotationEditorPlugin.ANNOTATED_COLOR);
	private static final Font DEFAULT_FONT = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);	
	@Override
	public String getText(Object element) {
		if (element instanceof ClassModel) {
			return ((ClassModel) element).toString();
		}
		
		if (element instanceof PackageElement) {
			return ((PackageElement) element).getName();
		}
		
		if (element instanceof ClassElement) {
			return ((ClassElement) element).getName();
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
		}
		
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public Font getFont(Object element) {
		FontDescriptor descriptor = FontDescriptor.createFrom(DEFAULT_FONT);
		
		boolean isAnnotated = false;
		boolean isAbstract = false;
		
		if (element instanceof Annotatable<?>) {
			isAnnotated = ((Annotatable<?>) element).hasAnnotation();
		}
		
		if (isAnnotated || isAbstract) {
			int style = SWT.NONE;
			style |= isAbstract ? SWT.ITALIC : SWT.NONE;
			style |= isAnnotated ? SWT.BOLD : SWT.NONE;
			
			descriptor.setStyle(style);
		}
		
		return descriptor.createFont(DEFAULT_FONT.getDevice());
	}
}
