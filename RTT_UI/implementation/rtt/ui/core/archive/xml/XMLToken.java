package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.Token;
import rtt.ui.core.archive.IAttribute;
import rtt.ui.core.archive.IToken;

public class XMLToken implements IToken {
	
	private List<IAttribute> attributes;
	private boolean isEof;
	private String className;

	public XMLToken(Token token) {
		this.attributes = XMLAttribute.getList(token);
		this.isEof = token.isIsEof();
		this.className = token.getFullName();
	}

	@Override
	public List<IAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public boolean isEndOfFile() {
		return isEof;
	}

	public static List<IToken> getList(LexerOutput lexerOutput) {
		List<IToken> list = new LinkedList<IToken>();
		
		if (lexerOutput != null) {
			for (Token token : lexerOutput.getToken()) {
				list.add(new XMLToken(token));
			}			
		}		
		
		return list;
	}

	@Override
	public String getClassName() {
		return className;
	}

}
