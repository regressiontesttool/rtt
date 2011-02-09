/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import rtt.archive.Configuration;
import rtt.archive.Configurations;
import rtt.archive.Testarchive;
import rtt.archive.Testcase;
import rtt.archive.Testsuit;
import rtt.archive.TestsuiteRef;
import rtt.archive.Testsuites;
import rtt.archive.utils.ArchiveLoader;
import rtt.archive.utils.ReferenceConverter;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class Archive {

	Testarchive currentArchive;
	ArchiveLoader currentArchiveLoader;

	public Archive(ArchiveLoader loader) {
		currentArchive = null;
		currentArchiveLoader = loader;
	}

	public void save(File archivePath) throws Exception {
		ReferenceConverter.setCurrentArchive(this);
		currentArchiveLoader.saveArchive(currentArchive, archivePath);
	}

	public void load(File archivePath) throws Exception {
		currentArchive = currentArchiveLoader.loadArchive(archivePath);
	}

	public void create() {
		currentArchive = new Testarchive();
		currentArchive.setTestsuites(new Testsuites());
		currentArchive.setConfigurations(new Configurations());
	}

	protected boolean hasConfiguration(String config) {
		try {
			Configuration c = getConfiguration(config);
			return c != null;
		} catch (Exception e) {
			return false;
		}

	}

	public Configuration getConfiguration(String config) throws Exception {
		if (config == null || config.isEmpty()) {
			config = currentArchive.getConfigurations().getDefault();
		}

		if (config == null)
			return null; //throw new Exception("No configuration specified");

		for (Configuration c : currentArchive.getConfigurations()
				.getConfiguration()) {
			if (c.getName().equals(config))
				return c;
		}

		if (Manager.verbose)
			System.out.println("Configuration [" + config + "] cannot be found");
		
		return null;
	}

	protected void addConfiguration(Configuration config, boolean defaultConfig) {
		if (currentArchive.getConfigurations().getConfiguration().size() == 0)
			defaultConfig = true;

		currentArchive.getConfigurations().getConfiguration().add(config);
		if (defaultConfig)
			currentArchive.getConfigurations().setDefault(config.getName());
	}

	protected void print() throws Exception {

		System.out.println("DefaultConfiguration: "
				+ currentArchive.getConfigurations().getDefault());
		for (Configuration c : currentArchive.getConfigurations()
				.getConfiguration()) {
			System.out.println("Config: " + c.getName());
			if (c.getLexerClass() != null)
				System.out.println("\tLexer: " + c.getLexerClass().getValue());
			if (c.getParserClass() != null)
				System.out
						.println("\tParser: " + c.getParserClass().getValue());
		}

		List<TestsuiteRef> testsuites = currentArchive.getTestsuites()
				.getTestsuite();
		System.out.println("loaded Testsuites: " + testsuites.size());

		int i = 0;
		for (TestsuiteRef ref : testsuites) {
			System.out.println("Testsuit " + (++i));
			List<Testcase> testCases = ref.getTestsuit().getTestcase();
			System.out.println("\tTestCases: " + testCases.size());
		}

	}

	protected List<Testsuit> getTestSuites() {
		List<TestsuiteRef> refs = currentArchive.getTestsuites().getTestsuite();
		// resolve references
		List<Testsuit> suits = new LinkedList<Testsuit>();

		for (TestsuiteRef ref : refs)
			suits.add(ref.getTestsuit());

		return suits;
	}

	protected void setTestSuites(List<Testsuit> suits) {
		List<TestsuiteRef> refs = currentArchive.getTestsuites().getTestsuite();
		refs.clear();

		for (Testsuit t : suits) {
			TestsuiteRef ref = new TestsuiteRef();
			ref.setTestsuit(t);
			refs.add(ref);
		}

	}

	protected Testsuit getTestSuite(String name) {
		List<TestsuiteRef> refs = currentArchive.getTestsuites().getTestsuite();
		for (TestsuiteRef ref : refs)
			if (ref.getTestsuit().getName().equals(name))
				return ref.getTestsuit();
		return null;
	}

	protected Testcase getTest(String testSuit, String test) throws Exception {
		Testsuit ts = getTestSuite(testSuit);

		if (ts == null)
			throw new Exception("Testsuit " + testSuit + " not found");

		for (Testcase t : ts.getTestcase())
			if (t.getName().equals(test))
				return t;

		return null;
	}

	protected ArchiveLoader getArchiveLoader() {
		return currentArchiveLoader;
	}

	public Testarchive getArchive() {
		return currentArchive;
	}

	protected void merge(Archive secondArchive) {
		List<Testsuit> ownSuits = this.getTestSuites();
		List<Testsuit> secSuits = secondArchive.getTestSuites();

		ownSuits.addAll(secSuits);
		this.setTestSuites(ownSuits);

		secondArchive.getArchiveLoader().addAllReferencesTo(
				currentArchiveLoader);

		// TODO: configNames

		currentArchive.getConfigurations().getConfiguration().addAll(
				secondArchive.getArchive().getConfigurations()
						.getConfiguration());

	}

	public void removeTest(String testSuitName, String testName)
			throws Exception {
		Testsuit t = getTestSuite(testSuitName);
		if (t == null)
			throw new Exception("Testcase not found");

		for (Testcase test : t.getTestcase()) {
			if (test.getName().equals(testName)) {
				t.getTestcase().remove(test);
				break;
			}
		}

	}

}
