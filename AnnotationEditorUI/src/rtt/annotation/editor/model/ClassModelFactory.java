package rtt.annotation.editor.model;

import rtt.annotation.editor.model.annotation.Annotation;

/**
 * A factory for new {@link ModelElement}s.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
public class ClassModelFactory {
	
	protected ClassModelFactory() {}

	public static ClassModelFactory getFactory() {
		return new ClassModelFactory();
	}
	
	public ClassModel createClassModel() {
		return new ClassModel();
	}
	
	public ClassElement createClassElement(ClassModel parent, 
			String className, String packageName) {
		
		ClassElement element = new ClassElement(parent);
		element.setName(className);
		element.setPackageName(packageName);
		
		return element;
	}
	
	public <T extends Annotation> FieldElement<T> createFieldElement(ClassElement parent, String name) {
		FieldElement<T> element = new FieldElement<>(parent);		
		element.setName(name);
		
		return element;
	}
	
	public <T extends Annotation> MethodElement<T> createMethodElement(ClassElement parent, String name) {
		MethodElement<T> element = new MethodElement<>(parent);
		element.setName(name);
		
		return element;
	}

}
