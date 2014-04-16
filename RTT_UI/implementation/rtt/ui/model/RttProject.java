package rtt.ui.model;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.ui.dialogs.ContainerGenerator;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.logging.Comment;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.Testrun;
import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;
import rtt.core.manager.Manager.TestCaseMode;
import rtt.core.manager.data.ConfigurationManager.ConfigStatus;
import rtt.core.manager.data.LogManager;
import rtt.core.utils.GenerationInformation;
import rtt.ui.RttPluginUI;
import rtt.ui.utils.RttLog;
import rtt.ui.utils.RttPluginUtil;

public class RttProject {

	public abstract static class ArchiveCommand<T> {
		public abstract T execute(Manager manager) throws Exception;
	}
	
	private Configuration activeConfig = null;
	
	// project data
	private String name;
	
	private IJavaProject javaProject;
	private IJavaSearchScope scope;
	
	private IFile archive;

	public RttProject(IJavaProject javaProject) throws RTTException,
			CoreException {
		this.javaProject = javaProject;
		IProject project = javaProject.getProject();
		this.name = project.getDescription().getName();
		
		IPath archivePath = RttPluginUtil.getArchivePath(project);		
		if (archivePath != null) {
			archive = project.getFile(archivePath);
		}
	}
	
	// ----- getter -----

	public String getName() {
		return name;
	}
	
