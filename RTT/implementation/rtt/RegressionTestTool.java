/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt;

import java.util.LinkedList;
import java.util.List;

import rtt.cli.CommandLineValidator;
import rtt.cli.CommandlineOptions;
import rtt.logging.TestInformation;
import rtt.managing.Manager;
import uk.co.flamingpenguin.jewel.cli.Cli;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

/**
 * Includes the main method for starting RTT. See
 * {@link rtt.cli.CommandlineOptions} on how to use RTT.
 * 
 * @author Peter Mucha
 */
public class RegressionTestTool {

	/**
	 * Starts RTT. For more informations about the parameters, see
	 * {@link rtt.cli.CommandlineOptions}.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Throwable {
		Cli<CommandlineOptions> optionParser = CliFactory
				.createCli(CommandlineOptions.class);
		CommandlineOptions options = null;
		try {
			options = optionParser.parseArguments(args);
			if (!CommandLineValidator.validate(options)) {
				System.err.println(optionParser.getHelpMessage());
				return;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(optionParser.getHelpMessage());
			return;
		}

		Manager m = new Manager(options.getArchive(), true);

		if (options.getArchive().exists() && !options.isOverwrite()) {
			if (options.isConfiguration())
				m.loadArchive(options.getConfiguration());
			else
				m.loadArchive();
		} else
			m.createArchive();

		if (options.getInformation())
			m.printArchiveInformations();

		if (options.isMerge()) {
			m.merge(options.getMerge());
		} else if (options.isGenerate()) {
			m.generateTests(null);
		} else if (options.isRegenerate()) {
			m.generateTests(null);
		} else if (options.isTest()) {
			m.runTests(null, true);
		} else if (options.isAddFile()) {
			List<TestInformation> r = m.addFile(options.getAddFile(), options
					.getTestsuite(), options.isOutput() ? 3 : 0);
			m.currentLog.addInformational("File added", "", r);
		} else if (options.isRemoveTestsuite()) {
			m.removeTestsuite(options.getRemoveTestsuite());
		} else if (options.isRemoveTest()) {
			m.removeTest(options.getTestsuite(), options.getRemoveTest());
		} else if (options.isExport()) {
			m.exportLog(options.getExport());
		} else if (options.isNewConfiguration()) {
			
			List<String> cpEntries = new LinkedList<String>();
			
			if (options.isClasspath()) {
				for (String cpe : options.getClasspath().split(";"))
					cpEntries.add(cpe);
			}
			
			
			m.createConfiguration(options.getLexer(), options.getParser(),
					options.getNewConfiguration(), true, options.isOverwrite(), cpEntries);

			
		}

		if (options.isOutput()) {
			m.saveArchive(options.getOutput());
		}

	}

}
