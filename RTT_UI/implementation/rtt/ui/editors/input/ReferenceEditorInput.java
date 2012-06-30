package rtt.ui.editors.input;

import java.io.InputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rtt.core.archive.configuration.Configuration;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.OutputDataManager;
import rtt.core.manager.data.OutputDataManager.OutputDataType;
import rtt.ui.model.RttProject;

public class ReferenceEditorInput implements IEditorInput {

	private OutputDataManager refManager;
	private int version;

	public ReferenceEditorInput(RttProject project, String suiteName,
			String caseName, int version) {

		ArchiveLoader loader = project.getLoader();
		Configuration config = project.getActiveConfiguration();

		this.version = version;
		this.refManager = new OutputDataManager(loader, suiteName,
				caseName, config, OutputDataType.REFERENCE);		
	}

	public ReferenceEditorInput(OutputDataManager manager, int version) {
		this.version = version;
		this.refManager = manager;		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		if (obj instanceof ReferenceEditorInput) {
			ReferenceEditorInput input = (ReferenceEditorInput) obj;
			if (refManager.equals(input.refManager) && input.version == this.version) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "Reference data [" + refManager.getSuiteName() + "/"
				+ refManager.getCaseName() + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	public InputStream getLexerOutputStream() {
		return refManager.getLexerOutputStream(version);
	}
	
	public InputStream getParserOutputStream() {
		return refManager.getParserOutputStream(version);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}
