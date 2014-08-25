package rtt.annotations.processing2;

import java.lang.reflect.Member;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.core.archive.output.Type;

public abstract class AnnotatedElement<T extends Member> 
	implements Comparable<AnnotatedElement<?>> {
	
	private int index;
	private String name;
	private Type type;
	private boolean informational;
	
	private T member;
	
	public AnnotatedElement(T member, Compare annotation) {
		this(annotation.name(), annotation.index(), false, member);
	}
	
	public AnnotatedElement(T member, Informational annotation) {
		this(annotation.name(), annotation.index(), false, member);
	}
	
	private AnnotatedElement(String nameAttribute, int index, 
			boolean informational, T member) {
		
		this.index = index;
		this.informational = informational;
		this.member = member;		
		
		this.type = getType(member);
		if (nameAttribute != null && !nameAttribute.trim().equals("")) {
			this.name = nameAttribute.trim();
		} else {
			this.name = getName(member);
		}		
	}
	
	protected abstract String getName(T member);
	protected abstract Type getType(T member);
	public abstract Object getResult(T member, Object object) throws Exception;
	
	public final String getName() {
		return name;
	}
	
	public final Type getType() {
		return type;
	}
	
	public final boolean isInformational() {
		return informational;
	}
	
	public final Object getResult(Object object) throws Exception {
		return getResult(member, object);
	}

	@Override
	public int compareTo(AnnotatedElement<?> other) {
		if (this.equals(other)) {
			return 0;
		}
		
		if (this.index != other.index) {
			return Integer.compare(this.index, other.index);
		}
		
		if (this.type != other.type) {
			if (this.type == Type.FIELD) {
				return -1;
			} else {
				return 1;
			}
		}
		
		if (this.informational != other.informational) {
			if (this.informational == false) {
				return -1;
			} else {
				return 1;
			}
		}
		
		return this.name.compareTo(other.name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (informational ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AnnotatedElement)) {
			return false;
		}
		AnnotatedElement<?> other = (AnnotatedElement<?>) obj;
		if (index != other.index) {
			return false;
		}
		if (informational != other.informational) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
}
