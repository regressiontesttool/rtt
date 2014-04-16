package rtt.ui.ecore.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class TestUtils {
	
	public static EAttribute getAttribute(EClass classObject, String attributeName) {
		EStructuralFeature feature = classObject.getEStructuralFeature(attributeName);
		if (feature instanceof EAttribute) {
			return (EAttribute) feature; 
		}
		
		return null;
	}
	
	public static EReference getReference(EClass classObject, String referenceName) {
		EStructuralFeature feature = classObject.getEStructuralFeature(referenceName);
		if (feature instanceof EReference) {
			return (EReference) feature;
		}
		
		return null;
	}
	
	public static EOperation getOperation(EClass classObject, String operationName) {
		for (EOperation operation : classObject.getEOperations()) {
			if (operation.getName().equals(operationName)) {
				return operation;
			}
		}
		
		return null;
	}

}
