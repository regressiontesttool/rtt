package rtt.ui;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

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

	public static File getArchiveFile(IProject project) {

		IEclipsePreferences pref = RttPreferenceStore.getPreferences(project);

		if (pref != null) {
			String archivePath = pref.get(RttPreferenceStore.PREF_ARCHIVE_PATH, "");
			if (archivePath != null && !archivePath.equals("")) {
				return new File(
						pref.get(RttPreferenceStore.PREF_ARCHIVE_PATH, null));
			}			
		}

		return null;

	}

}
