package rtt.annotations.processing2;

import java.lang.reflect.Member;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.Type;

public abstract class AnnotatedElement<T extends Member> 
	implements Comparable<AnnotatedElement<?>> {
	
	private int index;
	private String name;
	private Type type;
	private boolean informational;
	
	private Value valueAnnotation;
	private T member;

	public AnnotatedElement(T member, Value valueAnnotation) {
		
		this.member = member;
		this.valueAnnotation = valueAnnotation;
		
		this.index = valueAnnotation.index();
		this.informational = valueAnnotation.informational();		
		this.type = getType(member);
		
		String nameAttribute = valueAnnotation.name().trim();
		if (nameAttribute != null && !nameAttribute.equals("")) {
			this.name = nameAttribute;
		} else {
			this.name = getSignature(member);
		}		
	}
	
	protected abstract String getSignature(T member);
	protected abstract Type getType(T member);	
	public abstract Object getResult(T member, Object object) throws Exception;
	
	public T getMember() {
		return member;
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasMember(Member searchedMember) {
		if (searchedMember.getClass().isInstance(member)) {
			String ownSignature = getSignature(this.member);
			String searchedSignature = getSignature((T) searchedMember);
			
			return ownSignature.equals(searchedSignature); 
		}
		
		return false;
	}

	public Value getValueAnnotation() {
		return valueAnnotation;
	}
	
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
