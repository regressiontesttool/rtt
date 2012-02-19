package rtt.ui.core.archive.xml;

import java.util.List;

import rtt.core.archive.output.LexerOutput;
import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.core.archive.IToken;

public class XMLLexerOutput implements ILexerOutput {
	
	List<IToken> tokens;

	public XMLLexerOutput(LexerOutput lexerOutput) {
		tokens = XMLToken.getList(lexerOutput);
	}

	@Override
	public List<IToken> getToken() {
		return tokens;
	}

}
