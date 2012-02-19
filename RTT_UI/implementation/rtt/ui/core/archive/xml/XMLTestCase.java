package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.core.archive.IInput;
import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.core.archive.IParserOutput;
import rtt.ui.core.archive.ITestCase;

public class XMLTestCase implements ITestCase {
	
	private Testcase testCase;
	
	public XMLTestCase(Testcase testCase) {
		this.testCase = testCase;
	}

	@Override
	public String getName() {
		return testCase.getName();
	}

	@Override
	public Integer getVersion() {
		return 99999;
	}

	@Override
	public IInput getInput() {
//		if (testCase.getInput() != null) {
//			return new XMLInput(testCase.getInput());
//		}
		
		return null;	
	}

	@Override
	public ILexerOutput getLexerOutput() {
//		if (testCase.getLexerOutputRef() != null) {
//			return new XMLLexerOutput(testCase.getLexerOutputRef());
//		}
		
		return null;
	}

	@Override
	public IParserOutput getParserOutput() {
//		if (testCase.getParserOutputRef() != null) {
//			return new XMLParserOutput(testCase.getParserOutputRef());
//		}
		
		return null;
	}

	public static List<ITestCase> getList(Testsuite testsuite) {
		List<ITestCase> list = new LinkedList<ITestCase>();
		
		for (Testcase testcase : testsuite.getTestcase()) {
			list.add(new XMLTestCase(testcase));
		}
		
		return list;
	}

}
