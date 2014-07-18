package rtt.core.loader;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.utils.RTTLogging;

public abstract class ArchiveLoader {
	
	public static class NotSupportedArchiveType extends RTTException {

		private static final long serialVersionUID = -8229231611431198987L;

		private NotSupportedArchiveType(String message) {
			super(Type.OPERATION_FAILED, message);
		}
		
		public static NotSupportedArchiveType create(File path) {
			String message = "Not supported archive type: " + path;
			return new NotSupportedArchiveType(message);
		}
		
	}

	private String baseDir = null;
	private File baseFile;

	protected ArchiveLoader() {}
	
	public static ArchiveLoader create(File path) throws RTTException {
		boolean supportedArchiveType = path.isDirectory() || path.getPath().endsWith("zip");
		if (!supportedArchiveType) {
			throw NotSupportedArchiveType.create(path);
		}			
		
		File aPath = path.getAbsoluteFile();
		if (aPath == null) {
			throw new RTTException(Type.NO_ARCHIVE, "Absolute file of '"
					+ path.getAbsolutePath() + "' returned null.");
		}
		
		return new ZipArchiveLoader();
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
			
			if (parentFile != null) {
				baseDir = parentFile.getAbsolutePath();
			} else {
				throw new IllegalAccessError("Parent file was null. Base path: " + basePath);
			}
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
			RTTLogging.error("Could not retrieve InputStream", e);
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
			RTTLogging.error("Could not retrieve OutputStream", e);
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

	public void close() {}
}
