package prototype.asm.model;

public class Source {
	
	private Object source;
	
	public Source(Object source) {
		this.source = source;
	}

	@SuppressWarnings("unchecked")
	public <T> T adapt(Class<T> type) {
		T result = null;
		
		if (type != null && type.isInstance(source)) {
			result = (T) source;
		}
		
		return result;
	}
	
}
