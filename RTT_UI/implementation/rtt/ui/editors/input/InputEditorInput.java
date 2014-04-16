package rtt.ui.editors.input;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;
import rtt.core.manager.data.history.InputManager;
import rtt.ui.content.IContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.model.RttProject;

@SuppressWarnings("rawtypes")
public class InputEditorInput implements IStorageEditorInput {

	RttProject project;
	String suiteName;
	String caseName;
	Integer versionNr;

	public InputEditorInput(IContent content, String suiteName, String caseName, Integer versionNr) {
		super();
		this.project = content.getProject();
		this.suiteName = suiteName;
		this.caseName = caseName;
		this.versionNr = versionNr;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ContentIcon.INPUT.getImageDescriptor(false);
	}

	@Override
	public String getName() {
		StringBuilder builder = new StringBuilder();
		builder.append("Input (");
		builder.append(versionNr);
		builder.append(") [");
		builder.append(suiteName);
		builder.append("/");
		builder.append(caseName);
		builder.append("]");
		
		return builder.toString();
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
				return null;
			}

			@Override
			public InputStream getContents() throws CoreException {
				try {
					Manager manager = project.getManager();
					if (manager == null) {
						throw new RuntimeException("Could not open manager.");
					}
					
					InputManager inputManager = new InputManager(manager.getArchive().getLoader(), suiteName, caseName);
					InputStream bais = new ByteArrayInputStream(inputManager.getInput(versionNr).getValue().getBytes());

					manager.close();
					
					return bais;
				} catch (RTTException e) {
					throw new RuntimeException("Could not get input content.", e);
				}
			}
		};
	}
}
