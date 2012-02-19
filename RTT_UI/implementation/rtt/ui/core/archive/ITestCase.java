package rtt.ui.core.archive;

public interface ITestCase {
	
	String getName();
	Integer getVersion();
	
	IInput getInput();
	ILexerOutput getLexerOutput();
	IParserOutput getParserOutput();
}
