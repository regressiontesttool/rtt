package rtt.annotation.editor.data.jaxb;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
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
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

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
					NodeAnnotation nodeAnnotation = null;
					if (annotatedClass.isNode()) {
						nodeAnnotation = Annotation.create(NodeAnnotation.class);
					}
					classElement.setAnnotation(nodeAnnotation);
					classElement.setChanged();
					
					setValueFields(annotatedClass.getValueField(), classElement);
					setValueMethods(annotatedClass.getValueMethod(), classElement);
					setInits(annotatedClass.getInitMethod(), classElement);
				}
			}
		}
	}

	private void setValueFields(List<Value> valueFields, ClassElement classElement) {
		FieldElement<ValueAnnotation> fieldElement = null;		
		for (Value value : valueFields) {
			fieldElement = classElement.getValuableField(value.getName(), value.getType());
			if (fieldElement != null) {
				fieldElement.setAnnotation(createValueAnnotation(value));
				fieldElement.setChanged();
			}
		}
	}

	private void setValueMethods(List<Value> valueMethods, ClassElement classElement) {
		MethodElement<ValueAnnotation> methodElement = null;
		for (Value value : valueMethods) {
			methodElement = classElement.getValuableMethod(value.getName(), value.getType());
			if (methodElement != null) {				
				methodElement.setAnnotation(createValueAnnotation(value));
				methodElement.setChanged();
			}
		}
	}
	
	private ValueAnnotation createValueAnnotation(Value value) {
		ValueAnnotation annotation = Annotation.create(ValueAnnotation.class);
		annotation.setValueIndex(value.getValueIndex());
		annotation.setValueName(value.getValueName());
		annotation.setInformational(value.isValueInformational());
		
		return annotation;
	}

	private void setInits(List<Init> initMethod, ClassElement classElement) {
		MethodElement<InitAnnotation> methodElement = null;
		for (Init init : initMethod) {
			methodElement = classElement.getInitializableMethod(init.getName());
			if (methodElement != null) {
				methodElement.setAnnotation(createInitAnnotation(init));
				methodElement.setChanged();
			}
		}
	}
	
	private InitAnnotation createInitAnnotation(Init init) {
		InitAnnotation annotation = Annotation.create(InitAnnotation.class);
		annotation.setWithParams(init.isWithParams());
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
		return classElement.hasAnnotation() || 
				classElement.hasAnnotatedValueMembers() || 
				classElement.hasAnnotatedInitializeMembers();
	}
	
	private void addValueFields(List<FieldElement<ValueAnnotation>> fields, List<Value> valueAnnotatables) {
		Value valueAnnotated = null;		
		for (FieldElement<ValueAnnotation> fieldElement : fields) {
			valueAnnotated = createValue(fieldElement);
			if (valueAnnotated != null) {
				valueAnnotated.setType(fieldElement.getType());
				valueAnnotatables.add(valueAnnotated);
			}
		}
	}
	
	private void addValueMethods(List<MethodElement<ValueAnnotation>> methods, List<Value> valueAnnotatables) {
		Value valueAnnotated = null;		
		for (MethodElement<ValueAnnotation> methodElement : methods) {
			valueAnnotated = createValue(methodElement);
			if (valueAnnotated != null) {
				valueAnnotated.setType(methodElement.getType());
				valueAnnotatables.add(valueAnnotated);
			}
		}
	}

	private Value createValue(Annotatable<ValueAnnotation> annotatable) {
		Value valueAnnotated = null;
		
		if (annotatable.hasAnnotation()) {
			valueAnnotated = factory.createValue();
			valueAnnotated.setName(annotatable.getName());
			
			ValueAnnotation annotation = annotatable.getAnnotation();		
			
			valueAnnotated.setValueIndex(annotation.getValueIndex());
			valueAnnotated.setValueName(annotation.getValueName());
			valueAnnotated.setValueInformational(annotation.isInformational());
		}
		
		return valueAnnotated;
	}

	private void addInits(List<? extends Annotatable<InitAnnotation>> annotatables, 
			List<Init> initAnnotatables) {
		
		Init initAnnotated = null;		
		for (Annotatable<InitAnnotation> annotatable : annotatables) {
			if (annotatable.hasAnnotation()) {
				initAnnotated = factory.createInit();
				initAnnotated.setName(annotatable.getName());
				
				InitAnnotation annotation = annotatable.getAnnotation();				
				initAnnotated.setWithParams(annotation.isWithParams());
				
				initAnnotatables.add(initAnnotated);
			}
		}
	}
}
