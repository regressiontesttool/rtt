package rtt.annotation.editor.controller;

import java.util.HashMap;
import java.util.Map;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.rules.Annotation;

public class ControllerRegistry {
	
	public static final ControllerRegistry INSTANCE = new ControllerRegistry();
	private Map<Class<? extends Annotatable<?>>, IAnnotationController<?>> controller;
	
	protected ControllerRegistry() {
		controller = new HashMap<Class<? extends Annotatable<?>>, IAnnotationController<?>>();
		
		controller.put(ClassElement.class, new IAnnotationController<ClassElement>() {
			@Override
			public boolean setAnnotation(Annotation annotation,
					ClassElement element) {
				element.setAnnotation(annotation);
				return true;
			}
		});
		controller.put(FieldElement.class, new IAnnotationController<FieldElement>() {
			@Override
			public boolean setAnnotation(Annotation annotation, FieldElement element) {
				element.setAnnotation(annotation);
				return true;
			}
		});
		
		controller.put(MethodElement.class, new IAnnotationController<MethodElement>() {
			@Override
			public boolean setAnnotation(Annotation annotation,
					MethodElement element) {
				element.setAnnotation(annotation);
				return true;
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected <T extends Annotatable<?>> IAnnotationController<T> findController(T annotatableType) {
		if (controller.containsKey(annotatableType.getClass())) {
			return (IAnnotationController<T>) controller.get(annotatableType.getClass());
		}
		
		return null;
	}

	public static <T extends Annotatable<?>> void apply(Annotation node, T selectedObject) {
		IAnnotationController<T> controller = ControllerRegistry.INSTANCE.findController(selectedObject);
		if (controller != null) {
			controller.setAnnotation(node, selectedObject);
		}
	}
}