	public void createArchive(IFile archiveFile) throws CoreException {
		// create parent folders if necessary
		IContainer resource = archiveFile.getParent();
		if (resource != null && resource.getType() == IResource.FOLDER) {
			ContainerGenerator generator = new ContainerGenerator(resource.getFullPath());
			generator.generateContainer(new NullProgressMonitor());
		}
		
		// create archive
		File archive = archiveFile.getLocation().toFile();				
		Manager m = new Manager(archive, true);
		try {
			m.createArchive();
			m.saveArchive(archive);
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR,
					RttPluginUI.PLUGIN_ID,
					"Could not create archive file: " + archive, e));
		}
		
		resource.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		this.archive = archiveFile;
	}
	
	public IFile getArchiveFile() {
		return archive;
	}
	
	public Configuration getActiveConfiguration() {
		return activeConfig;
	}
	
	public IJavaProject getJavaProject() {
		return javaProject;
	}
	
	public IProject getIProject() {
		return javaProject.getProject();
	}

	public IPath getArchivePath(boolean cutOf) {
		IPath archivePath = archive.getProjectRelativePath();
		
		if (cutOf) {
			return archivePath.removeLastSegments(1);
		}
		
		return archivePath;
	}
	
	// ---- change archive ----
	
	public <T> T changeArchive(ArchiveCommand<T> command) throws Exception {
		Manager manager = getManager();
		if (manager != null) {
			T result = command.execute(manager);
			manager.saveArchive(archive.getLocation().toFile());

			manager.close();
			
			return result;
		}	
		
		throw new RuntimeException("Could not open archive manager.");
	}
	
	public Manager getManager() throws RTTException {
		Manager manager = RttPluginUtil.getArchiveManager(archive, activeConfig);
		if (activeConfig == null && manager != null) {
			activeConfig = manager.getArchive().getActiveConfiguration();
		}
		
		return manager;
	}

	public void setConfiguration(
			final String configName, 
			final String lexerClass, 
			final String parserClass, 
			final List<String> cp, 
			final boolean makeDefault)
		throws Exception {
		
		changeArchive(new ArchiveCommand<ConfigStatus>() {
			
			@Override
			public ConfigStatus execute(Manager manager) {
				return manager.setConfiguration(configName, lexerClass, parserClass, cp, makeDefault, true);
			}
		});
	}

	public void setActiveConfiguration(Configuration activeConfig) {
		this.activeConfig = activeConfig;
	}

	public boolean addTestsuite(final String suiteName) throws Exception {
		return changeArchive(new ArchiveCommand<Boolean>() {
			@Override
			public Boolean execute(Manager manager) {
				return manager.createTestSuite(suiteName);
			}
		});
	}

	public boolean removeTestsuite(final String suiteName) throws Exception {
		return changeArchive(new ArchiveCommand<Boolean>() {
			@Override
			public Boolean execute(Manager manager) throws RTTException {
				return manager.removeTestsuite(suiteName);
			}
		});
	}

	public List<RTTException> addTestcase(final String suiteName, final List<File> files) throws Exception {
		return changeArchive(new ArchiveCommand<List<RTTException>>() {
			
			@Override
			public List<RTTException> execute(Manager manager) {
				return manager.addAllFiles(files, suiteName, TestCaseMode.OVERWRITE);
			}
		});
	}

	public void removeTestcase(final String suiteName, final String caseName)
			throws Exception {
		changeArchive(new ArchiveCommand<Boolean>() {
			@Override
			public Boolean execute(Manager manager) throws RTTException {
				manager.removeTest(suiteName, caseName);
				return true;
			}
		});
	}
	
	public void addParameters(final String suiteName, final String caseName, final List<String> parameters) throws Exception {
		changeArchive(new ArchiveCommand<Boolean>() {
			@Override
			public Boolean execute(Manager manager) throws RTTException {
				manager.setParametersToTest(suiteName, caseName, parameters);
				return true;
			}
		});
		
	}
	
	public void addComment(final String commentText, final Testrun testrun, final Result result) throws Exception {
		changeArchive(new ArchiveCommand<Boolean>() {
			public Boolean execute(Manager manager) throws Exception {
				if (commentText == null || commentText.equals("")) {
					return false;
				}
				
				LogManager logManager = manager.getArchive().getLogManager();
				if (logManager == null) {
					throw new RuntimeException("Logmanager was null.");
				}
				
				Testrun toTestrun = logManager.getTestrun(testrun.getDate());
				if (toTestrun != null) {
					Result toResult = logManager.getResultFromTestrun(toTestrun, 
							result.getType(), result.getTestsuite(), result.getTestcase());
					if (toResult != null) {
						Comment comment = new Comment();
						comment.setValue(commentText);
						
						toResult.getComment().add(comment);
						
						return true;
					}
				}
				
				return false;
			}
		});
	}
	
	public void editComment(final String oldText, final String newText, final Testrun testrun, final Result result) throws Exception {
		changeArchive(new ArchiveCommand<Boolean>() {
			public Boolean execute(Manager manager) throws Exception {
				if (oldText == null || newText == null) {
					return false;
				}
				
				LogManager logManager = manager.getArchive().getLogManager();
				if (logManager == null) {
					throw new RuntimeException("Logmanager was null.");
				}
				
				Testrun toTestrun = logManager.getTestrun(testrun.getDate());
				if (toTestrun != null) {
					Result toResult = logManager.getResultFromTestrun(toTestrun, 
							result.getType(), result.getTestsuite(), result.getTestcase());
					if (toResult != null) {
						for (Comment comment : toResult.getComment()) {
							if (comment.getValue().equals(oldText)) {
								comment.setValue(newText);
								return true;
							}
						}
					}
				}
				
				return false;
			}
		});		
	}
	
	public void removeComment(final String commentText, final Testrun testrun, final Result result) throws Exception {
		changeArchive(new ArchiveCommand<Boolean>() {
			@Override
			public Boolean execute(Manager manager) throws Exception {
				if (commentText == null || commentText.equals("")) {
					return false;
				}
				
				LogManager logManager = manager.getArchive().getLogManager();
				if (logManager == null) {
					throw new RuntimeException("LogManager was null.");
				}
				
				Testrun toTestrun = logManager.getTestrun(testrun.getDate());
				if (toTestrun != null) {
					Result toResult = logManager.getResultFromTestrun(toTestrun, 
							result.getType(), result.getTestsuite(), result.getTestcase());
					if (toResult != null) {
						Comment delComment = null;
						for (Comment comment : toResult.getComment()) {
							if (comment.getValue().equals(commentText)) {
								delComment = comment;
								break;
							}
						}
						
						if (delComment != null) {
							toResult.getComment().remove(delComment);
							return true;
						}
					}
					
				}
				return false;
			}
		});
	}

	public GenerationInformation generateTests(final String suiteName) throws Exception {
		return changeArchive(new ArchiveCommand<GenerationInformation>() {
			@Override
			public GenerationInformation execute(Manager manager)
					throws Exception {
				return manager.generateTests(suiteName);
			}
		});
	}

	public GenerationInformation runTests(final String suiteName, final boolean matching)
			throws Exception {
		return changeArchive(new ArchiveCommand<GenerationInformation>() {
			@Override
			public GenerationInformation execute(Manager manager)
					throws Exception {
				return manager.runTests(suiteName, matching);
			}
		});
	}
	
	// ---- utilities ------

	public Configuration createEmptyConfiguration() {
		Configuration config = new Configuration();
		config.setName("");
		config.setLexerClass("");
		config.setParserClass("");

		Classpath classpath = new Classpath();
		try {
			IPath outputLocation = javaProject.getOutputLocation();
			outputLocation = outputLocation.removeFirstSegments(1);			
			classpath.getPath().add(outputLocation.makeRelativeTo(getArchivePath(true)).toPortableString());
		} catch (JavaModelException exception) {
			RttLog.log(exception);
		}
		
		config.setClasspath(classpath);
		
		return config;
	}

	public void close(boolean closeJavaProject) {
		if (closeJavaProject) {
			javaProject = null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((javaProject == null) ? 0 : javaProject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RttProject other = (RttProject) obj;
		if (javaProject == null) {
			if (other.javaProject != null)
				return false;
		} else if (!javaProject.equals(other.javaProject))
			return false;
		return true;
	}
	
	public IJavaSearchScope getSearchScope() {
		if (scope == null) {
			scope = SearchEngine.createJavaSearchScope(
					new IJavaElement[] { javaProject }, true);
		}
		
		return scope;
	}
}
