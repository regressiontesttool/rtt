package rtt.annotation.editor.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import rtt.annotation.editor.AnnotationEditorPlugin;

public class StatusFactory {
	
	public static IStatus createError(String message) {
		return new Status(Status.ERROR, AnnotationEditorPlugin.PLUGIN_ID, message);
	}

}
