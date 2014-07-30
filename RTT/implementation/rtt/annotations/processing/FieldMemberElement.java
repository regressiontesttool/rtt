package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

final class FieldMemberElement extends AbstractMemberElement<Field> {

	public FieldMemberElement(ClassElement classElement) {
		super(classElement);
	}

	@Override
	protected synchronized List<Field> createElements(
			ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		List<Field> annotatedFields = new ArrayList<>();
		
		ClassElement parentElement = classElement.parentElement;
		if (parentElement != null) {
			for (Field field : parentElement.getFields(annotation)) {
				annotatedFields.add(field);
			}
		}
		
		addFields(classElement.type, annotation, annotatedFields);
		
		return annotatedFields;
	}

	private void addFields(Class<?> type,
			Class<? extends Annotation> annotation, 
			List<Field> annotatedFields) {
		
		for (Class<?> interfaceType : type.getInterfaces()) {
			addFields(interfaceType, annotation, annotatedFields);
		}
		
		for (Field field : type.getDeclaredFields()){
			if (field.isAnnotationPresent(annotation)) {
				annotatedFields.add(field);
			}
		}
		
	}
}