package rtt.ui.dialogs.utils;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ListViewer;
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

	private SelectionDialog dialog;
	private List<String> cpEntries;
	private ListViewer viewer;
	private RttProject project;

	public ResourceSelectionAdapter(RttProject project, List<String> cpEntries,
			ListViewer viewer) {
		this.project = project;
		this.cpEntries = cpEntries;
		this.viewer = viewer;
	}

	public void setDialog(SelectionDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
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
					path = path.makeRelativeTo(project.getArchivePath(true));
					if (!path.isEmpty()) {
						cpEntries.add(path.toPortableString());
					}					
				}
			}
			viewer.setInput(cpEntries);
		}
	}

	public static ResourceSelectionAdapter createAdapter(DialogType type,
			ConfigurationDialog configDialog) {

		RttProject project = configDialog.getProject();
		ResourceSelectionAdapter adapter = new ResourceSelectionAdapter(
				project, configDialog.getClasspathEntries(),
				configDialog.getViewer());

		switch (type) {
		case RESOURCE:
			adapter.setDialog(new ResourceSelectionDialog(configDialog
					.getShell(), project.getJavaProject().getProject(), "Select a file ..."));
			break;

		case CONTAINER:
			adapter.setDialog(new ContainerSelectionDialog(configDialog
					.getShell(), project.getJavaProject().getProject(), false,
					"Select a folder ..."));
			break;
		}

		return adapter;

	}
}