package rtt.ui.core.archive;

import java.util.List;

public interface IToken {
	
	String getClassName();
	List<IAttribute> getAttributes();
	boolean isEndOfFile();

}
