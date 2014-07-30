package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FieldMemberElement extends MemberElement<Field> {

	public FieldMemberElement(ClassElement classElement) {
		super(classElement);
	}

	@Override
	protected synchronized List<Field> createElements(ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		List<Field> annotatedFields = new ArrayList<>();
		
		ClassElement parentElement = classElement.getParentElement();
		if (parentElement != null) {
			for (Field field : parentElement.getFields(annotation)) {
				annotatedFields.add(field);
			}
		}
		
		Class<?> objectType = classElement.getType();				
		for (Field field : objectType.getDeclaredFields()){
			if (field.isAnnotationPresent(annotation)) {
				annotatedFields.add(field);
			}
		}
		
		System.out.println("Fields: " + Arrays.toString(annotatedFields.toArray()));
		
		return annotatedFields;
	}
}