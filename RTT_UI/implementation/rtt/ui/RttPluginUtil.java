package rtt.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;
import rtt.ui.content.IContent;
import rtt.ui.content.IContentObserver;
import rtt.ui.content.internal.EmptyContent;
import rtt.ui.content.internal.ProjectContent;
import rtt.ui.core.archive.IArchive;
import rtt.ui.core.archive.xml.XMLArchive;
import rtt.ui.model.RttProject;

public class RttPluginUtil {

	public static List<IContent> getProjectContents(IContentObserver observer) {
		List<IContent> contents = new ArrayList<IContent>();

		for (RttProject project : getProjects()) {
			try {
				ProjectContent content = new ProjectContent(project);
				
				if (observer != null) {
					content.addObserver(observer);
				}
				
				contents.add(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (contents.size() == 0) {
			contents.add(new EmptyContent("No projects with rtt nature found."));
		}
		
		return contents;
	}
	
	public static List<RttProject> getProjects() {
		List<RttProject> projects = new ArrayList<RttProject>();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			try {
				RttProject newProject = new RttProject(project);
				projects.add(newProject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return projects;
	}

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

	@Deprecated
	public static IArchive getArchive(IProject project) {
		return getArchive(project, null);
	}

	@Deprecated
	public static IArchive getArchive(IProject project, String configuration) {
		File f = getArchiveFile(project);
		if (f != null) {

			if (configuration == null || configuration.equals("")) {
				configuration = RttPreferenceStore.get(project,
						RttPreferenceStore.PREF_CONFIG_DEFAULT, "");
			}

			try {
				return new XMLArchive(f, configuration);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	public static File getArchiveFile(IProject project) {

		IEclipsePreferences pref = RttPreferenceStore.getPreferences(project);

		if (pref != null
				&& !pref.get(RttPreferenceStore.PREF_ARCHIVE_PATH, "").equals(
						"")) {
			return new File(
					pref.get(RttPreferenceStore.PREF_ARCHIVE_PATH, null));
		}

		return null;

	}

}
