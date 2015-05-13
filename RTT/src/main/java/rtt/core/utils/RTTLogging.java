package rtt.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RTTLogging {
	
	private static final Logger LOG = LoggerFactory.getLogger("RTT");
	
	public static void info(String infoMessage) {		
		LOG.info(infoMessage);
	}
	
	public static void debug(String debugMessage) {
		LOG.debug(debugMessage);
	}
	
	public static void debug(String debugMessage, Throwable throwable) {
		LOG.debug(debugMessage, throwable);
	}
	
	public static void error(String errorMessage) {
		LOG.error(errorMessage);
	}
	
	public static void error(String errorMessage, Throwable throwable) {
		LOG.error(errorMessage, throwable);
	}
	
	public static <T extends Throwable> void throwException(T throwable) throws T {
		LOG.error(throwable.getMessage());
		throw throwable;
	}
	
	public static void trace(String traceMessage) {
		LOG.trace(traceMessage);
	}
	
	public static void trace(String traceMessage, Throwable throwable) {
		LOG.warn(traceMessage);
		LOG.trace(traceMessage, throwable);
	}
	
	public static void warn(String warnMessage) {
		LOG.warn(warnMessage);
	}

	
}
