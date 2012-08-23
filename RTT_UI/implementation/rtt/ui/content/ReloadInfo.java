package rtt.ui.content;

public class ReloadInfo {

	public enum Content {
		TESTSUITE, CONFIGURATION, TESTCASE, LOG, TESTRUN, DETAIL, PROJECT
	}
	
	private Content[] contents;
	
	public ReloadInfo(Content... contents) {
		this.contents = contents;
	}
	
	public boolean contains(Content content) {
		for (Content temp : contents) {
			if (content == temp) {
				return true;
			}
		}
		
		return false;
	}

}
