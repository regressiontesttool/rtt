package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

public class ClassElementReference extends ElementReference<ClassElement> {

	public ClassElementReference(String className) {
		super(className);
	}
	
	public boolean isAnnotated() {
		if (isResolved()) {
			return getReference().hasAnnotation() 
					|| getReference().hasExtendedAnnotation();
		}
		
		return false;
	}

	public static ClassElementReference create(String className) {
		if (className != null && !className.equals("")) {
			return new ClassElementReference(className);
		}
		
		return null;
	}
	
	public static List<ClassElementReference> create(String[] classNames) {
		List<ClassElementReference> result = new ArrayList<>();
		for (String interfaceName : classNames) {
			result.add(create(interfaceName));
		}
		
		return result;
	}

}
