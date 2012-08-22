package rtt.ui.editors.input;

import java.io.InputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rtt.core.archive.configuration.Configuration;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.model.RttProject;

public class OutputDataEditorInput implements IEditorInput {

	private OutputDataManager manager;
	private int version;

	public OutputDataEditorInput(RttProject project, String suiteName,
			String caseName, int version, OutputDataType type) {

		ArchiveLoader loader = project.getLoader();
		Configuration config = project.getActiveConfiguration();

		this.version = version;
		this.manager = new OutputDataManager(loader, suiteName,
				caseName, config, type);		
	}

	public OutputDataEditorInput(OutputDataManager manager, int version) {
		this.version = version;
		this.manager = manager;		
	}
	
	public OutputDataType getType() {
		return manager.getType();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		if (obj instanceof OutputDataEditorInput) {
			OutputDataEditorInput input = (OutputDataEditorInput) obj;
			
			if (!manager.getType().equals(input.manager.getType())) {
				return false;
			}			
			
			if (manager.equals(input.manager) && input.version == this.version) {
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
		return manager.getType().getText() + " data [" + manager.getSuiteName() + "/"
				+ manager.getCaseName() + "] (Ver. " + version + ")";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	public InputStream getLexerOutputStream() {
		return manager.getLexerOutputStream(version);
	}
	
	public InputStream getParserOutputStream() {
		return manager.getParserOutputStream(version);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}
