package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rtt.annotations.Parser.Node;


@SuppressWarnings("rawtypes")
@Node
public class ClassModel extends ModelElement {
	
	@Node
	public static class PackageElement extends ModelElement<ClassModel> {

		public PackageElement(ClassModel parent, String name) {
			super(parent);
			super.setName(name);
		}
	}

	private Map<PackageElement, List<ClassElement>> classElements;
	
	@SuppressWarnings("unchecked")
	protected ClassModel() {
		super(null);
		classElements = new HashMap<PackageElement, List<ClassElement>>();
	}
	
	public void addClassElement(ClassElement newElement) {
		if (newElement == null) {
			throw new IllegalArgumentException("The new class element must not be null.");
		}
		
		PackageElement packageKey = createPackageKey(newElement.getPackageName(), true);
		
		List<ClassElement> elements = classElements.get(packageKey);
		if (!elements.contains(newElement)) {
			elements.add(newElement);
			packageKey.setParent(this);
		}
	}
	
	private PackageElement createPackageKey(String packageName, boolean createEntry) {
		PackageElement packageKey = new PackageElement(this, packageName);
		
		if (createEntry && !classElements.containsKey(packageKey)) {
			classElements.put(packageKey, new ArrayList<ClassElement>());
		}
		
		return packageKey;
	}

	@Node.Child
	public Map<PackageElement, List<ClassElement>> getClassElements() {
		return classElements;
	}
	
	public List<ClassElement> getClasses(String packageName) {
		PackageElement packageKey = createPackageKey(packageName, false);
		
		if (classElements.containsKey(packageKey)) {
			return classElements.get(packageKey);
		}
		
		return null;
	}
	
	public Set<PackageElement> getPackages() {
		return classElements.keySet();
	}
}
