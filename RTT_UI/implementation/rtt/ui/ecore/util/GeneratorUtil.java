package rtt.ui.ecore.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenOperation;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import rtt.ui.RttPluginUI;
import rtt.ui.ecore.EcoreAnnotation;

/**
 * A utility class for generating annotations from a generator model.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class GeneratorUtil {

	public final static String TEMPLATE_LOCATION = RttPluginUI.getPlugin().
			getBaseURL().toString() + "templates";

	public final static String CLASSPATH_VARIABLE_NAME = "RTT_UI_ECORE_GENERATOR";

	public static GenModel getGenModel(ResourceSet resourceSet) {
		if (resourceSet == null) {
			throw new IllegalArgumentException("Resource set was null.");
		}
		
		Resource resource = resourceSet.getResources().get(0);
		return (GenModel) resource.getContents().get(0);
	}
	
	public static String getPackageName(GenClass genClass) {
		return genClass.getGenPackage().getPackageName();
	}
	
	public static List<GenClass> getAnnotatedClasses(GenModel genModel, 
			EcoreAnnotation annotation) {
		
		List<GenClass> resultList = new ArrayList<GenClass>();
		for (GenPackage genPackage : genModel.getGenPackages()) {
			getGenClasses(genModel, genPackage, annotation, resultList);
		}
		
//		Object o2 = genModel.getAllGenAndUsedGenPackagesWithClassifiers();
//		Object o3 = genModel.getAllGenPackagesWithClassifiers();
//		Object o4 = genModel.getAllGenPackagesWithConcreteClasses();
//		Object o5 = genModel.getAllUsedGenPackagesWithClassifiers();

		return resultList;
	}
	
	private static void getGenClasses(GenModel genModel, 
			GenPackage genPackage, 
			EcoreAnnotation annotation,	
			List<GenClass> resultList) {
		
		for (GenClass genClass : genPackage.getGenClasses()) {
			EClass eClass = genClass.getEcoreClass();
			if (annotation.isPresentAt(eClass)) {
				genModel.addImport(genClass.getQualifiedInterfaceName());
				resultList.add(genClass);
			}
		}
		
		for (GenPackage subPackage : genPackage.getSubGenPackages()) {
			getGenClasses(genModel, subPackage, annotation, resultList);
		}
	}

	public static GenOperation getInitOperation(GenClass parserClass) {
		if (parserClass != null) {
			for (GenOperation genOperation : parserClass.getGenOperations()) {
				EOperation operation = genOperation.getEcoreOperation();
				if (EcoreAnnotation.PARSER_INIT.isPresentAt(operation)) {
					return genOperation;
				}
			}
		}
		
		return null;
	}

	public static GenFeature getAstFeature(GenClass parserClass) {
		if (parserClass != null) {
			for (GenFeature genFeature : parserClass.getGenFeatures()) {
				EStructuralFeature feature = genFeature.getEcoreFeature();
				if (EcoreAnnotation.PARSER_AST.isPresentAt(feature)) {				
					return genFeature;
				}
			}
		}	
		
		return null;
	}
	

	public static List<GenFeature> getNodeFeatures(GenClass nodeClass,
			EcoreAnnotation annotation) {
		
		List<GenFeature> resultList = new ArrayList<GenFeature>();
		for (GenFeature genFeature : nodeClass.getGenFeatures()) {
			EStructuralFeature feature = genFeature.getEcoreFeature();
			if (annotation.isPresentAt(feature)) {
				resultList.add(genFeature);
			}
		}
		return resultList;
	}
	
	public static List<GenOperation> getOperations(GenClass classObject,
			EcoreAnnotation annotation) {
		
		List<GenOperation> resultList = new ArrayList<GenOperation>();
		for (GenOperation genoperation : classObject.getGenOperations()) {
			EOperation operation = genoperation.getEcoreOperation();
			if (annotation.isPresentAt(operation)) {
				resultList.add(genoperation);
			}
		}
		return resultList;
	}

}
