package rtt.annotation.editor.data;


import java.util.List;

import org.eclipse.core.runtime.Status;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ElementReference;

public class NameResolver {
	
	private String packageSeparator = ".";

	public NameResolver(String packageSeparator) {
		this.packageSeparator = packageSeparator;
	}
	
	public static final NameResolver create(String packageSeparator) {
		return new NameResolver(packageSeparator);
	}
	
	public final String computePackageName(String completeName) {
		int packageBorder = computePackageBorder(completeName);
		if (packageBorder > 0) {
			return completeName.substring(0, packageBorder);
		}
		
		return "";
	}

	public final String computeClassName(String completeName) {
		int packageBorder = computePackageBorder(completeName);
		if (packageBorder > 0) {
			return completeName.substring(packageBorder + 1);
		}
		
		return "";
	}
	
	private int computePackageBorder(String completeName) {
		return completeName.lastIndexOf(packageSeparator);
	}

	public final void resolveModel(ClassModel model) {
		for (List<ClassElement> classList : model.getClassElements().values()) {
			for (ClassElement classElement : classList) {
				resolveSuperClass(classElement, model);
				resolveInterfaces(classElement, model);
			}
		}		
	}

	private void resolveSuperClass(ClassElement classElement, ClassModel model) {
		ElementReference<ClassElement> reference = classElement.getSuperClass();
		if (reference != null && !reference.isResolved()) {
			AnnotationEditorPlugin.log(Status.INFO, "Search SuperClass: " + reference.getName());
			
			ClassElement superClass = findClass(reference.getName(), model);
			if (superClass != null) {
				reference.setReference(superClass);
			}
		}
	}
	
	private void resolveInterfaces(ClassElement classElement, ClassModel model) {
		List<ClassElementReference> interfaces = classElement.getInterfaces();
		if (interfaces != null && !interfaces.isEmpty()) {
			for (ElementReference<ClassElement> interfaceRef : interfaces) {
				interfaceRef.setReference(resolveInterface(interfaceRef, model));
			}
		}
	}
	
	private ClassElement resolveInterface(ElementReference<ClassElement> interfaceRef, ClassModel model) {
		if (interfaceRef != null && !interfaceRef.isResolved()) {
			AnnotationEditorPlugin.log(Status.INFO, "Searching Interface: " + interfaceRef.getName());
			ClassElement interfaceElement = findClass(interfaceRef.getName(), model);
			if (interfaceElement != null) {
				return interfaceElement;
			}
		}
		
		return null;
	}

	public ClassElement findClass(String completeName, ClassModel model) {
		String packageName = computePackageName(completeName);
		String className = computeClassName(completeName);
		
		List<ClassElement> classes = model.getClasses(packageName);
		if (classes != null && !classes.isEmpty()) {
			for (ClassElement element : classes) {
				if (element.getName().equals(className)
						&& element.getPackageName().equals(packageName)) {
					return element;
				}
			}
		}
		
		return null;
	}

}
