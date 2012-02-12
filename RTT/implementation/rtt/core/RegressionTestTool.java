/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.logging.Detail;
import rtt.core.cli.CommandLineValidator;
import rtt.core.cli.CommandlineOptions;
import rtt.core.manager.Manager;
import rtt.core.manager.Manager.TestCaseMode;
import uk.co.flamingpenguin.jewel.cli.Cli;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

/**
 * Includes the main method for starting RTT. See
 * {@link rtt.core.cli.CommandlineOptions} on how to use RTT.
 * 
 * @author Peter Mucha
 */
public class RegressionTestTool {

	/**
	 * Starts RTT. For more informations about the parameters, see
	 * {@link rtt.core.cli.CommandlineOptions}.
	 * 
	 * @param args
	 * @throws Throwable 
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
			if (options.isConfiguration()) {
				m.loadArchive(options.getConfiguration());
			} else {
				m.loadArchive();
			}
		} else {
			m.createArchive();
		}
			

		if (options.getInformation())
			m.printArchiveInformations();

		if (options.isGenerate()) {
			m.generateTests(null);
		} else if (options.isRegenerate()) {
			m.generateTests(null);
		} else if (options.isTest()) {
			m.runTests(null, true);
		} else if (options.isAddFile()) {
			List<Detail> r = m.addFile(options.getAddFile(), options
					.getTestsuite(), options.isOutput() ? TestCaseMode.OVERWRITE : TestCaseMode.SKIP);
			m.getArchive().getLogManager().addInformational("File added", "", r);
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
