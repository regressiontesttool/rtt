package rtt.ui.ecore.tests;

import java.net.URL;

import org.junit.Assert;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rtt.ui.RttPluginUI;
import rtt.ui.ecore.EcoreAnnotation;
import rtt.ui.ecore.EcoreController;
import rtt.ui.utils.RttPluginUtil;

public class AnnotationTest {
	
	private static ComposedAdapterFactory factory;
	private static EditingDomain domain;
	private static ResourceSet set;
	private static Resource resource;
	private static EcoreController controller;
	
	private EClass parserClass;
	private EClass nodeClass;	

	@BeforeClass
	public static void loadEcore() {
		factory = RttPluginUtil.createFactory();
		Assert.assertNotNull("The adapter factory was null.", factory);
		
		domain = RttPluginUtil.createEditingDomain(factory);
		Assert.assertNotNull("The editing domain was null.", domain);		
		
		domain.getResourceSet().getURIConverter().getURIMap().putAll(
				EcorePlugin.computePlatformURIMap());
		
		URL resourceURL = FileLocator.find(
				RttPluginUI.getPlugin().getBundle(), 
				new Path("./model/test.ecore"), null);
		
		domain.loadResource(resourceURL.toString());
		
		set = domain.getResourceSet();
		Assert.assertNotNull("The resource set was null.", set);
		
		resource = set.getResources().get(0);
		Assert.assertNotNull("The resource was null.", resource);
		
		controller = new EcoreController(domain);
		Assert.assertNotNull("The controller was null.", controller);
	}
	
	@AfterClass
	public static void dispose() {
		if (factory != null) {
			factory.dispose();
		}		
	}
	
	@Before
	public void initializeClasses() {		
		parserClass = null;
		nodeClass = null;
		
		for (EObject object : resource.getContents()) {
			EPackage packageObject = (EPackage) object;
			for (EClassifier classifier : packageObject.getEClassifiers()) {
//				System.out.println(classifier.getName());
				if (classifier.getName().equals(TestConstants.PARSER_CLASS)) {
					parserClass = (EClass) classifier;
				}
				
				if (classifier.getName().equals(TestConstants.NODE_CLASS)) {					
					nodeClass = (EClass) classifier;
				}
			}
		}
		
		Assert.assertNotNull("The parser class null.", parserClass);
		Assert.assertNotNull("The node class was null.", nodeClass);
	}
	
	@After
	public void resetEditingDomain() {
		CommandStack stack = domain.getCommandStack();
		while(stack.canUndo()) {
			stack.undo();
		}
	}
	
	public void checkAnnotationPresent(EModelElement element, EcoreAnnotation annotation) {
		EAnnotation eAnnotation = element.getEAnnotation(annotation.getSource());
		Assert.assertNotNull("The " + annotation.getToken() + " annotation was not set.", eAnnotation);
	}
	
	public void checkAnnotationNotPresent(EModelElement element, EcoreAnnotation annotation) {
		EAnnotation eAnnotation = element.getEAnnotation(annotation.getSource());
		Assert.assertNull("The " + annotation.getToken() + " annotation was set, but should not.", eAnnotation);
	}
	
	@Test
	public void testNodeClass() {
		// check if not other annotation is present
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);

		// add @Node to node class
		Assert.assertTrue("Could not set @Node annotation",
				controller.addAnnotation(nodeClass, EcoreAnnotation.NODE));
		checkAnnotationPresent(nodeClass, EcoreAnnotation.NODE);
		
