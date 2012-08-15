package rtt.core.testing.generation;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.output.Token;
import rtt.core.archive.output.Tree;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.utils.Debug;
import rtt.core.utils.Debug.LogType;

public class DataGenerator {

	public static LexerOutput generateOutput(Input data, LexerExecutor lexer)
			throws Exception {	

		LexerOutput lexOut = new LexerOutput();

		if (lexer != null) {
			lexOut = new LexerOutput();

			lexer.loadInput(data);

			while (true) {
				Token t = lexer.getToken();
				lexOut.getToken().add(t);
				if (t.isIsEof())
					break;
			}
		}

		return lexOut;
	}

	public static ParserOutput generateOutput(Input data, ParserExecutor parser)
			throws Exception {

		ParserOutput parOut = new ParserOutput();

		if (parser != null) {
			parOut = new ParserOutput();

			parser.loadInput(data);

			List<Node> treeNodes = parser.getAst();
			for (Node astTree : treeNodes) {
				Tree tree = new Tree();
				tree.setSimpleName(parser.getSimpleName());
				tree.setFullName(parser.getFullName());
				tree.getNode().add(astTree);
				parOut.getTree().add(tree);
			}

		}

		return parOut;
	}

	public static LexerExecutor getLexerExecutor(Configuration config,
			String baseDir) throws RTTException {

		try {

			String lexerClass = config.getLexerClass();

			if (lexerClass != null && !lexerClass.equals("")) {

				Debug.log(LogType.ALL, "Creating lexer class: " + lexerClass);

				return new LexerExecutor(lexerClass, config.getClasspath(),
						baseDir);
			}

		} catch (Exception e) {
			throw new RTTException(Type.OPERATION_FAILED,
					"Could not generate lexer class.", e);
		}

		return null;
	}

	public static ParserExecutor getParserExecutor(Configuration config,
			String baseDir) throws RTTException {

		try {

			String parserClass = config.getParserClass();

			if (parserClass != null && !parserClass.equals("")) {

				Debug.log(LogType.ALL, "Creating parser class: "
						+ parserClass);

				return new ParserExecutor(parserClass, config.getClasspath(),
						baseDir);
			}

		} catch (Exception e) {
			throw new RTTException(Type.OPERATION_FAILED,
					"Could not generate parser class.", e);
		}

		return null;
	}

}
