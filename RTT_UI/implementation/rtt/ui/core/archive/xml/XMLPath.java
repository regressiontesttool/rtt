package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Path;
import rtt.ui.core.archive.IPath;

public class XMLPath implements IPath {
	
	String value;

	public XMLPath(Path path) {
		this.value = path.getValue();
	}
	
	@Override
	public String getValue() {
		return value;
	}

	public static List<IPath> getList(Configuration configuration) {
		List<IPath> list = new LinkedList<IPath>();
		
		for (Path path : configuration.getClasspath().getPath()) {
			list.add(new XMLPath(path));
		}
		
		return list;
	}

}
