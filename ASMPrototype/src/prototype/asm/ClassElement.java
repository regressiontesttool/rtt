package prototype.asm;

public class ClassElement {

	public enum Annotation {
		NONE, NODE;
	}
	
	private String className;
	private String superName;
	private String[] interfaces;
	private Annotation annotation = Annotation.NONE;
	
	public Annotation getAnnotation() {
		return annotation;
	}
	
	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public String getClassName() {
		return className;
	}
	
	public void setName(String className) {
		this.className = className;
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

}
