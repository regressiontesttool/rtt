package rtt.core.loader.fetching;

public abstract class HistoryFileFetching implements FileFetchingStrategy {

	private String folders;
	
	public HistoryFileFetching(String folders) {
		super();
		this.folders = folders;
	}

	@Override
	public final String getFileName() {
		return "history.xml";
	}
	
	@Override
	public final String getFolders() {
		return folders;
	}
	
	public abstract String getFileName(Integer version);

}
