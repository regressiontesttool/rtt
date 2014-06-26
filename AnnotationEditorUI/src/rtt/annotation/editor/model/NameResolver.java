package rtt.annotation.editor.model;

import java.util.List;

public class NameResolver {
	
	public static String PACKAGE_SEPERATOR = ".";
	
	private ClassModel model;
	
	public NameResolver(ClassModel model) {
		this.model = model;
	}

	public static final void resolveModel(ClassModel model) {
		NameResolver resolver = new NameResolver(model);
		
		for (List<ClassElement> classList : model.getClassElements().values()) {
			for (ClassElement classElement : classList) {
				resolver.resolveSuperClass(classElement);
			}
		}		
	}
	
	public static final String computePackageName(String completeName) {
		String packageName = "";
		int packageIndex = completeName.lastIndexOf(PACKAGE_SEPERATOR);
		if (packageIndex > 0) {
			packageName = completeName.substring(0, packageIndex);
		}
		
		return packageName;
	}
	
	public static final String computeClassName(String completeName) {
		String className = completeName;
		int packageIndex = completeName.lastIndexOf(PACKAGE_SEPERATOR);
		if (packageIndex > 0) {
			className = completeName.substring(packageIndex + 1, 
					completeName.length());
		}
		
		return className;
	}

	private void resolveSuperClass(ClassElement classElement) {
		ElementReference<ClassElement> reference = classElement.getSuperClass();
		if (reference != null && !reference.isResolved()) {
			System.out.println("Search SuperClass: " + reference.getName());
			
			ClassElement superClass = findClass(reference.getName());
			if (superClass != null) {
				reference.setReference(superClass);
			}
		}
	}
	
	private ClassElement findClass(String completeName) {
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
