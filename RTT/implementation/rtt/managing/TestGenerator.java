/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import rtt.archive.Configuration;
import rtt.archive.Input;
import rtt.archive.LexerOutput;
import rtt.archive.LexerOutputRef;
import rtt.archive.ParserOutput;
import rtt.archive.ParserOutputRef;
import rtt.archive.Testcase;
import rtt.archive.Testsuit;
import rtt.logging.TestInformation;
import rtt.managing.Tester.LexerTestFailure;
import rtt.managing.Tester.ParserTestFailure;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class TestGenerator extends TestRunner {

	public TestGenerator() {

	}

	/**
	 * gerenrates all testresults if regenerate = true, then all testcases will
	 * be regenerated (if testcases differ, old results will be versioned)
	 * 
	 * @param currentArchive
	 * @param configuration
	 * @param testSuiteName
	 * @param regenerate
	 * @param baseDir
	 * @throws Exception
	 */
	public List<TestInformation> generateTests(Archive currentArchive,
			Configuration configuration, String testSuiteName,
			String baseDir, Manager manager)
			throws Exception {
		List<TestInformation> result = new LinkedList<TestInformation>();
		LexerExecuter lexer = null;
		if (configuration.getLexerClass() != null
				&& configuration.getLexerClass().getValue() != null) {
			System.out.println("Using lexer class: "
					+ configuration.getLexerClass().getValue());
			lexer = new LexerExecuter(configuration.getLexerClass().getValue(),
					configuration.getClasspath(), baseDir);
		}

		ParserExecuter parser = null;
		if (configuration.getParserClass() != null
				&& configuration.getParserClass().getValue() != null) {
			System.out.println("Using parser class: "
					+ configuration.getParserClass().getValue());
			parser = new ParserExecuter(configuration.getParserClass()
					.getValue(), configuration.getClasspath(), baseDir);
		}

		for (Testsuit t : currentArchive.getTestSuites()) {
			if (testSuiteName != null && !testSuiteName.equals(t.getName()))
				continue;

			for (Testcase test : t.getTestcase()) {
				boolean somethingDone = false;
				boolean replaceLexerOutput = false;
				boolean replaceParserOutput = false;
				boolean parserOutputExist = false;
				boolean lexerOutputExist = false;

				LexerOutput lo = null;
				ParserOutput po = null;

				Input in = test.getInput();
				if (in == null)
					in = test.getInputRef().getInput();

				try{
					// if no lexerOutput is set, generate it!
					if (lexer != null) {
						LexerOutput existingLexerOutput = test.getLexerOutput();
						if (existingLexerOutput == null
								&& test.getLexerOutputRef() != null)
							existingLexerOutput = test.getLexerOutputRef()
									.getLexerOutput();
	
						lexerOutputExist = existingLexerOutput != null;
	
						somethingDone = true;
						lo = this.generateLexerResult(lexer, in);
						if (lexerOutputExist) {
							// check, if the results are really new (if so,
							// version old ones)
							Tester tmpTester = new Tester();
							// compare informationalAttribs too!
							if (tmpTester.compare(lo, existingLexerOutput,
									null, null, true) != null) {
								// tests ARE different, version old results!
								replaceLexerOutput = true;
							}
						} else replaceLexerOutput = true;
					}
	
					// if no parserOutput is set, generate it!
					if (parser != null) {
						ParserOutput existingParserOutput = test.getParserOutput();
						if (existingParserOutput == null
								&& test.getParserOutputRef() != null)
							existingParserOutput = test.getParserOutputRef()
									.getParserOutput();
	
						parserOutputExist = existingParserOutput != null;
	
						somethingDone = true;
						po = this.generateParserResult(parser, in);
						
						if (parserOutputExist) {
							// check, if the results are really new (if so,
							// version old ones)
							Tester tmpTester = new Tester();
							// Strict comparing everytime (is not THAT bad, if
							// result will be overwritten if false positive)
							List<ParserTestFailure> ptr = tmpTester
									.compareStrict(po, existingParserOutput,
											null, null, false);
							if (!(ptr == null || ptr.size() == 0)) {
								// tests ARE different, version old results!
								replaceParserOutput = true;
							} 
						} else replaceParserOutput = true;
					}
	
					if ((replaceLexerOutput && lexerOutputExist)
							|| (replaceParserOutput && parserOutputExist)) {
						if (replaceLexerOutput) {
							TestInformation info = new TestInformation();
							info.setMsg("Lexer output for test [" + test.getName()
									+ "] in testsuite [" + t.getName()
									+ "] were versioned.");
							info.setPriority(2);
							info.setTest(test.getName());
							result.add(info);
						}
						if (replaceParserOutput) {
							
						}
	
						test.setVersion(test.getVersion() + 1);
						manager.versionResults(t, test, configuration,
								replaceLexerOutput, replaceParserOutput);
					}
	
					if (replaceLexerOutput) {
	
						LexerOutputRef ref = new LexerOutputRef();
						ref.setLexerOutput(lo);
						currentArchive.getArchiveLoader().setFileFor(lo,
								t.getName() + "/" + test.getName() + ".lexer.xml");
						test.setLexerOutputRef(ref);
	
					}
	
					if (replaceParserOutput) {
						ParserOutputRef ref = new ParserOutputRef();
						ref.setParserOutput(po);
						currentArchive.getArchiveLoader().setFileFor(po,
								t.getName() + "/" + test.getName() + ".parser.xml");
						test.setParserOutputRef(ref);
	
					}
	
					if (somethingDone && (replaceLexerOutput || replaceParserOutput)) {
						TestInformation info = new TestInformation();
						
						String message= "";
						if (replaceLexerOutput)
							message += "Lexer ";
						if (replaceLexerOutput && replaceParserOutput)
							message += "and ";
						if (replaceParserOutput)
							message += "Parser ";
						
						
						info.setMsg(message += "Results for testCase [" + test.getName()
								+ "] in testsuite [" + t.getName()
								+ "] were generated.");
						info.setPriority(1);
						info.setTest(test.getName());
						result.add(info);
					}
					else
					{
						TestInformation info = new TestInformation();
						info.setMsg("Results for test ["
								+ test.getName() + "] in testsuite ["
								+ t.getName() + "] were not changed.");
						info.setPriority(0);
						info.setTest(test.getName());
						result.add(info);
					}
				}
				catch(Throwable tex)
				{
					if (tex instanceof InvocationTargetException)
						tex = tex.getCause();
					
					if (Manager.verbose)
						tex.printStackTrace();
					manager.currentLog.addInformational(
							"Error during generation of Test Results: "
									+tex, t.getName() + "/" + test.getName());
					continue;
				}

			}

		}
		return result;
	}

}
