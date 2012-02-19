package rtt.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.osgi.service.prefs.BackingStoreException;


public class RttPreferenceStore {
	
	public static final String PREF_LEXER_CLASS = "rtt.lexer";
	public static final String PREF_PARSER_CLASS = "rtt.parser";
	public static final String PREF_ARCHIVE_EXIST = "rtt.archive.exist";
	public static final String PREF_ARCHIVE_PATH = "rtt.archive.path";
	public static final String PREF_CONFIG_DEFAULT = "rtt.archive.config.default";
	
	public static IEclipsePreferences getPreferences(IJavaProject project) {
		return getPreferences(project.getProject());
	}
	
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
	
	public static boolean getBoolean(IProject project, String key, boolean def) {
		IEclipsePreferences pref = getPreferences(project);
		if (pref != null) {
			return pref.getBoolean(key, def);
		}
		
		return def;
	}
	
	public static void put(IProject project, String key, String value) {
		IEclipsePreferences pref = getPreferences(project);
		if (pref != null) {
			pref.put(key, value);
			try {
				pref.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}		
	}

	public static void putBoolean(IProject project, String key, boolean value) {
		IEclipsePreferences pref = getPreferences(project);
		if (pref != null) {
			pref.putBoolean(key, value);
			try {
				pref.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}
}
