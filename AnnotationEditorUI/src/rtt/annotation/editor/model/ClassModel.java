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
	public static class PackageElement extends NamedModelElement<ClassModel> {

		public PackageElement(String name) {
			setName(name);
		}		
	}

	private Map<PackageElement, List<ClassElement>> classElements;
	
	protected ClassModel() {
		classElements = new HashMap<PackageElement, List<ClassElement>>();
	}
	
	public void addClassElement(ClassElement newElement) {
		PackageElement packageElement = 
				new PackageElement(newElement.getPackageName());		
		
		if (!classElements.containsKey(packageElement)) {
			classElements.put(packageElement, new ArrayList<ClassElement>());
		}
		
		List<ClassElement> elements = classElements.get(packageElement);
		if (!elements.contains(newElement)) {
			elements.add(newElement);
			packageElement.setParent(this);
		}
	}
	
	@Node.Child
	public Map<PackageElement, List<ClassElement>> getClassElements() {
		return classElements;
	}
	
	public List<ClassElement> getClassElement(String packageName) {
		PackageElement packageElement = new PackageElement(packageName);
		
		if (classElements.containsKey(packageElement)) {
			return classElements.get(packageElement);
		}
		
		return null;
	}
	
	public Set<PackageElement> getPackages() {
		return classElements.keySet();
	}
	
	@Override
	@Node.Compare
	public String getLabel() {
		return toString();
	}
}
