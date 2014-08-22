package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractMemberElement<T extends AnnotatedElement> {
	private Map<Class<? extends Annotation>, Map<String, T>> elements;
	private ClassElement classElement;

	public AbstractMemberElement(ClassElement classElement) {
		this.elements = new HashMap<>();
		this.classElement = classElement;
	}

	public Map<String, T> getAnnotatedElements(Class<? extends Annotation> annotation) {

		if (!elements.containsKey(annotation)) {			
			elements.put(annotation, createElements(classElement, annotation));
		}

		return elements.get(annotation);
	}
	
	protected synchronized Map<String, T> createElements(ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		Map<String, T> entries = new TreeMap<>();
		if (classElement.parentElement != null) {
			entries.putAll(getParentElements(
					classElement.parentElement, annotation));
		}
		
		addElements(classElement.type, annotation, entries);
		return entries;
	}
	
	protected abstract Map<String, T> getParentElements(
			ClassElement parentElement, 
			Class<? extends Annotation> annotation);

	protected abstract void addElements(Class<?> type,
			Class<? extends Annotation> annotation, Map<String, T> annotatedElements);
	
}