package rtt.annotation.editor.controller;

import java.util.HashMap;
import java.util.Map;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.controller.impl.ClassElementAnnotationController;
import rtt.annotation.editor.controller.impl.FieldElementAnnotationController;
import rtt.annotation.editor.controller.impl.MethodElementAnnotationController;
import rtt.annotation.editor.controller.rules.RTTAnnotation;
import rtt.annotation.editor.controller.rules.RTTAnnotation.AnnotationType;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ControllerRegistry {
	
	public static final ControllerRegistry INSTANCE = new ControllerRegistry();
	private Map<Class<? extends Annotatable<?>>, IAnnotationController<?>> controller;
	
	protected ControllerRegistry() {
		controller = new HashMap<Class<? extends Annotatable<?>>, IAnnotationController<?>>();
		
		controller.put(ClassElement.class, new ClassElementAnnotationController());
		controller.put(FieldElement.class, new FieldElementAnnotationController());		
		controller.put(MethodElement.class, new MethodElementAnnotationController());
	}

	@SuppressWarnings("unchecked")
	protected <T extends Annotatable<?>> IAnnotationController<T> findController(T annotatableType) {
		if (controller.containsKey(annotatableType.getClass())) {
			return (IAnnotationController<T>) controller.get(annotatableType.getClass());
		}
		
		return null;
	}
	
	public static <T extends Annotatable<?>> boolean canExecute(
			Mode mode, AnnotationType annotationType, T element) {
		
		IAnnotationController<T> controller = INSTANCE.findController(element);
		if (controller != null) {
			return controller.canExecute(mode, annotationType, element);
		}
		
		return false;
	}

	public static <T extends Annotatable<?>> void execute(
			Mode mode, RTTAnnotation annotation, T element) {
		
		IAnnotationController<T> controller = INSTANCE.findController(element);
		if (controller != null) {
			controller.execute(mode, annotation, element);
			element.setChanged(true);
		}
	}	
}