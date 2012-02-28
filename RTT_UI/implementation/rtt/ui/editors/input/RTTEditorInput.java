package rtt.ui.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.ReferenceManager;
import rtt.ui.model.RttProject;

public class RTTEditorInput implements IEditorInput {

	private ReferenceManager refManager;
	private int version;

	public RTTEditorInput(ReferenceManager refManager, int version) {
		this.refManager = refManager;
		this.version = version;
	}

	public static IEditorInput getReference(RttProject project,
			String suiteName, String caseName, int version) {

		ArchiveLoader loader = project.getLoader();
		Configuration config = project.getActiveConfiguration();

		ReferenceManager refManager = new ReferenceManager(loader, suiteName,
				caseName, config);

		return new RTTEditorInput(refManager, version);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		if (obj instanceof RTTEditorInput) {
			RTTEditorInput input = (RTTEditorInput) obj;
			return refManager.equals(input.refManager);
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

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public LexerOutput getLexerOutput() {
		return refManager.getLexerOutput(version);
	}

	public ParserOutput getParserOuput() {
		return refManager.getParserOutput(version);
	}
}
