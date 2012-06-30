package rtt.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import regression.test.TestPackage;
import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;

public class RttPluginUtil {

//	public static List<IContent> getProjectContents(IContentObserver observer) {
//		List<IContent> contents = new ArrayList<IContent>();
//
//		for (RttProject project : getProjects()) {
//			try {
//				ProjectContent content = new ProjectContent(project);
//				
//				if (observer != null) {
//					content.addObserver(observer);
//				}
//				
//				contents.add(content);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if (contents.size() == 0) {
//			contents.add(new EmptyContent("No projects with rtt nature found."));
//		}
//		
//		return contents;
//	}
//	
//	public static List<RttProject> getProjects() {
//		List<RttProject> projects = new ArrayList<RttProject>();
//		
//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		for (IProject project : root.getProjects()) {
//			try {
//				RttProject newProject = new RttProject(project);
//				projects.add(newProject);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return projects;
//	}

	public static Manager getRttArchive(File file) throws RTTException {
		return getRttArchive(file, null);
	}

	public static Manager getRttArchive(File file, String config)
			throws RTTException {
		if (file != null && file.exists()) {
			Manager manager = new Manager(file, true);
			manager.loadArchive(config);

			return manager;
		}

		return null;
	}

	public static IFile getArchiveFile(IProject project) {
		String archivePath = RttPreferenceStore.get(project, RttPreferenceStore.PREF_ARCHIVE_PATH, "");
		
		if (archivePath != null && !archivePath.equals("")) {
			IPath path = Path.fromPortableString(archivePath);
			IFile archiveFile = project.getFile(path);
			if (archiveFile != null && archiveFile.exists()) {
				return archiveFile;
			}
		}
		
		return null;
	}
	
	public static ResourceSet createResourceSet(InputStream input, String fileTitle) throws IOException {
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(TestPackage.eNS_URI, TestPackage.eINSTANCE);
		ModelUtils.load(input, fileTitle, resourceSet);
		
		return resourceSet;
	}

}