		// remove @Node to node class
		controller.removeAnnotation(nodeClass, EcoreAnnotation.NODE);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);
		
		// add @Parser and @Node at the same time
		Assert.assertTrue("Could not set @Parser annotation",
				controller.addAnnotation(nodeClass, EcoreAnnotation.PARSER));
		checkAnnotationPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);
		Assert.assertTrue("Could not set @Node annotation", 
				controller.addAnnotation(nodeClass, EcoreAnnotation.NODE));
		checkAnnotationPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationPresent(nodeClass, EcoreAnnotation.NODE);
	}
	
	@Test
	public void testParserClass() {
		// check if not other annotation is present
		checkAnnotationNotPresent(parserClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(parserClass, EcoreAnnotation.NODE);

		// add @Node to node class
		Assert.assertTrue("Could not set @Parser annotation",
				controller.addAnnotation(parserClass, EcoreAnnotation.PARSER));
		checkAnnotationPresent(parserClass, EcoreAnnotation.PARSER);
		
		// remove @Node to node class
		controller.removeAnnotation(parserClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(parserClass, EcoreAnnotation.PARSER);
		
		// add @Parser and @Node at the same time
		Assert.assertTrue("Could not set @Node annotation",
				controller.addAnnotation(parserClass, EcoreAnnotation.NODE));
		checkAnnotationPresent(parserClass, EcoreAnnotation.NODE);
		checkAnnotationNotPresent(parserClass, EcoreAnnotation.PARSER);
		Assert.assertTrue("Could not set @Parser annotation", 
				controller.addAnnotation(parserClass, EcoreAnnotation.PARSER));
		checkAnnotationPresent(parserClass, EcoreAnnotation.PARSER);
		checkAnnotationPresent(parserClass, EcoreAnnotation.NODE);
	}
	
	@Test
	public void testAttributes() {
		// check if not other annotation is present
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);
		
		EAttribute parserAttribute = TestUtils.getAttribute(parserClass, TestConstants.ATTRIBUTE);	
		Assert.assertNotNull("Parser attribute was null.", parserAttribute);
		EAttribute nodeAttribute = TestUtils.getAttribute(nodeClass, TestConstants.ATTRIBUTE);		
		Assert.assertNotNull("Node attribute was null.", nodeAttribute);
		
		// Test 1.1: if parent class has no node annotation, 
		// setting related annotation should fail at all
		Assert.assertFalse("An annotation was set, but should not.",
				controller.setRelatedAnnotation(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL));
		
		controller.addAnnotation(nodeClass, EcoreAnnotation.NODE);
		controller.addAnnotation(parserClass, EcoreAnnotation.PARSER);
		
		// Test 2.1: parser EAttributes dont have ast or init annotation
		Assert.assertFalse("@Parser.AST was set, but should not.", 
				controller.setRelatedAnnotation(parserAttribute, EcoreAnnotation.PARSER_AST));
		checkAnnotationNotPresent(parserAttribute, EcoreAnnotation.PARSER_AST);
		checkAnnotationNotPresent(parserAttribute, EcoreAnnotation.PARSER_INIT);

		Assert.assertFalse("@Parser.Initialize was set, but should not.", 
				controller.setRelatedAnnotation(parserAttribute, EcoreAnnotation.PARSER_INIT));
		checkAnnotationNotPresent(parserAttribute, EcoreAnnotation.PARSER_AST);
		checkAnnotationNotPresent(parserAttribute, EcoreAnnotation.PARSER_INIT);
		
		// Test 2.2: node EAttributes can have info or compare annotation, but not children
		Assert.assertTrue("@Node.Informational was not set.", 
				controller.setRelatedAnnotation(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL));
		checkAnnotationPresent(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL);
		checkAnnotationNotPresent(nodeAttribute, EcoreAnnotation.NODE_COMPARE);

		Assert.assertTrue("@Node.Compare was not set.", 
				controller.setRelatedAnnotation(nodeAttribute, EcoreAnnotation.NODE_COMPARE));
		checkAnnotationPresent(nodeAttribute, EcoreAnnotation.NODE_COMPARE);
		checkAnnotationNotPresent(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL);
	}
	
	@Test
	public void testReferences() {
		// check if not other annotation is present
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);

		EReference parserReference = TestUtils.getReference(parserClass, TestConstants.REFERENCE);
		Assert.assertNotNull("Parser reference was null.");
		EReference nodeReference = TestUtils.getReference(nodeClass, TestConstants.REFERENCE);
		Assert.assertNotNull("Node reference was null.");

		// Test 1.1: if parent class has no parser/node annotation
		// setting related annotation should fail at all
		Assert.assertFalse("An annotation was set, but should not.",
				controller.setRelatedAnnotation(parserReference, EcoreAnnotation.PARSER_AST));
		Assert.assertFalse("An annotation was set, but should not", 
				controller.setRelatedAnnotation(nodeReference, EcoreAnnotation.NODE_COMPARE));
		
		controller.addAnnotation(parserClass, EcoreAnnotation.PARSER);
		controller.addAnnotation(nodeClass, EcoreAnnotation.NODE);

		// Test 2.1: parser EReference can have ast but not init annotation
		Assert.assertTrue("@Parser.AST was not set.", 
				controller.setRelatedAnnotation(parserReference, EcoreAnnotation.PARSER_AST));
		checkAnnotationPresent(parserReference, EcoreAnnotation.PARSER_AST);
		checkAnnotationNotPresent(parserReference, EcoreAnnotation.PARSER_INIT);

		Assert.assertFalse("@Parser.Initialize was set, but should not.", 
				controller.setRelatedAnnotation(parserReference, EcoreAnnotation.PARSER_INIT));
		checkAnnotationPresent(parserReference, EcoreAnnotation.PARSER_AST); // ast annotation from last set
		checkAnnotationNotPresent(parserReference, EcoreAnnotation.PARSER_INIT);

		// Test 2.2: node EReferences can have info, compare and children annotations
		Assert.assertTrue("@Node.Informational was not set.", 
				controller.setRelatedAnnotation(nodeReference, EcoreAnnotation.NODE_INFORMATIONAL));
		checkAnnotationPresent(nodeReference, EcoreAnnotation.NODE_INFORMATIONAL);
		checkAnnotationNotPresent(nodeReference, EcoreAnnotation.NODE_COMPARE);

		Assert.assertTrue("@Node.Compare was not set.", 
				controller.setRelatedAnnotation(nodeReference, EcoreAnnotation.NODE_COMPARE));
		checkAnnotationPresent(nodeReference, EcoreAnnotation.NODE_COMPARE);
		checkAnnotationNotPresent(nodeReference, EcoreAnnotation.NODE_INFORMATIONAL);
	}
	
	@Test
	public void testOperations() {
		// check if not other annotation is present
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);
		
		EOperation parserOperation = TestUtils.getOperation(parserClass, TestConstants.OPERATION);
		Assert.assertNotNull("Parser operation was null.", parserOperation);
		EOperation nodeOperation = TestUtils.getOperation(nodeClass, TestConstants.OPERATION);
		Assert.assertNotNull("Node operation was null.", nodeOperation);
		
		// Test 1.1: if parent class has no parser/node annotation
		// setting related annotation should fail at all
		Assert.assertFalse("An annotation was set, but should not.",
				controller.setRelatedAnnotation(parserOperation, EcoreAnnotation.PARSER_INIT));
		Assert.assertFalse("An annotation was set, but should not", 
				controller.setRelatedAnnotation(nodeOperation, EcoreAnnotation.NODE_COMPARE));
		
		controller.addAnnotation(parserClass, EcoreAnnotation.PARSER);
		controller.addAnnotation(nodeClass, EcoreAnnotation.NODE);
		
		// Test 2.1: parser EOperation can have ast or init annotation
		Assert.assertTrue("@Parser.AST was not set.", 
				controller.setRelatedAnnotation(parserOperation, EcoreAnnotation.PARSER_AST));
		checkAnnotationPresent(parserOperation, EcoreAnnotation.PARSER_AST);
		checkAnnotationNotPresent(parserOperation, EcoreAnnotation.PARSER_INIT);

		Assert.assertTrue("@Parser.Init was not set.", 
				controller.setRelatedAnnotation(parserOperation, EcoreAnnotation.PARSER_INIT));
		checkAnnotationPresent(parserOperation, EcoreAnnotation.PARSER_INIT);
		checkAnnotationNotPresent(parserOperation, EcoreAnnotation.PARSER_AST);		
		
		// Test 2.2: node EOperations can have info, compare and children annotations
		Assert.assertTrue("@Node.Informational was not set.", 
				controller.setRelatedAnnotation(nodeOperation, EcoreAnnotation.NODE_INFORMATIONAL));
		checkAnnotationPresent(nodeOperation, EcoreAnnotation.NODE_INFORMATIONAL);
		checkAnnotationNotPresent(nodeOperation, EcoreAnnotation.NODE_COMPARE);

		Assert.assertTrue("@Node.Compare was not set.", 
				controller.setRelatedAnnotation(nodeOperation, EcoreAnnotation.NODE_COMPARE));
		checkAnnotationPresent(nodeOperation, EcoreAnnotation.NODE_COMPARE);
		checkAnnotationNotPresent(nodeOperation, EcoreAnnotation.NODE_INFORMATIONAL);
	}
	
	@Test 
	public void testRemoveAnnotations() {
		// check if not other annotation is present
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);
		
		// Test 1.1: remove an non existing annotation
		Assert.assertFalse("A non existing annotation has been removed.",
				controller.removeAnnotation(parserClass, EcoreAnnotation.PARSER));
		
		// Test 1.2: remove an existing annotation (no annotations set to childs)
		Assert.assertTrue("@Node annotation could not be set.",
				controller.addAnnotation(nodeClass, EcoreAnnotation.NODE));
		Assert.assertTrue("@Node annotation could not be removed", 
				controller.removeAnnotation(nodeClass, EcoreAnnotation.NODE));
		
		// Test 1.3: remove an existing annotation (annotations set to childs)
		Assert.assertTrue("@Node annotation could not be set.",
				controller.addAnnotation(nodeClass, EcoreAnnotation.NODE));
		EAttribute nodeAttribute = TestUtils.getAttribute(nodeClass, TestConstants.ATTRIBUTE);
		Assert.assertNotNull("The attribute was null.",	nodeAttribute);
		Assert.assertTrue("@Node.Info annotation could not be set.",
				controller.setRelatedAnnotation(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL));
		Assert.assertTrue("@Node annotation was removed, but should not",
				controller.removeAnnotation(nodeClass, EcoreAnnotation.NODE));
		checkAnnotationNotPresent(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.NODE);
		
		// Test 2.1: - adding both parser and node annotation to one class
		// - adding related annotations to childs
		// - removing one (parser or node) annotation should only remove 
		//   related annotations from children
		Assert.assertTrue("@Node annotation could not be set.",
				controller.addAnnotation(nodeClass, EcoreAnnotation.NODE));
		Assert.assertTrue("@Parser annotation could not be set.",
				controller.addAnnotation(nodeClass, EcoreAnnotation.PARSER));
		Assert.assertTrue("@Node.Info annotation could not be set.",
				controller.setRelatedAnnotation(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL));
		
		EOperation nodeOperation = TestUtils.getOperation(nodeClass, TestConstants.OPERATION);
		Assert.assertNotNull("Node operation was null.", nodeOperation);
		Assert.assertTrue("@Parser.Init annotation could not be set", 
				controller.setRelatedAnnotation(nodeOperation, EcoreAnnotation.PARSER_INIT));
		
		EReference nodeReference = TestUtils.getReference(nodeClass, TestConstants.REFERENCE);
		Assert.assertNotNull("Node reference was null.", nodeReference);
		Assert.assertTrue("@Parser.AST annotation could not be set.", 
				controller.setRelatedAnnotation(nodeReference, EcoreAnnotation.PARSER_AST));
		
		Assert.assertTrue("Could not remove @Parser annotation.",
				controller.removeAnnotation(nodeClass, EcoreAnnotation.PARSER));
		checkAnnotationPresent(nodeClass, EcoreAnnotation.NODE);
		checkAnnotationNotPresent(nodeClass, EcoreAnnotation.PARSER);
		checkAnnotationPresent(nodeAttribute, EcoreAnnotation.NODE_INFORMATIONAL);
		checkAnnotationNotPresent(nodeOperation, EcoreAnnotation.PARSER_INIT);
		checkAnnotationNotPresent(nodeReference, EcoreAnnotation.PARSER_AST);
		
	}
}
