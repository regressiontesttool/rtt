package rtt.ui.ecore.editor.util;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rtt.ui.ecore.EcoreAnnotation;

/**
 * <p>This {@link ViewerFilter} can be used to filter
 * elements with a specific {@link EcoreAnnotation}. <br />
 * e.g.: {@link EcoreAnnotation#NODE} </p>
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class AnnotationViewerFilter extends ViewerFilter {
	
	private EcoreAnnotation[] annotations;

	/**
	 * Creates a new {@link AnnotationViewerFilter}. If used without
	 * any param the filter will display all {@link EcoreAnnotation}s.
	 * @param annotations
	 * @see EcoreAnnotation
	 */
	public AnnotationViewerFilter(EcoreAnnotation... annotations) {
		if (annotations.length == 0) {
			this.annotations = new EcoreAnnotation[] {
					EcoreAnnotation.PARSER,
					EcoreAnnotation.PARSER_AST,
					EcoreAnnotation.PARSER_INIT,
					EcoreAnnotation.NODE,
					EcoreAnnotation.NODE_INFORMATIONAL,
					EcoreAnnotation.NODE_COMPARE
			};
		} else {
			this.annotations = annotations;
		}				
	}

	@Override
	public final boolean select(final Viewer viewer, 
			final Object parentElement, final Object element) {
		
		if (element instanceof EModelElement) {
			if (element instanceof EPackage) {
				return true;
			} else if (element instanceof EAnnotation) {
				String source = ((EAnnotation) element).getSource();
				
				for (EcoreAnnotation annotation : annotations) {
					if (annotation.getSource().equals(source)) {
						return true;
					}
				}
			} else {
				EModelElement modelElement = (EModelElement) element;
				for (EcoreAnnotation annotation : annotations) {
					if (modelElement.getEAnnotation(annotation.getSource()) != null) {
						return true;
					}
				}
			}		
			
			return false;
		}
		
		return true;
	}

}
