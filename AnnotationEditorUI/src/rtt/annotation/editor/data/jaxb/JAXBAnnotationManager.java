package rtt.annotation.editor.data.jaxb;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.data.AnnotationExporter;
import rtt.annotation.editor.data.AnnotationImporter;
import rtt.annotation.editor.data.jaxb.model.AnnotatedClass;
import rtt.annotation.editor.data.jaxb.model.AnnotationModel;
import rtt.annotation.editor.data.jaxb.model.Init;
import rtt.annotation.editor.data.jaxb.model.ObjectFactory;
import rtt.annotation.editor.data.jaxb.model.Value;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.Annotation;
import rtt.annotation.editor.model.Annotation.AnnotationType;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;

public class JAXBAnnotationManager implements 
	AnnotationImporter, AnnotationExporter {

	private static final ObjectFactory FACTORY = new ObjectFactory();
	
	@Override
	public String getExtension() {
		return "*.annotation";
	}
	
	@Override
	public void importAnnotations(ClassModel model, Path path) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void exportAnnotations(ClassModel model, Path exportPath) {		
		AnnotationModel jaxbAnnotationModel = FACTORY.createAnnotationModel();
		for (Entry<PackageElement, List<ClassElement>> classEntry : model.getClassElements().entrySet()) {
			for (ClassElement classElement : classEntry.getValue()) {
				AnnotatedClass annotatedClass = null;
				if (isAnnotated(classElement)) {
					annotatedClass = FACTORY.createAnnotatedClass();
					
					annotatedClass.setName(classElement.getName());
					annotatedClass.setPackage(classElement.getPackageName());					
					annotatedClass.setIsNode(classElement.hasAnnotation());
					
					addValues(classElement.getValuableFields(), annotatedClass.getValueField());
					addValues(classElement.getValuableMethods(), annotatedClass.getValueMethod());
					
					addInits(classElement.getInitializableMethods(), annotatedClass.getInitMethod());
					
					jaxbAnnotationModel.getAnnotatedClass().add(annotatedClass);
				}
			}
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(AnnotationModel.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(jaxbAnnotationModel, exportPath.toFile());
		} catch (JAXBException e) {
			AnnotationEditorPlugin.logException("Could not export annotations", e);
		}		
	}
	
	private boolean isAnnotated(ClassElement classElement) {
		return classElement.hasAnnotation() || classElement.hasValues() || classElement.hasInits();
	}

	private void addValues(List<? extends Annotatable> annotatables, 
			List<Value> valueAnnotatables) {
		
		Annotation annotation = null;
		Value valueAnnotated = null;
		
		for (Annotatable annotatable : annotatables) {
			if (annotatable.hasAnnotation()) {
				annotation = annotatable.getAnnotation();
				
				if (annotation.getType() == AnnotationType.VALUE) {
					valueAnnotated = FACTORY.createValue();
					valueAnnotated.setName(annotatable.getName());
					
					Map<String,Object> attributes = annotation.getAttributes();
					valueAnnotated.setValueIndex((Integer) attributes.get("index"));
					valueAnnotated.setValueName((String) attributes.get("name"));
					valueAnnotated.setValueInformational((Boolean) attributes.get("informational"));
					
					valueAnnotatables.add(valueAnnotated);
				}
			}
		}
	}

	private void addInits(List<? extends Annotatable> annotatables, 
			List<Init> initAnnotatables) {
		
		Annotation annotation = null;
		Init initAnnotated = null;
		
		for (Annotatable annotatable : annotatables) {
			if (annotatable.hasAnnotation()) {
				annotation = annotatable.getAnnotation();
				
				if (annotation.getType() == AnnotationType.INITIALIZE) {
					initAnnotated = FACTORY.createInit();
					initAnnotated.setName(annotatable.getName());
					
					Map<String,Object> attributes = annotation.getAttributes();
					initAnnotated.setWithParams((Boolean) attributes.get("withParams"));
					
					initAnnotatables.add(initAnnotated);
				}
			}
		}
	}
}
