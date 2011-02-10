/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.archive.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import javax.xml.bind.MarshalException;

import rtt.archive.Testarchive;
import rtt.logging.ArchiveLog;
import de.schlichtherle.io.ArchiveBusyException;
import de.schlichtherle.io.ArchiveBusyWarningException;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.ArchiveOutputBusyException;
import de.schlichtherle.io.DefaultArchiveDetector;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;
import de.schlichtherle.io.FileReader;
import de.schlichtherle.io.archive.zip.Zip32Driver;

/**
 * A loader based on TrueZip can load/save zip and normal directories
 * 
 * @author Peter Mucha
 * 
 */
public class TrueZipLoader extends ArchiveLoader {
	private String basePath;

	/**
	 * loads an archive from a directory
	 * 
	 * @param directory
	 *            the directory, where archive.xml resides
	 * @return the parsed testarchive
	 * @throws FileNotFoundException
	 */
	@Override
	public Testarchive loadArchive(java.io.File directory) throws Exception {
		basePath = directory.getAbsolutePath() + File.separator;
		ReferenceConverter.setCurrentArchiveLoader(this);

		Testarchive archive = this.unmarshal(Testarchive.class, "archive.xml");

		File.umount();
		return archive;
	}

	@Override
	public void saveArchive(Testarchive archive, java.io.File directory)
			throws Exception {
		File.setDefaultArchiveDetector(new DefaultArchiveDetector("zip",
				new Zip32Driver()));
		basePath = directory.getAbsolutePath() + File.separator;
		ReferenceConverter.setCurrentArchiveLoader(this);
		File zip = new File(directory.getAbsolutePath()); // zip-enabled file
															// from de.sch...
		if (!zip.exists())
			zip.mkdir();

		this.marshal(Testarchive.class, archive, "archive.xml", false);

		File.umount();
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

		File.umount();
		return log;
	}

	@Override
	public void saveLog(ArchiveLog log, java.io.File directory)
			throws Exception {
		String name = "log.xml";

		if (directory.isDirectory() || directory.getName().endsWith("zip"))
			basePath = directory.getAbsolutePath() + File.separator;
		else {
			basePath = directory.getCanonicalFile().getParent()
					+ File.separator;
			name = directory.getName();
		}
		ReferenceConverter.setCurrentArchiveLoader(this);
		this.marshal(ArchiveLog.class, log, name, true);

		File.umount();
	}

	@Override
	public <T> T unmarshal(Class<T> docClass, String fileName) throws Exception {

		FileInputStream fis = new FileInputStream(basePath + fileName);
		T value;
		try {
			value = unmarshal(docClass, fis);
		} finally {
			fis.close();
		}
		return value;
	}

	@Override
	public <T> void marshal(Class<T> docClass, T instance, String fileName,
			boolean xsltStyle) throws Exception {

		
		
		FileOutputStream fos = new FileOutputStream(basePath + fileName, false);
		
		try {
			marshal(docClass, instance, fos, xsltStyle);
		}
		catch(Throwable me)
		{
			me.printStackTrace();
		
		} finally {
			//fos.flush();
			fos.close();
		}
	}

	@Override
	public String loadInput(String fileName) throws Exception {
		// System.out.println("loading: " + fileName);
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(basePath
				+ fileName));
		try {
			char[] buf = new char[1024];

			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} finally {
			reader.close();
//			File.update();
//			File.umount();
		}
		return fileData.toString();

	}

	@Override
	public void saveInput(String fileName, String fileContent) throws Exception {
		// System.out.println("saving: " + fileName);
		OutputStream os = new FileOutputStream(basePath + fileName);
		try {
			os.write(fileContent.getBytes());
		} finally {
			os.flush();
			os.close();
		}
	}

	@Override
	public void closeFile() {
		try {
			File.update();
			File.umount();
		} catch (ArchiveException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveFileToPath(String oldFile, String newFile) throws Exception {
		closeFile();
		File o = new File(basePath + oldFile);

		if (!o.exists())
			return;

		File n = new File(basePath + newFile);

		// System.out.println(o.toString());
		// System.out.println(n.toString());
		// File.cp(o, n);
		o.copyTo(n);
		o.delete();
	}

}
