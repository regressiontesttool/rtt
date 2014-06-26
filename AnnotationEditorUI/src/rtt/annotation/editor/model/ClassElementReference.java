package rtt.annotation.editor.model;

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

}
