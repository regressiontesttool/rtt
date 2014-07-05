package prototype.asm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassModel {
	
	public static class PackageElement {
		private String name;
		
		public PackageElement(String packageName) {
			this.name = packageName;
		}

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PackageElement other = (PackageElement) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}
	
	private Map<PackageElement, List<ClassElement>> classes;
	
	private ClassModel() {
		classes = new HashMap<ClassModel.PackageElement, List<ClassElement>>();
	}
	
	public <T> void addClass(ClassElement classElement) {
		String packageName = classElement.getPackageName();
		if (packageName.equals("")) {
			packageName = "<default>";
		}
		
		PackageElement key = new PackageElement(packageName);
		if (!classes.containsKey(key)) {
			classes.put(key, new ArrayList<ClassElement>());
		}
		
		classes.get(key).add(classElement);		
	}

	public static ClassModel create() {
		return new ClassModel();
	}
	
	public Map<PackageElement, List<ClassElement>> getClasses() {
		return classes;
	}

	public List<ClassElement> getClasses(PackageElement packageElement) {
		return classes.get(packageElement);
	}
}
