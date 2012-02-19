package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.editors.IEditorCommand;

public class LexerOutputTreeItem extends AbstractTreeItem {

	public LexerOutputTreeItem(ITreeItem parent, ILexerOutput lexerOutput, IEditorCommand command) {
		super(parent);
		
		String name = "LexerOutput";
		if (lexerOutput != null) {
			setCommand(command);
		} else {
			name += " (not generated)";
		}
		setName(name);
		setIcon(TreeItemIcon.LEXER_OUTPUT);
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}

}
