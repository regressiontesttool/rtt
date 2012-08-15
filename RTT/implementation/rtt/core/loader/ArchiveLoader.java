package rtt.core.loader;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import rtt.core.utils.Debug;

public abstract class ArchiveLoader {

	private String baseDir = null;
	private File baseFile;

	public ArchiveLoader() {
	}

	
	public void setBasePath(File basePath) {
		this.baseFile = basePath.getAbsoluteFile();
		if (basePath.isDirectory()) {
			baseDir = basePath.getAbsolutePath();
		} else {
			File parentFile = basePath.getParentFile();

			while (parentFile != null && parentFile.isDirectory() == false) {
				parentFile.getParentFile();
			}

			baseDir = parentFile.getAbsolutePath();
		}
	}

	/**
	 * This function returns an {@link InputStream} for the given file name. If
	 * an error occurs during the creation of the stream, this function will
	 * return {@code null}.
	 * <p>
	 * The file will be searched downwards from the base file/base directory.
	 * 
	 * @param fileName
	 *            the name of the file
	 * @param folders
	 *            the path of the folder, containing the given file.
	 * @return an {@link InputStream} or {@code null}
	 * @see #setBasePath(File)
	 */
	public InputStream getInputStream(String fileName, String folders) {
		try {
			File file = new File(baseFile.getAbsolutePath() + File.separator
					+ folders + File.separator + fileName);

			return this.doGetInput(file);
		} catch (Exception e) {
			Debug.printTrace(e);
			return null;
		}
	}

	/**
	 * This function returns a {@link OutputStream} for the given file name. If
	 * an error occurs during the creation of the stream, this function will
	 * return {@code null}.
	 * <p>
	 * The file will be searched/created downwards from the base file/base directory.
	 * @param fileName
	 *            the name of the file
	 * @param folders
	 *            the path of the folder, containing the given file.
	 * @return an {@link OutputStream} or {@code null}
	 * @see #setBasePath(File)
	 */
	public OutputStream getOutputStream(String fileName, String folders) {
		try {
			File file = new File(baseFile.getAbsolutePath() + File.separator
					+ folders + File.separator + fileName);

			return this.doGetOutput(file);
		} catch (Exception e) {
			Debug.printTrace(e);
			return null;
		}
	}

	/**
	 * Returns the current base directory/file.
	 * @return a string with a path to the file/directory
	 */
	public String getBasePath() {
		return baseDir;
	}

	protected abstract OutputStream doGetOutput(File file) throws Exception;

	protected abstract InputStream doGetInput(File file) throws Exception;
}
