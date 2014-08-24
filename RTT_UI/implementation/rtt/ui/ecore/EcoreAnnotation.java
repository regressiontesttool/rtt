package rtt.ui.ecore;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

import rtt.annotations.Node;
import rtt.ui.ecore.editor.EcoreEditor;

/**
 * This enumeration represents several annotations
 * which are used by the {@link EcoreEditor}.
 * @see rtt.annotations
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public enum EcoreAnnotation {
	
	/**
	 * @deprecated
	 */
	PARSER("rtt/annotation/parser", 
			"@Parser", EClass.class),
	
	/**
	 * @deprecated
	 */
	PARSER_INIT("rtt/annotation/parser/init", 
			"@Parser.Initialize", EOperation.class),
	
	/**
	 * @deprecated
	 */
	PARSER_AST("rtt/annotation/parser/ast", 
			"@Parser.AST", EReference.class, EOperation.class),
	
	/**
	 * Represents the {@link Node} annotation.
	 */
	NODE("rtt/annotation/node", 
			"@Node", EClass.class),
	
	/**
	 * Represents a {@link Node.Compare} annotation.
	 */
	NODE_COMPARE("rtt/annotation/node/compare", 
			"@Node.Compare", EAttribute.class, EReference.class, EOperation.class),
	
	/**
	 * Represents a {@link Node.Informational} annotation.
	 */
	NODE_INFORMATIONAL("rtt/annotation/node/informational", 
			"@Node.Informational", EAttribute.class, EReference.class, EOperation.class);
	
	private String source;
	private String token;
	private Class<?>[] targetClasses;

	private EcoreAnnotation(final String sourceString,
			final String tokenString, Class<?>... classes) {
		
		this.source = sourceString;
		this.token = tokenString;
		this.targetClasses = classes;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getToken() {
		return token;
	}
	
	/**
	 * Creates a new corresponding {@link EAnnotation}.
	 * @return an {@link EAnnotation}
	 */
	public EAnnotation createEAnnotation() {
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(source);
		
		return annotation;
	}
	
	/**
	 * Returns the corresponding {@link EAnnotation} of the given
	 * {@link EModelElement} or {@code null}, if no such annotation 
	 * is set. The element must not be {@code null}.
	 * @param element the {@link EModelElement}
	 * @return the corresponding {@link EAnnotation} or {@code null} 
	 */
	public EAnnotation getEAnnotation(final EModelElement element) {
		if (element != null) {
			return element.getEAnnotation(source);
		}
		
		throw new IllegalArgumentException("The element must not null.");		
	}

	/**
	 * Checks if an corresponding {@link EAnnotation} is present
	 * at the given {@link EModelElement}.
	 * @param element the {@link EModelElement}
	 * @return true, if a corresponding {@link EAnnotation} exists
	 * @see EcoreAnnotation#getEAnnotation
	 */
	public boolean isPresentAt(final EModelElement element) {
		return getEAnnotation(element) != null;
	}
	
	public boolean canSetTo(final Object object) {
		for (Class<?> classObject : targetClasses) {
			if (classObject.isInstance(object)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Returns a corresponding {@link EcoreAnnotation} for the 
	 * given {@link EAnnotation}, if such an annotation exists.
	 * @param annotation an {@link EAnnotation}
	 * @return an {@link EcoreAnnotation} or {@code null}
	 */
	public static EcoreAnnotation convert(EAnnotation annotation) {
		if (annotation == null) {
			throw new IllegalArgumentException(
					"The annotation must not be null.");
		}
		
		String source = annotation.getSource();
		
		for (EcoreAnnotation rttAnnotation : values()) {
			if (rttAnnotation.source.equals(source)) {
				return rttAnnotation;
			}
		}

		return null;
	}

	public static EcoreAnnotation search(EModelElement element) {
		for (EcoreAnnotation annotation : EcoreAnnotation.values()) {
			if (element.getEAnnotation(annotation.getSource()) != null) {
				return annotation;
			}
		}
		return null;
	}
	
	/**
	 * This method searchs for {@link EcoreAnnotation}s within
	 * all {@link EAttribute}s, {@link EReference}s and {@link EOperation}s
	 * of a given {@link EClass}. If any object contains a 
	 * {@link EcoreAnnotation} this method will return {@code true}.
	 * 
	 * @param classObject
	 * @return true, if any children has a {@link EcoreAnnotation}.
	 */
	public static boolean areContainedByChilds(EClass classObject, 
			EcoreAnnotation classAnnotation) {
		
		for (EStructuralFeature feature : classObject.getEStructuralFeatures()) {
			EcoreAnnotation annotation = search(feature);
			if (annotation != null && AnnotationRelation.areRelated(classAnnotation, annotation)) {
				return true;
			}
		}
		
		for (EOperation operation : classObject.getEOperations()) {
			EcoreAnnotation annotation = search(operation);
			if (annotation != null && AnnotationRelation.areRelated(classAnnotation, annotation)) {
				return true;
			}
		}
		
		return false;
	}
}
