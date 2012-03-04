package rtt.ui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.Manager;
import rtt.core.manager.Manager.TestCaseMode;
import rtt.ui.RttPluginUtil;
import rtt.ui.content.main.ProjectContent;

public class RttProject {

	private String name;
	private IProject project;
	private File archiveFile;
	private Manager manager;

	public RttProject(IProject project) throws RTTException, CoreException {
		this.project = project;
		archiveFile = RttPluginUtil.getArchiveFile(project);
		manager = RttPluginUtil.getRttArchive(archiveFile);

		this.name = project.getDescription().getName();
	}

	public String getName() {
		return name;
	}
	
	public List<String> getTestsuiteNames() {
		List<String> suiteNames = new ArrayList<String>();
		for (Testsuite suite : manager.getArchive().getTestsuites(false)) {
			suiteNames.add(suite.getName());
		}
		
		return suiteNames;
	}

	public Archive getArchive() {
		return manager.getArchive();
	}
	
	public IProject getWorkspaceProject() {
		return project;
	}
	
	public ArchiveLoader getLoader() {
		return manager.getArchive().getLoader();
	}

	public Configuration getActiveConfiguration() {
		return manager.getArchive().getActiveConfiguration();
	}
	
	public void save() throws RTTException {
		manager.saveArchive(archiveFile);
	}
	
	public void addConfiguration(String lexerClass, String parserClass,
			String configName, boolean makeDefault, List<String> cp)
			throws RTTException {

		manager.createConfiguration(lexerClass, parserClass, configName,
				makeDefault, true, cp);
	}

	public void setActiveConfiguration(String configName) {
		manager.getArchive().setActiveConfiguration(configName);
	}

	public void addClassPathEntry(String configName, String entry) throws RTTException {
		manager.addClassPathEntry(configName, entry);
	}

	public void removeClasspathEntry(String configName, String entry) throws RTTException {
		manager.removeClassPathEntry(configName, entry);
	}

	public void addTestsuite(String suiteName) throws RTTException {
		manager.createTestSuite(suiteName);
	}

	public void removeTestsuite(String suiteName) throws RTTException {
		manager.removeTestsuite(suiteName);
	}

	public void addTestcase(String text, IFile file) throws RTTException {
		manager.addFile(file.getLocation().toFile(), text, TestCaseMode.OVERWRITE);
	}

	public void removeTestcase(String suiteName, String caseName) throws RTTException {
		manager.removeTest(suiteName, caseName);
	}
	
	public List<Throwable> generateTests(String suiteName) throws RTTException {
		return manager.generateTests(suiteName);
	}

	public void runTests(String suiteName, boolean matching) throws RTTException {
		manager.runTests(suiteName, matching);
	}

	public IPath find(String path) {
		IResource resource = project.findMember(new Path(path));
		if (resource != null) {
			return resource.getLocation();
		} 
		
		return null;		
	}
}
