package rtt.ui.core.archive;

import java.util.List;


public interface IConfiguration {
	String getName();
	String getDescription();
	boolean isDefault();
	
	void setActive(boolean isActive);
	boolean isActive();
	
	ILexerClass getLexerClass();
	IParserClass getParserClass();
	List<IPath> getClasspathEntries();
}
