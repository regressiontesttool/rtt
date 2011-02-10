/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import java.util.List;

import rtt.archive.Input;
import rtt.archive.LexerOutput;
import rtt.archive.Node;
import rtt.archive.ParserOutput;
import rtt.archive.Token;
import rtt.archive.Tree;

/**
 * 
 * @author Peter Mucha
 * 
 */
public abstract class TestRunner {
	public LexerOutput generateLexerResult(LexerExecuter lexer, Input input)
			throws Exception {
		LexerOutput l = new LexerOutput();

		lexer.loadInput(input);

		while (true) {
			Token t = lexer.getToken();
			l.getToken().add(t);
			if (t.isIsEof())
				break;
		}

		return l;
	}

	public ParserOutput generateParserResult(ParserExecuter parser, Input input)
			throws Exception {
		ParserOutput p = new ParserOutput();

		parser.loadInput(input);

		List<Node> trees = parser.getAst();
		for (Node n : trees) {
			Tree t = new Tree();
			t.getNode().add(n);
			p.getTree().add(t);
		}

		return p;
	}
}
