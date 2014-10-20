package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to reference {@link ClassElement}s.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
public class ClassElementReference extends ElementReference<ClassElement> {

	protected ClassElementReference(String className) {
		super(className);
	}
	
	public boolean isAnnotated() {
		if (isResolved()) {
			ClassElement refClass = getReference();
			return refClass.hasAnnotation() || refClass.hasExtendedAnnotation();
		}
		
		return false;
	}

	public static ClassElementReference create(String className) {
		if (className != null && !className.equals("")) {
			return new ClassElementReference(className);
		}
		
		return null;
	}
	
	public static List<ClassElementReference> create(String... classNames) {
		List<ClassElementReference> result = new ArrayList<>();
		for (String interfaceName : classNames) {
			result.add(create(interfaceName));
		}
		
		return result;
	}

}
