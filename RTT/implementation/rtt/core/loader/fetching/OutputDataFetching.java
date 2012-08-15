package rtt.core.loader.fetching;

public class OutputDataFetching implements IFileFetching {
	
	public enum OutputType {
		LEXER ("lexer"),
		PARSER ("parser");
		
		private String name;
		
		private OutputType(String name) {
			this.name = name;
		}
	}
	
	private String path;
	private OutputType type;
	private Integer version = 0;
	
	public OutputDataFetching(String path, OutputType type) {
		this.type = type;
		this.path = path;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public String getFileName() {
		return type.name + "." + version + ".xml";
	}

	@Override
	public String getFolders() {
		return path;
	}

}
