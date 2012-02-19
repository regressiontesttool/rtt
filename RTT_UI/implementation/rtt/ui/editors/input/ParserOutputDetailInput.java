package rtt.ui.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import rtt.ui.core.archive.IParserOutput;
import rtt.ui.core.internal.treeItem.NodeTreeItem;
import rtt.ui.core.internal.treeItem.ParserOutputDataTreeItem;

public class ParserOutputDetailInput implements IDetailInput {
	
	private String editorID = ""; 
	private IParserOutput parserOutput;
	
	public ParserOutputDetailInput(String editorID, IParserOutput parserOutput) {
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
		return new String[] { NodeDetail.CLASSNAME, NodeDetail.C_COUNT , NodeDetail.A_COUNT };
	}

	@Override
	public String[] getHeaderNames() {
		return new String[] { "Name", "Value", "Typ" };
	}

	@Override
	public IDetail getDetail(Object o) {
		if (o != null && o instanceof NodeTreeItem) {
			return new NodeDetail((NodeTreeItem) o);
		}
		
		return null;
	}

	@Override
	public String getMasterSectionTitle() {
		return "Abstract Syntax Tree";
	}

	@Override
	public Object getMasterRoot() {
		return new ParserOutputDataTreeItem(parserOutput);
	}

	@Override
	public Class<?>[] getDetailClasses() {
		return new Class<?>[] { NodeTreeItem.class };
	}

}
