package rtt.core.loader.fetching;

public class OutputDataFetching implements IFileFetching {
	
	private String path;
	private Integer version = 0;
	
	public OutputDataFetching(String path) {
		this.path = path;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public String getFileName() {
		return "output." + version + ".xml";
	}

	@Override
	public String getFolders() {
		return path;
	}

}
