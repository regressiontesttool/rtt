package rtt.core.tests.junit;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import rtt.core.exceptions.RTTException;
import rtt.core.loader.ArchiveLoader.NotSupportedArchiveType;
import rtt.core.manager.Manager;

public class ManagerInitializeTests {

	private Path dirExisting;
	private Path dirNonExisting;
	
	private Path fileExisting;
	private Path fileNonExisting;
	
	private Path zipExisting;
	private Path zipNonExisting;
	
	private Path zipTempCreating;
	private Path zipSimpleFolderCreating;	
	private Path zipComplexFolderCreating;

	@Before
	public void setUp() throws Exception {
		dirExisting = Paths.get("tests/testset/").toAbsolutePath();
		dirNonExisting = Paths.get("non/existing/").toAbsolutePath();
		
		fileExisting = Paths.get("tests/testset/ccc/1.testcase");
		fileNonExisting = Paths.get("non/existing/file.txt");
		
		zipExisting = Paths.get("tests/archives/archive.zip");
		zipNonExisting = Paths.get("tests/archives/nonExisting.zip");
		
		zipTempCreating = Paths.get("tests/archives/tempArchive.zip");
		zipSimpleFolderCreating = Paths.get("tests/archives/temp/tempArchive.zip");
		zipComplexFolderCreating = Paths.get("tests/archives/temp/temp2/tempArchive.zip");
	}
	
	private Manager createManager(Path path) throws RTTException {
		return new Manager(path.toFile(), true);
	}
	
	@Test(expected=NotSupportedArchiveType.class)
	public void testNonExistingDirectory() throws Exception {
		createManager(dirNonExisting);
	}
	
	@Test(expected=NotSupportedArchiveType.class)
	public void testExistingDirectory() throws Exception {		
		createManager(dirExisting);
	}
	
	@Test(expected=NotSupportedArchiveType.class)
	public void testNonExistingFile() throws Exception {
		createManager(fileNonExisting);
	}
	
	@Test(expected=NotSupportedArchiveType.class)
	public void testExistingFile() throws Exception {
		createManager(fileExisting);
	}
	
	@Test(expected=RTTException.class)
	public void testNonExistingZip() throws Exception {
		Manager manager = createManager(zipNonExisting);
		manager.loadArchive(zipNonExisting.toFile());
	}
	
	private void createArchive(Path archivePath) throws Exception  {
		assertFalse("Temporary archive already exists.", 
				Files.exists(archivePath));
		
		Manager manager = createManager(archivePath);
		manager.createArchive(archivePath.toFile());
		manager.close();
		
		assertTrue("Temporary archive was not created.",
				Files.exists(archivePath));
		
		Files.delete(archivePath);
		
		assertFalse("Temporary archive could not be deleted.",
				Files.exists(archivePath));
	}
	
	@Test
	public void testCreatingZip() throws Exception {
		createArchive(zipTempCreating);
	}
	
	@Test
	public void testCreatingSimpleFolderZip() throws Exception {
		createArchive(zipSimpleFolderCreating);
	}
	
	@Test
	public void testCreatingComplexFolderZip() throws Exception {
		createArchive(zipComplexFolderCreating);
	}
	
	@Test
	public void testExistingZip() throws Exception {
		Manager manager = createManager(zipExisting);
		manager.loadArchive(zipExisting.toFile());
	}

}
