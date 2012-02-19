package rtt.ui.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import rtt.core.archive.configuration.Configuration;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.ReferenceManager;
import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.core.archive.IParserOutput;
import rtt.ui.core.archive.xml.XMLLexerOutput;
import rtt.ui.core.archive.xml.XMLParserOutput;

public class TestCaseEditorInput implements ITestCaseEditorInput {

	private ReferenceManager refManager;
	private String suiteName;
	private String caseName;

	public TestCaseEditorInput(ArchiveLoader loader, String suiteName, String caseName, Configuration config) {		
		refManager = new ReferenceManager(loader, suiteName, caseName, config);
		this.suiteName = suiteName;
		this.caseName = caseName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		if (obj instanceof TestCaseEditorInput) {
			TestCaseEditorInput input = (TestCaseEditorInput) obj;
			return refManager.equals(input.refManager);
		}

		return false;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Reference data [" + suiteName + "/" + caseName + "]";
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
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IParserOutput getParserOuput() {
		return new XMLParserOutput(refManager.getParserOutput());
	}

	@Override
	public ILexerOutput getLexerOutput() {
		return new XMLLexerOutput(refManager.getLexerOutput());
	}

}
