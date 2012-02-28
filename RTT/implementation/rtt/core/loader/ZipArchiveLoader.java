package rtt.core.loader;

import java.io.InputStream;
import java.io.OutputStream;

import de.schlichtherle.io.DefaultArchiveDetector;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;
import de.schlichtherle.io.archive.zip.ZipDriver;

public class ZipArchiveLoader extends ArchiveLoader {

	String archivePath;

	public ZipArchiveLoader(String folder, String fileName) {
		File.setDefaultArchiveDetector(new DefaultArchiveDetector("zip",
				new ZipDriver()));

		File archiveFile = new File(folder + File.separator + fileName);
		setBaseFile(archiveFile);
	}

	@Override
	public void setBaseFile(java.io.File baseFile) {
		File archiveFile = new File(baseFile);
		if (archiveFile.isArchive() == false) {
			throw new IllegalArgumentException(
					"The base file must be an zip archive");
		}

		super.setBaseFile(archiveFile);
	}

	@Override
	protected InputStream doGetInput(java.io.File file) throws Exception {
		File entryFile = new File(file);
		File.umount(entryFile.getTopLevelArchive(), true, false, true, false);

		if (entryFile.exists()) {
			return new FileInputStream(entryFile);
		}

		return null;
	}

	@Override
	protected OutputStream doGetOutput(java.io.File file) throws Exception {
		File entryFile = new File(file);
		entryFile.getParentFile().mkdirs();
		
		File archiveFile = entryFile.getTopLevelArchive();
		if (archiveFile.exists() == false) {
			archiveFile.mkdirs();
		}

		if (entryFile.exists() == false) {
			entryFile.createNewFile();
		}

		File.umount(archiveFile, true, false, true, false);

		return new FileOutputStream(entryFile);
	}

}
