/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.cli;

/**
 * The logic for validating the given parameters.
 * 
 * @author Peter Mucha
 * 
 */
public class CommandLineValidator {
	public static boolean validate(CommandlineOptions ops) {
		int mode = 0;
		if (ops.getTest() == true)
			mode++;
		if (ops.isMerge())
			mode++;
		// if (ops.getLog()) mode++;
		if (ops.getInformation())
			mode++;
		if (ops.getGenerate())
			mode++;
		if (ops.getRegenerate())
			mode++;
		if (ops.isAddFile())
			mode++;
		if (ops.isExport())
			mode++;
		if (ops.isNewConfiguration())
			mode++;
		if (ops.isRemoveTest())
			mode++;
		if (ops.isRemoveTestsuite())
			mode++;
		if (mode != 1) {
			System.err.println("Specify one and only one mode!");
			return false;
		}
		if (ops.isMerge() && !ops.isOutput()) {
			System.err.println("Output needed");
			return false;
		}

		if (ops.isGenerate() && ops.isRegenerate()) {
			System.err
					.println("Only one option is possible (generate or regenerate)");
			return false;
		}

		if (ops.isAddFile() && !ops.isTestsuite()) {
			System.err.println("AddFile needs a testsuite to be specified");
			return false;

		}
		if (ops.isRemoveTest() && !ops.isTestsuite()) {
			System.err.println("RemoveTest needs a testsuite to be specified");
			return false;

		}
		if (ops.isNewConfiguration() && !(ops.isLexer() || ops.isParser())) {
			System.err.println("A new configuration needs a lexer or parser");
			return false;

		}

		return true;
	}

}
