package rtt.annotation.editor.controller;

import java.util.HashMap;
import java.util.Map;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;

public class ControllerRegistry {
	
	public static final ControllerRegistry INSTANCE = new ControllerRegistry();
	private Map<Class<?>, IAnnotationController<?>> controller;
	
	protected ControllerRegistry() {
		controller = new HashMap<Class<?>, IAnnotationController<?>>();
		
		controller.put(ClassElement.class, new ClassElementAnnotationController());
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotatable<?>> IAnnotationController<T> findController(Class<T> annotatableType) {
		if (controller.containsKey(annotatableType)) {
			return (IAnnotationController<T>) controller.get(annotatableType);
		}
		
		return null;
	}
}