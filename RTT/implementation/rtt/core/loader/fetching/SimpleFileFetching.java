package rtt.core.loader.fetching;


public class SimpleFileFetching implements FileFetchingStrategy {
	
	private String fileName;
	private String folders;

	public SimpleFileFetching(String fileName, String folders) {
		super();
		this.fileName = fileName;
		this.folders = folders;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String getFolders() {
		return folders;
	}

}
