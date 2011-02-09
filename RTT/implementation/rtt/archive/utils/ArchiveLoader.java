/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.archive.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import rtt.archive.Testarchive;
import rtt.logging.ArchiveLog;
import rtt.managing.Archive;

/**
 * Abstract foundation for loading archives. Provides some utility methods as
 * marshalling and unmarshalling.
 * 
 * 
 * @author Peter Mucha
 * 
 */
public abstract class ArchiveLoader {
	protected String currentConfiguration;
	protected Archive currentArchive;

	public void setCurrentArchive(Archive currentArchive) {
		this.currentArchive = currentArchive;
	}

	public static <T> T unmarshal(Class<T> docClass, InputStream inputStream)
			throws JAXBException {
		// String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(new Class[] { docClass });
		Unmarshaller u = jc.createUnmarshaller();
		T doc = (T) u.unmarshal(inputStream);
		return doc; // .getValue();
	}

	public static <T> void marshal(Class<T> docClass, T instance,
			OutputStream outputStream, boolean xsltStyle) throws JAXBException {
		// String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(new Class[] { docClass });
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

		if (xsltStyle) {
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			try {
				outputStream.write("<?xml version='1.0'?>".getBytes());
				outputStream
						.write("<?xml-stylesheet type='text/xsl' href='log.xslt' ?>"
								.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		m.marshal(instance, outputStream);
	}

	public abstract Testarchive loadArchive(java.io.File directory)
			throws Exception;

	public abstract ArchiveLog loadLog(java.io.File directory) throws Exception;

	public abstract void saveArchive(Testarchive archive, java.io.File directory)
			throws Exception;

	public abstract void saveLog(ArchiveLog log, java.io.File directory)
			throws Exception;

	public abstract <T> T unmarshal(Class<T> docClass, String fileName)
			throws Exception;

	public abstract <T> void marshal(Class<T> docClass, T instance,
			String fileName, boolean xsltStyle) throws Exception;

	public void setCurrentConfiguration(String c) {
		currentConfiguration = c;
	}

	public String getCurrentConfiguration() {
		if (currentArchive.getArchive() == null)
			return currentConfiguration;
		if (currentConfiguration == null || currentConfiguration.isEmpty())
			return currentArchive.getArchive().getConfigurations().getDefault();

		return currentConfiguration;
	}

	private HashMap<Object, String> fileNames = new HashMap<Object, String>();

	public void setFileFor(Object o, String f) {
		setFileFor(o, f, false);
	}

	public void setFileFor(Object o, String f, boolean resolveNameCollision) {

		// System.out.println("setting fileRef: ("+resolveNameCollision+") " + o
		// + " <-> " + f);
		if (!resolveNameCollision) {
			fileNames.put(o, f);
		} else {
			String fileName = f;
			if (fileNames.containsValue(f)) {

				// find unique fileReference
				int i = 0;
				while (fileNames.containsValue(f + i))
					i++;

				fileName = f + i;
			}

			fileNames.put(o, fileName);

		}
	}

	public String getFileName(Object o) {
		return fileNames.get(o);
	}

	public void addAllReferencesTo(ArchiveLoader secondArchiveLoader) {
		Set<Entry<Object, String>> s = fileNames.entrySet();
		for (Entry<Object, String> e : s)
			secondArchiveLoader.setFileFor(e.getKey(), e.getValue(), true);

	}

	public abstract String loadInput(String fileName) throws Exception;

	public abstract void saveInput(String fileName, String fileContent)
			throws Exception;

	public abstract void closeFile();

	public abstract void moveFileToPath(String oldFile, String newFile)
			throws Exception;
}
