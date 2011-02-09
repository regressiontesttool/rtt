/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.archive.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;

import rtt.archive.Testarchive;
import rtt.logging.ArchiveLog;

/**
 * 
 * @author Peter Mucha
 * 
 */
@Deprecated
public class DirectoryLoader extends ArchiveLoader {
	private String basePath;

	/**
	 * loads an archive from a directory
	 * 
	 * @param directory
	 *            the directory, where archive.xml resides
	 * @return the parsed testarchidve
	 * @throws FileNotFoundException
	 */
	@Override
	public Testarchive loadArchive(java.io.File directory) throws Exception {
		basePath = directory.getAbsolutePath() + File.separator;
		ReferenceConverter.setCurrentArchiveLoader(this);

		Testarchive archive = this.unmarshal(Testarchive.class, "archive.xml");

		return archive;
	}

	@Override
	public void saveArchive(Testarchive archive, java.io.File directory)
			throws Exception {
		basePath = directory.getAbsolutePath() + File.separator;
		ReferenceConverter.setCurrentArchiveLoader(this);

		this.marshal(Testarchive.class, archive, "archive.xml", false);

	}

	/**
	 * loads an log from the archive directory
	 * 
	 * @param directory
	 *            the directory, where archive.xml resides
	 * @return the parsed testarchive
	 * @throws FileNotFoundException
	 */
	@Override
	public ArchiveLog loadLog(java.io.File directory) throws Exception {
		basePath = directory.getAbsolutePath() + File.separator;
		ReferenceConverter.setCurrentArchiveLoader(this);

		ArchiveLog log = this.unmarshal(ArchiveLog.class, "log.xml");

		return log;
	}

	@Override
	public void saveLog(ArchiveLog log, java.io.File directory)
			throws Exception {
		basePath = directory.getAbsolutePath() + File.separator;
		ReferenceConverter.setCurrentArchiveLoader(this);

		this.marshal(ArchiveLog.class, log, "log.xml", false);

	}

	@Override
	public <T> T unmarshal(Class<T> docClass, String fileName) throws Exception {

		FileInputStream fis = new FileInputStream(basePath + fileName);
		T value = unmarshal(docClass, fis);
		fis.close();
		return value;
	}

	@Override
	public <T> void marshal(Class<T> docClass, T instance, String fileName,
			boolean xsltStyle) throws Exception {

		FileOutputStream fos = new FileOutputStream(basePath + fileName, false);
		marshal(docClass, instance, fos, false);
		fos.close();

	}

	@Override
	public String loadInput(String fileName) throws Exception {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(basePath
				+ fileName));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();

	}

	@Override
	public void saveInput(String fileName, String fileContent) throws Exception {
		OutputStream os = new FileOutputStream(basePath + fileName);
		os.write(fileContent.getBytes());
		os.close();
	}

	@Override
	public void closeFile() {
		// dont do anything

	}

	@Override
	public void moveFileToPath(String oldFile, String newFile) throws Exception {
		// TODO Auto-generated method stub

	}
}
