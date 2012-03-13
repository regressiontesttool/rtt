package rtt.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;


public class RttPreferenceStore {
	
	public static final String PREF_ARCHIVE_PATH = "rtt.archive.path";
	
	public static IEclipsePreferences getPreferences(IProject project) {
		IScopeContext context = new ProjectScope(project);
		
		return context.getNode(RttPluginUI.PLUGIN_ID);
	}
	
	public static String get(IProject project, String key, String def) {
		IEclipsePreferences  pref = getPreferences(project);
		if (pref != null) {
			return pref.get(key, def);
		}
		
		return null;
	}
	
	public static void put(IProject project, String key, String value) {
		IEclipsePreferences pref = getPreferences(project);
		if (pref != null) {
			pref.put(key, value);
			try {
				pref.flush();
			} catch (BackingStoreException e) {
				RttLog.log(e);
			}
		}		
	}

	
}
