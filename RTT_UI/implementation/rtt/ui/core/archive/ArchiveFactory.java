package rtt.ui.core.archive;

import java.io.File;


public class ArchiveFactory {
	
	public IArchive getArchive(File file) throws Exception {
		
//		if (file == null) {
//			throw new Exception("The file object must not be null");
//		}
//		
//		if (!file.exists()) {
//			throw new FileNotFoundException("The file must exist");
//		}
//		
//		for (IArchiveLoader loader : archiveLoader) {
//			if (loader.accept(file)) {
//				return loader.load(file);
//			}
//		}
		
		throw new Exception("No archive loader found");
	}
	
	

}
