package rtt.ui.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import rtt.core.archive.output.LexerOutput;
import rtt.ui.content.IContent;
import rtt.ui.content.output.RootContent;
import rtt.ui.content.output.TokenContent;
import rtt.ui.editors.input.details.IDetail;
import rtt.ui.editors.input.details.TokenDetail;

// FIXME anpassungen auf Content !
public class LexerOutputDetailInput implements IDetailInput {
	
	private String editorID = ""; 
	private LexerOutput lexerOutput;
	
	public LexerOutputDetailInput(String editorID, LexerOutput lexerOutput) {
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
		return new String[] { TokenDetail.FULL_NAME, TokenDetail.A_COUNT };
	}

	@Override
	public String[] getHeaderNames() {
		return new String[] { "Name", "Value", "Typ" };
	}

	@Override
	public IDetail getDetail(Object o) {
		if (o != null && o instanceof TokenContent) {
			return new TokenDetail((TokenContent) o);
		}
		
		return null;
	}

	@Override
	public String getMasterSectionTitle() {
		return "Output";
	}

//	@Override
//	public Object getMasterRoot() {
//		return new LexerOutputDataTreeItem(lexerOutput);
//	}
	
	public IContent getRoot() {
		return new RootContent(lexerOutput);		
	};

	@Override
	public Class<?>[] getDetailClasses() {
		return new Class<?>[] { TokenContent.class };
	}

}
