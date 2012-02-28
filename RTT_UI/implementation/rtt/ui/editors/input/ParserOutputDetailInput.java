package rtt.ui.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import rtt.core.archive.output.ParserOutput;
import rtt.ui.content.IContent;
import rtt.ui.content.output.NodeContent;
import rtt.ui.content.output.RootContent;
import rtt.ui.editors.input.details.IDetail;
import rtt.ui.editors.input.details.NodeDetail;

public class ParserOutputDetailInput implements IDetailInput {

	private String editorID = "";
	private ParserOutput parserOutput;

	public ParserOutputDetailInput(String editorID, ParserOutput parserOutput) {
		this.editorID = editorID;
		this.parserOutput = parserOutput;
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
		return "ParserOutputName";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return "ParserOuputToolTip";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEditorID() {
		return editorID;
	}

	@Override
	public String getEditorTitle() {
		return "ParserOutputEditorTitle";
	}

	@Override
	public String[] getKeys() {
		return new String[] { NodeDetail.FULL_NAME, NodeDetail.C_COUNT,
				NodeDetail.A_COUNT };
	}

	@Override
	public String[] getHeaderNames() {
		return new String[] { "Name", "Value", "Typ" };
	}

	@Override
	public IDetail getDetail(Object o) {
		if (o != null && o instanceof NodeContent) {
			return new NodeDetail((NodeContent) o);
		}

		return null;
	}

	@Override
	public String getMasterSectionTitle() {
		return "Abstract Syntax Tree";
	}

	// @Override
	// public Object getMasterRoot() {
	// return new ParserOutputDataTreeItem(parserOutput);
	// }

	@Override
	public IContent getRoot() {
		return new RootContent(parserOutput);
	}

	@Override
	public Class<?>[] getDetailClasses() {
		return new Class<?>[] { NodeContent.class };
	}

}
