package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

final class FieldMemberElement extends MemberElement<Field> {

	public FieldMemberElement(ClassElement classElement) {
		super(classElement);
	}

	@Override
	protected Map<String, Field> createElements(ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		Map<String, Field> annotatedFields = new HashMap<>();
		
		ClassElement parentElement = classElement.getParentElement();
		if (parentElement != null) {
			annotatedFields.putAll(parentElement.getFieldMap(annotation));
		}
		
		Class<?> objectType = classElement.getType();				
		for (Field field : objectType.getDeclaredFields()){
			if (field.isAnnotationPresent(annotation)) {
				annotatedFields.put(field.getName(), field);
			}
		}
		
		return annotatedFields;
	}
}