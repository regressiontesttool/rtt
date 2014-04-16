package rtt.ui.ecore.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.RttPluginUI;

/**
 * An utility class for providing messages and strings from
 * properties files.
 *  
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class Messages {
	
	private static final String QUESTION_TITLE = "rtt.ui.dialogs.question.title";
	private static final String ERROR_TITLE = "rtt.ui.dialogs.error.title";

	
	public static String getString(String key) {
		return RttPluginUI.getPlugin().getString(key);
	}
	
	public static String getString(String key, Object... substitutions) {
		return RttPluginUI.getPlugin().getString(key, substitutions);
	}

	/**
	 * Executes {@link MessageDialog#openQuestion(Shell, String, String)}
	 * with the associated {@link String}s for the title and message of the dialog.
	 * @param parent a {@link Shell}
	 * @param titleKey the key for a resource associated string
	 * @param messageKey the key for a resource associated string
	 * @return {@code true}, if user presses the YES button
	 */
	public static boolean openQuestion(Shell parent, 
			String titleKey, String messageKey) {
		
		return MessageDialog.openQuestion(parent, 
				Messages.getString(titleKey), 
				Messages.getString(messageKey));
	}
	
	/**
	 * Executes {@link MessageDialog#openQuestion(Shell, String, String)}
	 * with the associated {@link String}s for the message and a standard title 
	 * for the dialog.
	 * @param parent a {@link Shell}
	 * @param messageKey the key for a resource associated string
	 * @return {@code true}, if user presses the YES button
	 */
	public static boolean openQuestion(Shell parent, String messageKey) {
		return openQuestion(parent, QUESTION_TITLE, messageKey);
	}
	
	/**
	 * Executes {@link MessageDialog#openError(Shell, String, String)}
	 * with the associated {@link String}s for the title and message of the dialog.
	 * @param parent a {@link Shell}
	 * @param titleKey the key for a resource associated string
	 * @param messageKey the key for a resource associated string
	 */
	public static void openError(Shell parent,
			String titleKey, String messageKey) {
		
		MessageDialog.openError(parent,
				Messages.getString(titleKey), 
				Messages.getString(messageKey));
	}
	
	/**
	 * Executes {@link MessageDialog#openError(Shell, String, String)}
	 * with the associated {@link String}s for the message and a standard title 
	 * for the dialog.
	 * @param parent a {@link Shell}
	 * @param messageKey the key for a resource associated string
	 */
	public static void openError(Shell parent, 
			String messageKey) {
		openError(parent, ERROR_TITLE, messageKey);
	}
	
	

}
