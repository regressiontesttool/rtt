package rtt.core.exceptions;

public class RTTException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3828945031247021640L;

	public enum Type {
		DATA_NOT_FOUND,
		OPERATION_FAILED,
		NO_ARCHIVE
	}

	private Type type;

	public RTTException(Type type, String message) {
		super(message);
		this.type = type;
	}

	public RTTException(Type type, Throwable cause) {
		super(cause);
		this.type = type;
	}

	public RTTException(Type type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
