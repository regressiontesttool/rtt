package rtt.annotation.editor.model;


public class ClassModelFactory {
	
	protected ClassModelFactory() {

	}

	public static ClassModelFactory getFactory() {
		return new ClassModelFactory();
	}
	
	public ClassModel createClassModel() {
		return new ClassModel();
	}
	
	public ClassElement createClassElement(ClassModel parent) {
		ClassElement element = new ClassElement(parent);		
		return element;
	}
	
	public FieldElement createFieldElement(ClassElement parent, String name) {
		FieldElement element = new FieldElement(parent);		
		element.setName(name);
		
		return element;
	}
	
	public MethodElement createMethodElement(ClassElement parent, String name) {
		MethodElement element = new MethodElement(parent);
		element.setName(name);
		
		return element;
	}

}
