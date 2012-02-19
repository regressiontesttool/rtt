package rtt.ui.editors.input;

import org.eclipse.ui.IEditorInput;

import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.core.archive.IParserOutput;

public interface ITestCaseEditorInput extends IEditorInput {
	
	IParserOutput getParserOuput();
	ILexerOutput getLexerOutput();

}
