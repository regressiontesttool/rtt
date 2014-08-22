package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class FieldMemberElement extends AbstractMemberElement<Field> {

	public FieldMemberElement(ClassElement classElement) {
		super(classElement);
	}

	@Override
	protected Map<String, Field> getParentElements(ClassElement parentElement,
			Class<? extends Annotation> annotation) {
		return parentElement.getFieldMap(annotation);
	}

	@Override
	protected void addElements(Class<?> type,
			Class<? extends Annotation> annotation, Map<String, Field> annotatedElements) {
		
		for (Class<?> interfaceType : type.getInterfaces()) {
			addElements(interfaceType, annotation, annotatedElements);
		}
		
		for (Field field : type.getDeclaredFields()){
			if (field.isAnnotationPresent(annotation)) {
				annotatedElements.put(field.toGenericString(), field);
			}
		}
	}
}