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

		private String name = null;
		
		public PackageElement(String name) {
			this.name = name;
		}
		
		@Node.Compare
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			PackageElement other = (PackageElement) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			return true;
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
}
