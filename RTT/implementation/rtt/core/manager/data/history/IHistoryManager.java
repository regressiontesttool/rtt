package rtt.core.manager.data.history;

import rtt.core.archive.history.History;

public interface IHistoryManager {
	
	History getHistory();
	
	String getSuiteName();
	String getCaseName();

}
