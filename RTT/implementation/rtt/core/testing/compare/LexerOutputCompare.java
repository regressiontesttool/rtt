package rtt.core.testing.compare;

import java.util.Iterator;
import java.util.List;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.Token;
import rtt.core.testing.compare.results.LexerTestFailure;

public class LexerOutputCompare {
	
	public static LexerTestFailure compareLexerOutput(LexerOutput was, LexerOutput expected, 
			boolean testInformational) {
		Iterator<Token> wasIterator = was.getToken().iterator();
		Iterator<Token> expIterator = expected.getToken().iterator();

		while (wasIterator.hasNext()) {
			Token wasToken = wasIterator.next();
			LexerTestFailure error = null;
			if (!expIterator.hasNext()) {
				return new LexerTestFailure(wasToken, null);
			}

			Token expToken = expIterator.next();
			error = compareToken(wasToken, expToken, testInformational);
			if (error != null)
				return error;

		}

		if (expIterator.hasNext()) {
			return new LexerTestFailure(null, expIterator.next());
		}

		return null;
	}
	
	private static LexerTestFailure compareToken(Token was, Token expected,
			boolean testInformational) {
		if (was.isIsEof() != expected.isIsEof())
			return new LexerTestFailure(was, expected);
		
		List<Attribute> wasAttributes = was.getAttributes();
		List<Attribute> expectedAttributes = expected.getAttributes();		
		
		if (wasAttributes.size() != expectedAttributes.size())
			return new LexerTestFailure(was, expected); 

		for (int i = 0; i < wasAttributes.size(); ++i) {
			Attribute wasAttrib = wasAttributes.get(i);
			Attribute expAttrib = expectedAttributes.get(i);

			if (wasAttrib.isInformational() && !testInformational)
				continue;

			if (!wasAttrib.getName().equals(expAttrib.getName()))
				return new LexerTestFailure(was, expected);
			if (!wasAttrib.getValue().equals(expAttrib.getValue()))
				return new LexerTestFailure(was, expected);

		}

		return null;
	}
}
