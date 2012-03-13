package rtt.core.testing.generation;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.LexerClass;
import rtt.core.archive.configuration.ParserClass;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.output.Token;
import rtt.core.archive.output.Tree;
import rtt.core.archive.input.Input;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.utils.DebugLog;
import rtt.core.utils.DebugLog.LogType;

public class DataGenerator {

	public static LexerOutput generateOutput(Input data, LexerExecuter lexer)
			throws Exception {

		LexerOutput lexOut = null;

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

	public static ParserOutput generateOutput(Input data, ParserExecuter parser)
			throws Exception {

		ParserOutput parOut = null;

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

	public static LexerExecuter getLexerExecuter(Configuration config, String baseDir)
			throws RTTException {

		try {

			LexerClass lexerClass = config.getLexerClass();

			if (lexerClass != null && lexerClass.getValue() != null) {

				DebugLog.log(LogType.ALL, "Creating lexer class: "
						+ config.getLexerClass().getValue());

				return new LexerExecuter(lexerClass.getValue(),
						config.getClasspath(), baseDir);
			}

		} catch (Exception e) {
			throw new RTTException(Type.CONFIGURATION,
					"Could not generate lexer class.", e);
		}

		return null;
	}

	public static ParserExecuter getParserExecuter(Configuration config, String baseDir)
			throws RTTException {

		try {

			ParserClass parserClass = config.getParserClass();

			if (parserClass != null && parserClass.getValue() != null) {

				DebugLog.log(LogType.ALL, "Creating parser class: "
						+ config.getParserClass().getValue());

				return new ParserExecuter(parserClass.getValue(),
						config.getClasspath(), baseDir);
			}

		} catch (Exception e) {
			throw new RTTException(Type.CONFIGURATION,
					"Could not generate parser class.", e);
		}

		return null;
	}

}
