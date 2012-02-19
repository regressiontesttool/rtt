package rtt.ui.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.core.internal.treeItem.LexerOutputDataTreeItem;
import rtt.ui.core.internal.treeItem.TokenTreeItem;

public class LexerOutputDetailInput implements IDetailInput {
	
	private String editorID = ""; 
	private ILexerOutput lexerOutput;
	
	public LexerOutputDetailInput(String editorID, ILexerOutput lexerOutput) {
		this.editorID = editorID;
		this.lexerOutput = lexerOutput;
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
		return "LexerOutputName";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return "LexerOutputToolTip";
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
		return "LexerOutputEditorTitle";
	}

	@Override
	public String[] getKeys() {
		return new String[] { TokenDetail.CLASSNAME, TokenDetail.A_COUNT };
	}

	@Override
	public String[] getHeaderNames() {
		return new String[] { "Name", "Value", "Typ" };
	}

	@Override
	public IDetail getDetail(Object o) {
		if (o != null && o instanceof TokenTreeItem) {
			return new TokenDetail((TokenTreeItem) o);
		}
		
		return null;
	}

	@Override
	public String getMasterSectionTitle() {
		return "Output";
	}

	@Override
	public Object getMasterRoot() {
		return new LexerOutputDataTreeItem(lexerOutput);
	}

	@Override
	public Class<?>[] getDetailClasses() {
		return new Class<?>[] { TokenTreeItem.class };
	}

}
