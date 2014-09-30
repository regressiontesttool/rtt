package rtt.annotation.editor.controller;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.controller.impl.InitAnnotationController;
import rtt.annotation.editor.controller.impl.NodeAnnotationController;
import rtt.annotation.editor.controller.impl.ValueAnnotationController;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

@SuppressWarnings("unchecked")
public class ControllerRegistry {
	
	public static final ControllerRegistry INSTANCE = new ControllerRegistry();
	private List<IAnnotationController<?>> controllers;
	
	protected ControllerRegistry() {
		controllers = new ArrayList<>();
		
		controllers.add(new NodeAnnotationController());
		controllers.add(new ValueAnnotationController());		
		controllers.add(new InitAnnotationController());
	}

	protected <A extends Annotation> IAnnotationController<A> findController(Class<A> annotationType) {
		
		for (IAnnotationController<?> controller : controllers) {
			if (controller.hasAnnotationType(annotationType)) {				
				return (IAnnotationController<A>) controller;
			}
		}
		
		return null;
	}
	
	public static <A extends Annotation> boolean canExecute(Mode mode, 
			Class<? extends Annotation> annotationType, Annotatable<?> element) {
		
		IAnnotationController<?> controller = INSTANCE.findController(annotationType);
		return controller != null && controller.canExecute(mode, element);
	}

	public static <A extends Annotation> boolean execute(Mode mode, 
			Class<A> annotationType, Annotatable<?> element) {
		
		IAnnotationController<A> controller = INSTANCE.findController(annotationType);
		
		if (controller != null && controller.canExecute(mode, element)) {
			if (controller.execute(mode, (Annotatable<A>) element)) {
				element.setChanged();
				return true;
			};
		}
		
		return false;
	}	
}