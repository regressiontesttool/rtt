package rtt.ui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Path;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.Manager;
import rtt.core.manager.Manager.TestCaseMode;
import rtt.core.manager.data.ConfigurationManager;
import rtt.core.utils.GenerationInformation;
import rtt.ui.RttLog;
import rtt.ui.RttPluginUtil;

public class RttProject {

	private String name;
	
	private IJavaProject javaProject;
	private IFile archiveFile;
	
	private Manager manager;

	public RttProject(IJavaProject javaProject) throws RTTException,
			CoreException {
		this.javaProject = javaProject;

		IProject project = javaProject.getProject();
		archiveFile = RttPluginUtil.getArchiveFile(project);
		manager = RttPluginUtil.getRttArchive(archiveFile.getLocation().toFile());

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

	public ArchiveLoader getLoader() {
		return manager.getArchive().getLoader();
	}

	public Configuration getActiveConfiguration() {
		return manager.getArchive().getActiveConfiguration();
	}

	public void save() throws RTTException {
		manager.saveArchive(archiveFile.getLocation().toFile());
	}

	public void addConfiguration(String lexerClass, String parserClass,
			String configName, boolean makeDefault, List<String> cp)
			throws RTTException {

		manager.setConfiguration(configName, lexerClass, parserClass,
				cp, makeDefault, true);
	}

	public boolean setActiveConfiguration(String configName) {
		return manager.getArchive().setActiveConfiguration(configName);
	}

	public void addClassPathEntry(Configuration config, String entry)
			throws RTTException {
		ConfigurationManager.addClasspathEntry(config, entry);
	}

	public void removeClasspathEntry(Configuration config, String entry)
			throws RTTException {
		ConfigurationManager.addClasspathEntry(config, entry);
	}

	public boolean addTestsuite(String suiteName) throws RTTException {
		return manager.createTestSuite(suiteName);
	}

	public boolean removeTestsuite(String suiteName) throws RTTException {
		return manager.removeTestsuite(suiteName);
	}

	public List<RTTException> addTestcase(String suiteName, List<File> files) {
		return manager.addAllFiles(files, suiteName, TestCaseMode.OVERWRITE);		
	}

	public void removeTestcase(String suiteName, String caseName)
			throws RTTException {
		manager.removeTest(suiteName, caseName);
	}

	public GenerationInformation generateTests(String suiteName) throws RTTException {
		return manager.generateTests(suiteName);
	}

	public void runTests(String suiteName, boolean matching)
			throws RTTException {
		manager.runTests(suiteName, matching);
	}

	public IJavaProject getJavaProject() {
		return javaProject;
	}

	public IPath getArchivePath(boolean cutOf) {
		IPath archivePath = archiveFile.getProjectRelativePath();
		
		if (cutOf) {
			return archivePath.removeLastSegments(1);
		}
		
		return archivePath;
	}

	public Configuration createEmptyConfiguration() {
		Configuration config = new Configuration();
		config.setName("myConfig");
		config.setLexerClass("");
		config.setParserClass("");

		Classpath classpath = new Classpath();
		try {
			IPath outputLocation = javaProject.getOutputLocation();
			outputLocation = outputLocation.removeFirstSegments(1);
			
			Path path = new Path();
			path.setValue(outputLocation.makeRelativeTo(getArchivePath(true)).toPortableString());
			classpath.getPath().add(path);
		} catch (JavaModelException exception) {
			RttLog.log(exception);
		}
		
		config.setClasspath(classpath);
		
		return config;
	}
}
