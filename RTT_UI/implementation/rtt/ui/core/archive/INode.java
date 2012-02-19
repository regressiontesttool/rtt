package rtt.ui.core.archive;

import java.util.List;

public interface INode {
	
	String getClassName();
	List<IAttribute> getAttributes();
	List<INode> getChildernNodes();
	boolean isNull();

}
