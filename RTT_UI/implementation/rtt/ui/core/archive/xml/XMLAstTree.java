package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.output.Tree;
import rtt.ui.core.archive.IAbstractSyntaxTree;
import rtt.ui.core.archive.INode;

public class XMLAstTree implements IAbstractSyntaxTree {
	
	private List<INode> nodes;
	private String fullName;
	private String simpleName;

	public XMLAstTree(Tree tree) {
		this.nodes = XMLNode.getList(tree);
		this.fullName = tree.getFullName();
		this.simpleName = tree.getSimpleName();
	}

	@Override
	public List<INode> getNodes() {
		return nodes;
	}
	
	@Override
	public String getFullName() {
		return fullName;
	}
	
	public String getSimpleName() {
		return simpleName;
	}

	public static List<IAbstractSyntaxTree> getList(ParserOutput parserOutput) {		
		// CHRISTIAN check
		
		List<IAbstractSyntaxTree> list = new LinkedList<IAbstractSyntaxTree>();
		
		if (parserOutput != null) {
			for (Tree tree : parserOutput.getTree()) {
				list.add(new XMLAstTree(tree));
			}
		}		
		
		return list;
	}

}
