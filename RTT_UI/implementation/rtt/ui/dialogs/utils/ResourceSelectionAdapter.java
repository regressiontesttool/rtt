package rtt.ui.dialogs.utils;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.model.RttProject;

public class ResourceSelectionAdapter extends SelectionAdapter {

	public enum DialogType {
		RESOURCE, CONTAINER
	}

	private List<String> cpEntries;
	private RttProject project;
	
	private ConfigurationDialog configDialog;
	private DialogType type;

	public ResourceSelectionAdapter(DialogType type, ConfigurationDialog configDialog) {
		this.configDialog = configDialog;
		this.type = type;
		
		this.project = configDialog.getProject();
		this.cpEntries = configDialog.getClasspathEntries();		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		SelectionDialog dialog = null;
		switch (type) {
		case CONTAINER:
			dialog = new ContainerSelectionDialog(configDialog
					.getShell(), project.getIProject(), false,
					"Select a folder ...");
			break;
			
		case RESOURCE:
			dialog = new ResourceSelectionDialog(configDialog
					.getShell(), project.getIProject(), "Select a file ...");
			break;
		}		
		
		if (dialog != null && dialog.open() == Dialog.OK) {
			for (Object o : dialog.getResult()) {
				IPath path = null;

				if (o instanceof IResource) {
					IResource file = (IResource) o;
					path = file.getProjectRelativePath();
				}

				if (o instanceof IPath) {
					IPath folderPath = (IPath) o;
					path = folderPath.removeFirstSegments(1);
				}
				
				if (path != null && !path.isEmpty()) {
					path = path.makeRelativeTo(project.getIProject().getFullPath());
					System.out.println("Path:" + path);

					if (!path.isEmpty()) {
						cpEntries.add(path.toPortableString());
						configDialog.setOkButtonEnabled(true);
					}					
				}
			}
			configDialog.getViewer().refresh();
		}
	}
}