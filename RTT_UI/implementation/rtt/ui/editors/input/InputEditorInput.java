package rtt.ui.editors.input;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import rtt.core.manager.data.InputManager;
import rtt.ui.content.ProjectContent;
import rtt.ui.core.ProjectFinder;
import rtt.ui.model.RttProject;

public class InputEditorInput implements IStorageEditorInput {

	String suiteName;
	String caseName;
	Integer versionNr;
	InputManager manager;

	public InputEditorInput(String suiteName, String caseName, Integer versionNr) {
		super();
		this.suiteName = suiteName;
		this.caseName = caseName;
		this.versionNr = versionNr;
	}

	public InputEditorInput(InputManager manager, Integer versionNr) {
		super();
		this.manager = manager;
		this.versionNr = versionNr;
	}

	@Override
	public Object getAdapter(Class adapter) {
		System.out.println("Adapt: " + adapter);
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		if (manager != null) {
			return "Input [" + manager.getSuiteName() + "/"
					+ manager.getCaseName() + "] (" + versionNr + ")";
		}

		return "Input [" + suiteName + "/" + caseName + "]";

	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return new IStorage() {

			@Override
			public Object getAdapter(Class adapter) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isReadOnly() {
				return true;
			}

			@Override
			public String getName() {
				return "Storage name";
			}

			@Override
			public IPath getFullPath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getContents() throws CoreException {
				if (manager == null) {
					ProjectContent projectContent = ProjectFinder
							.getCurrentProjectContent();
					RttProject project = projectContent.getProject();

					manager = new InputManager(project.getLoader(), suiteName,
							caseName);
				}

				InputStream bais = new ByteArrayInputStream(manager
						.loadData(versionNr).getValue().getBytes());

				return bais;
			}
		};
	}
}
