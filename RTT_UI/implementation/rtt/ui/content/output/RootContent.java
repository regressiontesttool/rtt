package rtt.ui.content.output;

import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.output.Token;
import rtt.core.archive.output.Tree;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.ContentIcon;

public class RootContent extends AbstractContent {
	
	private String text = "RootContent";
	
	public RootContent(LexerOutput lexerOutput) {
		super(null);
		text = "Lexer Output";
		
		for (Token token : lexerOutput.getToken()) {
			childs.add(new TokenContent(this, token));
		}
	}

	public RootContent(ParserOutput parserOutput) {
		super(null);
		text = "Parser Output";
		
		for (Tree tree : parserOutput.getTree()) {
			childs.add(new AstTreeContent(this, tree));
		}
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PLACEHOLDER;
	}

}
