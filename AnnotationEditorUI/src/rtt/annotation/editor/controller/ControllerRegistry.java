package rtt.annotation.editor.controller;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.controller.impl.ClassElementAnnotationController;
import rtt.annotation.editor.controller.impl.FieldElementAnnotationController;
import rtt.annotation.editor.controller.impl.InitiMethodElementAnnotationController;
import rtt.annotation.editor.controller.impl.ValueMethodElementAnnotationController;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

@SuppressWarnings("unchecked")
public class ControllerRegistry {
	
	public static final ControllerRegistry INSTANCE = new ControllerRegistry();
	private List<IAnnotationController<?, ?>> controllers;
	
	protected ControllerRegistry() {
		controllers = new ArrayList<>();
		
		controllers.add(new ClassElementAnnotationController());
		controllers.add(new FieldElementAnnotationController());		
		controllers.add(new ValueMethodElementAnnotationController());
		controllers.add(new InitiMethodElementAnnotationController());
	}

	protected <A extends Annotation, T extends Annotatable<A>> 
		IAnnotationController<A, T> findController(
				Class<A> annotationType, Class<T> annotatableType) {
		
		for (IAnnotationController<?, ?> controller : controllers) {
			if (controller.hasAnnotation(annotationType) 
					&& controller.hasType(annotatableType)) {
				
				return (IAnnotationController<A, T>) controller;
			}
		}
		
		return null;
	}
	
	public static <A extends Annotation, T extends Annotatable<A>> boolean canExecute(
			Mode mode, Class<A> annotationType, T element) {
		
		IAnnotationController<A, T> controller = INSTANCE.findController(
				annotationType, element.getClass());
		
		if (controller != null) {
			return controller.canExecute(mode, annotationType, element);
		}
		
		return false;
	}

	public static <A extends Annotation, T extends Annotatable<A>> boolean execute(
			Mode mode, A annotation, T element) {
		
		IAnnotationController<A, T> controller = INSTANCE.findController(
				annotation.getClass(), element.getClass());
		
		if (controller != null && controller.canExecute(mode, annotation.getClass(), element)) {
			if (controller.execute(mode, annotation, element)) {
				element.setChanged();
				return true;
			}
		}
		
		return false;
	}	
}