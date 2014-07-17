package rtt.core.testing.generation;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.output.Tree;
import rtt.core.utils.RTTLogging;

public class DataGenerator {

	public static ParserOutput generateOutput(Input input, List<String> params,
			ParserExecutor parser) throws Throwable {

		ParserOutput parOut = new ParserOutput();

		if (parser != null) {
			parOut = new ParserOutput();

			RTTLogging.debug("Initialize parser ...");
			parser.initialize(input, params);
			
			RTTLogging.debug("Generating parser output data ...");
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

	/**
	 * <p>Tries to locate the {@link ParserExecutor} via the class loader</p>
	 * 
	 * <p>Instantiate the parser through
	 * {@link Executor#initialize(Input, List)} before use!</p>
	 * 
	  * @param config the {@link Configuration}
	 * @param baseDir the base directory for searching
	 * @return a {@link ParserExecutor} or null (if config is empty)
	 * @throws Exception
	 *             mainly exceptions during class loading
	 */
	public static ParserExecutor locateParserExecutor(Configuration config,
			String baseDir) throws Exception {
		
		String parserClass = config.getParserClass();
		if (parserClass != null && !parserClass.trim().isEmpty()) {
			RTTLogging.info("Parser: " + parserClass);
			return new ParserExecutor(parserClass, config.getClasspath(), baseDir);
		}
		
		RTTLogging.info("Parser: <none>");

		return null;
	}

}
