/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core.cli;

import java.io.File;

import uk.co.flamingpenguin.jewel.cli.Option;

/**
 * The Class defining the options for the command line.<br>
 * An overview of possible commands (for more information, look in the manual):<br>
 * <br>
 * 
 * <pre>
 * Option is mandatory: --archive -a value : the archive, on which to operate
 * The options available are:
 *         [--addFile -f value] : adds an input file to the archive (the name of the testcase will be determined by the filename)(-s needed!)
 *         --archive -a value : the archive, on which to operate
 *         [--classpath -x value] : semicolon seperated list of dependencies (only for new configuration)
 *         [--configuration -c value] : sets active configuration (leave blank for default)
 *         [--export -e value] : exports log to given directory
 *         [--generate -g] : Generates input for new testCases
 *         [--help -h] : Shows this help
 *         [--information -i] : Shows Information about the archive
 *         [--newConfiguration -n value] : adds a new configuration
 *         [--output -o value] : output (for merging/saving/etc)
 *         [--overwrite -y] : in case of adding a new file, config or archive, already existing ones will be overwriten
 *         [--parser -p value] : defines the parser class (only for new configuration)
 *         [--regenerate -r] : Regenerates testCases
 *         [--removeTest value] : removes the specified test (-s is needed)
 *         [--removeTestsuite value] : removes the specified testsuite
 *         [--test -t] : Executes test, defined in the archive (archive)
 *         [--testsuite -s value] : specifies the testSuite
 * </pre>
 * 
 * 
 * 
 * @author Peter Mucha
 * 
 */
public interface CommandlineOptions {

	@Option(shortName = "t", description = "Executes test, defined in the archive (archive)")
	boolean getTest();

	boolean isTest();

	// @Option(shortName="l", description="Shows log of the archive")
	// boolean getLog();
	// boolean isLog();

	@Option(shortName = "i", description = "Shows Information about the archive")
	boolean getInformation();

	boolean isInformation();

	@Option(shortName = "g", description = "Generates input for new testCases")
	boolean getGenerate();

	boolean isGenerate();

	@Option(shortName = "r", description = "Regenerates testCases")
	boolean getRegenerate();

	boolean isRegenerate();

	@Option(shortName = "c", description = "sets active configuration (leave blank for default)")
	String getConfiguration();

	boolean isConfiguration();

	@Option(shortName = "a", description = "the archive, on which to operate")
	File getArchive();

	@Option(shortName = "o", description = "output (for merging/saving/etc)")
	File getOutput();

	boolean isOutput();

	@Option(shortName = "f", description = "adds an input file to the archive (the name of the testcase will be determined by the filename)(-s needed!)")
	File getAddFile();

	boolean isAddFile();

	@Option(shortName = "y", description = "in case of adding a new file, config or archive, already existing ones will be overwriten")
	boolean getOverwrite();

	boolean isOverwrite();

	@Option(shortName = "s", description = "specifies the testSuite")
	String getTestsuite();

	boolean isTestsuite();

	@Option(description = "removes the specified testsuite")
	String getRemoveTestsuite();

	boolean isRemoveTestsuite();

	@Option(description = "removes the specified test (-s is needed)")
	String getRemoveTest();

	boolean isRemoveTest();

	@Option(shortName = "e", description = "exports log to given directory")
	File getExport();

	boolean isExport();

	@Option(description = "Shows this help", shortName = "h", helpRequest = true)
	boolean getHelp();

	@Option(shortName = "n", description = "adds a new configuration")
	String getNewConfiguration();

	boolean isNewConfiguration();

	@Option(shortName = "p", description = "defines the parser class (only for new configuration)")
	String getParser();

	boolean isParser();

	@Option(shortName = "x", description = "semicolon seperated list of dependencies (only for new configuration)")
	String getClasspath();

	boolean isClasspath();
}
