package rtt.core.loader;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import rtt.core.utils.DebugLog;

public abstract class ArchiveLoader {

	private String baseDir = null;
	private File baseFile;

	public ArchiveLoader() {
	}

	public void setBaseFile(File baseFile) {
		this.baseFile = baseFile.getAbsoluteFile();
		if (baseFile.isDirectory()) {
			baseDir = baseFile.getAbsolutePath();
		} else {
			File parentFile = baseFile.getParentFile();

			while (parentFile != null && parentFile.isDirectory() == false) {
				parentFile.getParentFile();
			}

			baseDir = parentFile.getAbsolutePath();
		}
	}

	public InputStream getInputStream(String fileName, String folders) {
		try {
			File file = new File(baseFile.getAbsolutePath() + File.separator
					+ folders + File.separator + fileName);

			return this.doGetInput(file);
		} catch (Exception e) {
			DebugLog.printTrace(e);
			return null;
		}
	}

	public OutputStream getOutputStream(String fileName, String folders) {
		try {
			File file = new File(baseFile.getAbsolutePath() + File.separator
					+ folders + File.separator + fileName);

			return this.doGetOutput(file);
		} catch (Exception e) {
			DebugLog.printTrace(e);
			return null;
		}
	}
	
	public String getBaseDir() {
		return baseDir;
	}

	protected abstract OutputStream doGetOutput(File file) throws Exception;

	protected abstract InputStream doGetInput(File file) throws Exception;
}
