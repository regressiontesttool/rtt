package rtt.core.utils;

import rtt.core.manager.Manager;

public class Debug {
	
	public enum LogType {
		VERBOSE,
		ALL
	}
	
	public static boolean verbose = true;

	public static void printTrace(Throwable e) {
		if (verbose) {
			e.printStackTrace();
		}
	}	
	
	public static final void log(LogType type, String data) {
		if (type == LogType.VERBOSE) {
			logVerbose(data);
		} else {
			logAll(data);
		}	
	}
	
	public static final void log(String data) {
		logAll(data);
	}
	
	private static final void logAll(String data) {
		System.out.println(data);
	}
	
	private static final void logVerbose(String data) {
		if (Manager.verbose) {
			System.out.println(data);
		}
	}

}
