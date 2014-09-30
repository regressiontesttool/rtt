package rtt.annotation.editor.data.jaxb;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.data.AnnotationExporter;
import rtt.annotation.editor.data.AnnotationImporter;
import rtt.annotation.editor.data.jaxb.model.AnnotatedClass;
import rtt.annotation.editor.data.jaxb.model.AnnotationModel;
import rtt.annotation.editor.data.jaxb.model.Init;
import rtt.annotation.editor.data.jaxb.model.ObjectFactory;
import rtt.annotation.editor.data.jaxb.model.Value;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class JAXBAnnotationManager implements 
	AnnotationImporter, AnnotationExporter {

	private static JAXBAnnotationManager INSTANCE = null;
	
	private ObjectFactory factory = new ObjectFactory();
	private JAXBContext context = null;	
	
	private JAXBAnnotationManager() throws JAXBException {
		factory = new ObjectFactory();
		context = JAXBContext.newInstance(AnnotationModel.class);		
	}
	
	public static synchronized final JAXBAnnotationManager getInstance() {
		if (INSTANCE == null) {
			try {
				INSTANCE = new JAXBAnnotationManager();
			} catch (JAXBException e) {
				throw new IllegalStateException("Could not initialize jaxb manager.", e);
			}
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getExtension() {
		return "*.rtta";
	}
	
	@Override
	public void importAnnotations(ClassModel model, Path importPath) {
		AnnotationModel annotationModel = null;
		
		try (InputStream in = Files.newInputStream(importPath, StandardOpenOption.READ)) {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			annotationModel = (AnnotationModel) unmarshaller.unmarshal(in);			
		} catch (Exception e) {
			AnnotationEditorPlugin.logException("Could not import annotations", e);
		}
		
		List<ClassElement> classes = null;
		for (AnnotatedClass annotatedClass : annotationModel.getAnnotatedClass()) {
			classes = model.getClasses(annotatedClass.getPackage());
			for (ClassElement classElement : classes) {
				if (classElement.getName().equals(annotatedClass.getName())) {
					Annotation nodeAnnotation = null;
					if (annotatedClass.isNode()) {
						nodeAnnotation = Annotation.create(AnnotationType.NODE);
					}
					classElement.setAnnotation(nodeAnnotation);
					
					setValueFields(annotatedClass.getValueField(), classElement);
					setValueMethods(annotatedClass.getValueMethod(), classElement);
					setInits(annotatedClass.getInitMethod(), classElement);
				}
			}
		}
	}

	private void setValueFields(List<Value> valueFields, ClassElement classElement) {
		FieldElement fieldElement = null;		
		for (Value value : valueFields) {
			fieldElement = classElement.getValuableField(value.getName(), value.getType());
			if (fieldElement != null) {
				fieldElement.setAnnotation(createValueAnnotation(value));
			}
		}
	}

	private void setValueMethods(List<Value> valueMethods, ClassElement classElement) {
		MethodElement methodElement = null;
		for (Value value : valueMethods) {
			methodElement = classElement.getValuableMethod(value.getName(), value.getType());
			if (methodElement != null) {				
				methodElement.setAnnotation(createValueAnnotation(value));
			}
		}
	}
	
	private Annotation createValueAnnotation(Value value) {
		Annotation annotation = Annotation.create(AnnotationType.VALUE);
		annotation.setAttribute("index", value.getValueIndex());
		annotation.setAttribute("name", value.getValueName());
		annotation.setAttribute("informational", value.isValueInformational());
		
		return annotation;
	}

	private void setInits(List<Init> initMethod, ClassElement classElement) {
		MethodElement methodElement = null;
		for (Init init : initMethod) {
			methodElement = classElement.getInitializableMethod(init.getName());
			if (methodElement != null) {
				methodElement.setAnnotation(createInitAnnotation(init));
			}
		}
	}
	
	private Annotation createInitAnnotation(Init init) {
		Annotation annotation = Annotation.create(AnnotationType.INITIALIZE);
		annotation.setAttribute("withParams", init.isWithParams());
		return annotation;
	}

	@Override
	public void exportAnnotations(ClassModel model, Path exportPath) {		
		AnnotationModel jaxbAnnotationModel = factory.createAnnotationModel();
		for (Entry<PackageElement, List<ClassElement>> classEntry : model.getClassElements().entrySet()) {
			for (ClassElement classElement : classEntry.getValue()) {
				AnnotatedClass annotatedClass = null;
				if (isAnnotated(classElement)) {
					annotatedClass = factory.createAnnotatedClass();
					
					annotatedClass.setName(classElement.getName());
					annotatedClass.setPackage(classElement.getPackageName());					
					annotatedClass.setNode(classElement.hasAnnotation());
					
					addValueFields(classElement.getValuableFields(), annotatedClass.getValueField());
					addValueMethods(classElement.getValuableMethods(), annotatedClass.getValueMethod());
					
					addInits(classElement.getInitializableMethods(), annotatedClass.getInitMethod());
					
					jaxbAnnotationModel.getAnnotatedClass().add(annotatedClass);
				}
			}
		}
		
		try (OutputStream out = Files.newOutputStream(exportPath, 
				StandardOpenOption.CREATE, StandardOpenOption.WRITE)){
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(jaxbAnnotationModel, out);
		} catch (Exception e) {
			AnnotationEditorPlugin.logException("Could not export annotations", e);
		}		
	}
	
	private boolean isAnnotated(ClassElement classElement) {
		return classElement.hasAnnotation() || classElement.hasValues() || classElement.hasInits();
	}
	
	private void addValueFields(List<FieldElement> fields, List<Value> valueAnnotatables) {
		Value valueAnnotated = null;		
		for (FieldElement fieldElement : fields) {
			valueAnnotated = createValue(fieldElement);
			if (valueAnnotated != null) {
				valueAnnotated.setType(fieldElement.getType());
				valueAnnotatables.add(valueAnnotated);
			}
		}
	}
	
	private void addValueMethods(List<MethodElement> methods, List<Value> valueAnnotatables) {
		Value valueAnnotated = null;		
		for (MethodElement methodElement : methods) {
			valueAnnotated = createValue(methodElement);
			if (valueAnnotated != null) {
				valueAnnotated.setType(methodElement.getType());
				valueAnnotatables.add(valueAnnotated);
			}
		}
	}

	private Value createValue(Annotatable annotatable) {
		if (annotatable.hasAnnotation(AnnotationType.VALUE)) {
			Annotation annotation = annotatable.getAnnotation();
			
			Value valueAnnotated = factory.createValue();
			valueAnnotated.setName(annotatable.getName());
			
			Map<String,Object> attributes = annotation.getAttributes();
			valueAnnotated.setValueIndex((Integer) attributes.get("index"));
			valueAnnotated.setValueName((String) attributes.get("name"));
			valueAnnotated.setValueInformational((Boolean) attributes.get("informational"));
			
			return valueAnnotated;
		}
		
		return null;
	}

	private void addInits(List<? extends Annotatable> annotatables, 
			List<Init> initAnnotatables) {
		
		Annotation annotation = null;
		Init initAnnotated = null;
		
		for (Annotatable annotatable : annotatables) {
			if (annotatable.hasAnnotation()) {
				annotation = annotatable.getAnnotation();
				
				if (annotation.getType() == AnnotationType.INITIALIZE) {
					initAnnotated = factory.createInit();
					initAnnotated.setName(annotatable.getName());
					
					Map<String,Object> attributes = annotation.getAttributes();
					initAnnotated.setWithParams((Boolean) attributes.get("withParams"));
					
					initAnnotatables.add(initAnnotated);
				}
			}
		}
	}
}
