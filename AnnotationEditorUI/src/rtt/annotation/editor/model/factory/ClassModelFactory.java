package rtt.annotation.editor.model.factory;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ClassModelFactory {
	
	protected ClassModelFactory() {

	}

	public static ClassModelFactory getFactory() {
		return new ClassModelFactory();
	}
	
	public ClassModel createClassModel() {
		return new ClassModel();
	}
	
	public ClassElement createClassElement() {
		ClassElement element = new ClassElement();		
		return element;
	}
	
	public FieldElement createFieldElement() {
		FieldElement element = new FieldElement();		
		return element;
	}
	
	public MethodElement createMethodElement() {
		MethodElement element = new MethodElement();		
		return element;
	}

}
