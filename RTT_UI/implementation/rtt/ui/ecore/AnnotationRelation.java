package rtt.ui.ecore;

public class AnnotationRelation {
	
	public final EcoreAnnotation topAnnotation;
	public final EcoreAnnotation[] relatedAnnotations;
	
	public static final AnnotationRelation PARSER_RELATIONS = 
			new AnnotationRelation(EcoreAnnotation.PARSER, 
					EcoreAnnotation.PARSER_AST, EcoreAnnotation.PARSER_INIT);
	
	public static final AnnotationRelation NODE_RELATIONS =
			new AnnotationRelation(EcoreAnnotation.NODE, 
					EcoreAnnotation.NODE_CHILDREN, EcoreAnnotation.NODE_COMPARE,
					EcoreAnnotation.NODE_INFORMATIONAL);
	
	public AnnotationRelation(EcoreAnnotation topAnnotation, 
			EcoreAnnotation... relatedAnnotations) {
		
		this.topAnnotation = topAnnotation;
		this.relatedAnnotations = relatedAnnotations;
	}

	public static AnnotationRelation findRelation(EcoreAnnotation annotation) {
		if (PARSER_RELATIONS.topAnnotation.equals(annotation)) {
			return PARSER_RELATIONS;
		}
		if (NODE_RELATIONS.topAnnotation.equals(annotation)) {
			return NODE_RELATIONS;
		}
		
		for (EcoreAnnotation rttAnnotation : PARSER_RELATIONS.relatedAnnotations) {
			if (rttAnnotation.equals(annotation)) {
				return PARSER_RELATIONS;
			}
		}
		
		for (EcoreAnnotation rttAnnotation : NODE_RELATIONS.relatedAnnotations) {
			if (rttAnnotation.equals(annotation)) {
				return NODE_RELATIONS;
			}
		}
		
		throw new IllegalStateException("Could not determine annotation.");
	}

	public static boolean areRelated(EcoreAnnotation topAnnotation,
			EcoreAnnotation annotation) {
		
		AnnotationRelation relation = AnnotationRelation.findRelation(topAnnotation);
		for (EcoreAnnotation relatedAnnotation : relation.relatedAnnotations) {
			if (relatedAnnotation.equals(annotation)) {
				return true;
			}
		}
		
		return false;
	}

}
