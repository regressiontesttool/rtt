package rtt.core.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class loads and saves all data from a specified folder.
 * 
 * This class is marked as deprecated, because the functionality of 
 * this class is not tested at the moment.
 * 
 * @author Christian Oelsner
 */
@Deprecated
public class DirectoryArchiveLoader extends ArchiveLoader {
	
	public DirectoryArchiveLoader(String basePath) {
		File base = new File(basePath);
		if (base.exists() == false) {
			base.mkdirs();
		}
		
		setBasePath(base);
	}

	@Override
	protected InputStream doGetInput(File file) throws FileNotFoundException {
//		File file = new File(LoaderUtils.getPath(baseDir, folders));
		
		if (file.exists() == false) {
			return null;
		}
		
//		file = new File(file, fileName);
//		if (file.exists() == false) {
//			return null;
//		}		
		
		return new FileInputStream(file);
	}

	@Override
	protected OutputStream doGetOutput(File file) throws FileNotFoundException {
//		File directory = new File(LoaderUtils.getPath(baseDir, folders));
		
//		DebugLog.log("OutputStream: " + directory.getAbsolutePath());
		file.getParentFile().mkdirs();
		
		return new FileOutputStream(file);
	}

}
