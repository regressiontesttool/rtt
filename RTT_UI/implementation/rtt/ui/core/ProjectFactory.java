package rtt.ui.core;

import org.eclipse.core.resources.IProject;

import rtt.ui.RttPreferenceStore;
import rtt.ui.core.internal.model.XMLRttProject;

public class ProjectFactory {

	public static IRttProject create(IProject project) {
		if (RttPreferenceStore.getBoolean(project, RttPreferenceStore.PREF_ARCHIVE_EXIST, false)) {
			return new XMLRttProject(project);
		}
		
		return null;
	}

}
