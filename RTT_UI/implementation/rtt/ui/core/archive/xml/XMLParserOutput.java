package rtt.ui.core.archive.xml;

import java.util.List;

import rtt.core.archive.output.ParserOutput;
import rtt.ui.core.archive.IAbstractSyntaxTree;
import rtt.ui.core.archive.IParserOutput;

public class XMLParserOutput implements IParserOutput {
	
	List<IAbstractSyntaxTree> astTrees;

	public XMLParserOutput(ParserOutput parserOutput) {
		this.astTrees = XMLAstTree.getList(parserOutput);
	}

	@Override
	public List<IAbstractSyntaxTree> getAstTrees() {
		return astTrees;
	}

}
