package rtt.annotations.processing2;

import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.List;

import rtt.annotations.Node.Initialize;
import rtt.core.archive.output.Type;

public abstract class InitialMember<T extends Member> 
	implements Comparable<InitialMember<?>> {
	
	private String name;
	private Type type;
	private boolean withParams;
	private Class<?>[] parameterTypes;
	
	private Initialize initAnnotation;
	private T member;
	

	public InitialMember(T member, Initialize initAnnotation) {		
		this.name = getSignature(member);
		this.type = getType(member);
		this.withParams = initAnnotation.withParams();
		this.parameterTypes = getParameterTypes(member);
		
		this.member = member;
		this.initAnnotation = initAnnotation;			
	}
	
	protected abstract String getSignature(T member);
	protected abstract Type getType(T member);
	protected abstract Class<?>[] getParameterTypes(T member);
	protected abstract Object getResult(T member, 
			InputStream input, List<String> params) throws Exception;
	
	@SuppressWarnings("unchecked")
	public boolean hasMember(Member searchedMember) {
		if (searchedMember.getClass().isInstance(member)) {
			String ownSignature = getSignature(this.member);
			String searchedSignature = getSignature((T) searchedMember);
			
			return ownSignature.equals(searchedSignature); 
		}
		
		return false;
	}

	public Initialize getAnnotation() {
		return initAnnotation;
	}
	
	public final String getName() {
		return name;
	}
	
	public final Type getType() {
		return type;
	}
	
	public boolean isWithParams() {
		return withParams;
	}
	
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	
	public final Object getResult(InputStream input, 
			List<String> params) throws Exception {
		
		return getResult(member, input, params);
	}

	@Override
	public int compareTo(InitialMember<?> other) {
		if (this.equals(other)) {
			return 0;
		}
		
		if (this.type != other.type) {
			return type.compareTo(other.type);
		}
		
		return this.name.compareTo(other.name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof InitialMember)) {
			return false;
		}
		InitialMember<?> other = (InitialMember<?>) obj;
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
