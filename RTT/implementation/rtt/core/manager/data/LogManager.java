package rtt.core.manager.data;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

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
import rtt.core.utils.Debug;

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
			Debug.printTrace(e);
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

		Debug.log(message + " " + suffix);

		data.getEntry().add(entry);
	}

	/**
	 * Adds an {@link Entry} to the {@link ArchiveLog}, without details.
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
			String configName, String suiteName) {
		
		Testrun tr = new Testrun();
		tr.setDate(getNow());
		tr.setConfiguration(configName);
		tr.setType(EntryType.TESTRUN);
		
		String message = "Testrun started for test suite [" + suiteName + "] ";  
		if (testResults.isEmpty()) {
			message += " was empty. Configuration used: ";
		} else {
			message += "with configuration: ";
		}		
		tr.setMsg(message);
		
		tr.setSuffix(configName);
		tr.setTestsuite(suiteName);

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
	 * @throws Exception
	 *             thrown, if any error occur during export
	 */
	public void export(File location) throws Exception {
		
		String fileName = "log";
		
		if (!location.isDirectory()) {
			fileName = location.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
			
			location = location.getParentFile();
			
			if (fileName.equals("") || location == null) {
				throw new RuntimeException("The location of export couldn't be found. (was: " + location + ")");
			}
		}
		
		File resultFile = new File(location.getAbsolutePath() + File.separatorChar + fileName + ".htm");		
		
		doSave(data);
		InputStream logStream = getInputStream();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.parse(logStream);   
        
        StreamSource styleSource = new StreamSource(getClass().getResourceAsStream("/log.xslt"));

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(styleSource);
        
        Writer out = new FileWriter(resultFile);
        
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);

		Debug.log("Logging File exported to: " + resultFile.getCanonicalPath());
	}
}
