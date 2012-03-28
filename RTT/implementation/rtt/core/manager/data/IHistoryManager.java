package rtt.core.manager.data;

import rtt.core.archive.history.History;

public interface IHistoryManager {
	
	History getHistory();
	
	String getSuiteName();
	String getCaseName();

}
