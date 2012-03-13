package rtt.ui.model;

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
import rtt.core.archive.configuration.LexerClass;
import rtt.core.archive.configuration.ParserClass;
import rtt.core.archive.configuration.Path;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.Manager;
import rtt.core.manager.Manager.TestCaseMode;
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

		manager.createConfiguration(lexerClass, parserClass, configName,
				makeDefault, true, cp);
	}

	public void setActiveConfiguration(String configName) {
		manager.getArchive().setActiveConfiguration(configName);
	}

	public void addClassPathEntry(String configName, String entry)
			throws RTTException {
		manager.addClassPathEntry(configName, entry);
	}

	public void removeClasspathEntry(String configName, String entry)
			throws RTTException {
		manager.removeClassPathEntry(configName, entry);
	}

	public void addTestsuite(String suiteName) throws RTTException {
		manager.createTestSuite(suiteName);
	}

	public void removeTestsuite(String suiteName) throws RTTException {
		manager.removeTestsuite(suiteName);
	}

	public void addTestcase(String text, IFile file) throws RTTException {
		manager.addFile(file.getLocation().toFile(), text,
				TestCaseMode.OVERWRITE);
	}

	public void removeTestcase(String suiteName, String caseName)
			throws RTTException {
		manager.removeTest(suiteName, caseName);
	}

	public List<Throwable> generateTests(String suiteName) throws RTTException {
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
		config.setClasspath(new Classpath());
		
		LexerClass lexer = new LexerClass();
		lexer.setValue("");
		config.setLexerClass(lexer);
		
		ParserClass parser = new ParserClass();
		parser.setValue("");
		config.setParserClass(parser);
		
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
