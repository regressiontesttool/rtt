package rtt.core.testing.compare.results;

import rtt.core.archive.output.Token;
import rtt.core.manager.Printer;

public class LexerTestFailure implements ITestFailure {
	Token was, expected;

	public LexerTestFailure(Token was, Token expected) {
		this.was = was;
		this.expected = expected;
	}

	@Override
	public String getMessage() {
		return "Expected Token:\n" + Printer.PrintToken(expected)
				+ "\nbut Token was:\n" + Printer.PrintToken(was);
	}

	@Override
	public String getShortMessage() {
		return "Token " + Printer.PrintToken(was)
				+ " differs from expected token "
				+ Printer.PrintToken(expected);
	}

	@Override
	public String getPath() {
		return "";
	}
}
