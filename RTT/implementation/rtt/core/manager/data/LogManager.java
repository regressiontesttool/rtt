package rtt.core.manager.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import rtt.core.archive.logging.ArchiveLog;
import rtt.core.archive.logging.Detail;
import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.EntryType;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.FailureType;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.ResultType;
import rtt.core.archive.logging.Testrun;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.testing.compare.results.ITestFailure;
import rtt.core.testing.compare.results.LexerTestFailure;
import rtt.core.testing.compare.results.ParserTestFailure;
import rtt.core.testing.compare.results.TestResult;
import rtt.core.utils.DebugLog;

/**
 * This manager provides all operations on the {@link ArchiveLog}.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 * @see AbstractDataManager
 * @see ArchiveLog
 * 
 */
public class LogManager extends AbstractDataManager<ArchiveLog> {

	/**
	 * Creates a new log manager for the given {@link ArchiveLoader}.
	 * 
	 * @param loader
	 *            the {@link ArchiveLoader}
	 */
	public LogManager(ArchiveLoader loader) {
		super(loader, new SimpleFileFetching("log.xml", ""));
	}

	@Override
	protected ArchiveLog doLoad() throws Exception {
		return unmarshall(ArchiveLog.class);
	}

	@Override
	protected void doSave(ArchiveLog data) {
		marshall(ArchiveLog.class, data);
	}

	@Override
	protected ArchiveLog getEmptyData() {
		return new ArchiveLog();
	}

	private Entry createEntry(String message, String suffix, EntryType type) {
		Entry entry = new Entry();
		entry.setDate(getNow());
		entry.setMsg(message);
		entry.setSuffix(suffix);
		entry.setType(type);

		return entry;
	}
	
	private XMLGregorianCalendar getNow() {
		try {
			XMLGregorianCalendar cal = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(new GregorianCalendar());
			cal.normalize();

			return cal;
		} catch (DatatypeConfigurationException e) {
			DebugLog.printTrace(e);
			return null;
		}
	}

	/**
	 * Adds an {@link Entry} to the {@link ArchiveLog}, with a list of
	 * {@link Detail}s.
	 * 
	 * @param type
	 *            the {@link EntryType}
	 * @param message
	 *            the message of the new entry
	 * @param suffix
	 *            the suffix of the new entry
	 * @param details
	 *            a list of {@link Detail}s
	 * @see #addEntry(EntryType, String, String)
	 * @see ArchiveLog
	 * @see Entry
	 * @see EntryType
	 * @see Detail
	 */
	public void addEntry(EntryType type, String message, String suffix,
			List<Detail> details) {

		Entry entry = createEntry(message, suffix, type);
		if (details != null) {
			entry.getDetail().addAll(details);
		}

		DebugLog.log(message + " " + suffix);

		data.getEntry().add(entry);
	}

	/**
	 * Adds an {@link Entry} to the {@link ArchiveLog}, without
	 * 
	 * @param type
	 *            the {@link EntryType}
	 * @param message
	 *            the message of the new entry
	 * @param suffix
	 *            the suffix of the new entry
	 * @see #addEntry(EntryType, String, String, List)
	 * @see ArchiveLog
	 * @see Entry
	 * @see EntryType
	 * @see Detail
	 */
	public void addEntry(EntryType type, String message, String suffix) {
		addEntry(type, message, suffix, null);
	}

	public void addTestrunResult(List<TestResult> testResults,
			String configName, String testsuite) {
		Testrun tr = new Testrun();
		tr.setDate(getNow());
		tr.setConfiguration(configName);
		tr.setType(EntryType.TESTRUN);
		tr.setMsg("Testrun started with configuration: ");
		tr.setSuffix(configName);
		tr.setTestsuite(testsuite);

		for (TestResult result : testResults) {

			Result resultEntry = new Result();
			resultEntry.setTestcase(result.getCaseName());
			resultEntry.setTestsuite(result.getSuiteName());

			resultEntry.setRefVersion(result.getRefVersion());
			resultEntry.setTestVersion(result.getTestVersion());

			switch (result.getType()) {

			case FAILURE:
				List<ITestFailure> failures = result.getFailures();
				for (ITestFailure failure : failures) {
					Failure failureEntry = new Failure();
					failureEntry.setMsg(failure.getShortMessage());
					failureEntry.setPath(failure.getPath());

					if (failure instanceof LexerTestFailure) {
						failureEntry.setType(FailureType.LEXER);
					} else if (failure instanceof ParserTestFailure) {
						failureEntry.setType(FailureType.PARSER);
					}

					resultEntry.getFailure().add(failureEntry);
				}
				resultEntry.setType(ResultType.FAILED);
				break;

			case SUCCESS:
				resultEntry.setType(ResultType.PASSED);
				break;

			case SKIPPED:
				resultEntry.setType(ResultType.SKIPPED);
				break;
			}

			tr.getResult().add(resultEntry);
		}
		data.getEntry().add(tr);
	}

	/**
	 * Exports the complete {@link ArchiveLog} to a given location.
	 * 
	 * @param location
	 *            the location, where the log should be exported
	 * @throws IOException
	 *             thrown, if any error occur during export
	 */
	public void export(File location) throws IOException {

		File dir = location;

		if (!dir.isDirectory())
			dir = location.getCanonicalFile().getParentFile();
		if (location.isDirectory())
			location = new File(dir.getAbsolutePath() + File.separator
					+ "log.xml");

		DebugLog.log("Dir:  " + dir + "\nLoc: " + location.getAbsolutePath());

		FileOutputStream logOut = new FileOutputStream(location);
		marshall(ArchiveLog.class, data, logOut, "log.xslt");

		InputStream logStream = getClass().getResourceAsStream("/log.xslt");
		File f = new File(dir.getAbsolutePath() + "/log.xslt");

		FileOutputStream fos = new FileOutputStream(f, false);
		Reader r = new InputStreamReader(logStream);
		int curByte = -1;
		while ((curByte = r.read()) != -1) {
			fos.write(curByte);
		}
		r.close();
		fos.close();
		logStream.close();

		DebugLog.log("Logging File exported to: " + location);
	}

}
