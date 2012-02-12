package rtt.core.loader;

import java.io.File;

public class LoaderUtils {
	public static final String getPath(String... elements) {
		return getFilePath(null, elements);
	}
	
	public static final String getFilePath(String fileName, String... pathElements) {
		StringBuilder path = new StringBuilder();
		
		int i = 0;
		for (String element : pathElements) {
			if (element != null && element.equals("") == false) {
				path.append(element);
				
				if (element.endsWith(File.separator) == false) {
					path.append(File.separator);
				}			
				
				i++;
			}
		}
		
		if (fileName != null && fileName.equals("") == false) {
			path.append(fileName);
		}
		
		return path.toString();
	}
}
