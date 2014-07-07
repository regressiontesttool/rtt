package prototype.asm.model;



public class ClassElement {

	public enum Annotation {
		EMPTY, NODE;
	}
	
	private String className;
	private String packageName;
	private String superName;
	private String[] interfaces;
	private Annotation annotation = Annotation.EMPTY;
	
	private boolean changed;
	
	public Annotation getAnnotation() {
		return annotation;
	}
	
	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getSuperName() {
		return superName;
	}

	public void setSuperClass(String superName) {
		this.superName = superName;
	}
	
	public String[] getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String[] interfaces) {
		this.interfaces = interfaces;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean hasChanged() {
		return changed;
	}
}
