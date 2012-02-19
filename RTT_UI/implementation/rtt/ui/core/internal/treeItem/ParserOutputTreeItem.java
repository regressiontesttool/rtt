package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.IParserOutput;
import rtt.ui.editors.IEditorCommand;

public class ParserOutputTreeItem extends AbstractTreeItem {
	
	public ParserOutputTreeItem(ITreeItem parent, IParserOutput parserOutput, IEditorCommand command) {
		super(parent);
		
		String name = "ParserOutput";
		
		if (parserOutput != null) {
			setCommand(command);
		} else {
			name += " (not generated)";
		}
		
		setName(name);
		setIcon(TreeItemIcon.PARSER_OUTPUT);
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}

}
