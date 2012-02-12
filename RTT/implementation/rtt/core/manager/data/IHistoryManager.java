package rtt.core.manager.data;

import rtt.core.archive.history.History;

public interface IHistoryManager {
	
	Integer getCurrentNr();
	History getHistory();
	
	String getSuiteName();
	String getCaseName();

}
