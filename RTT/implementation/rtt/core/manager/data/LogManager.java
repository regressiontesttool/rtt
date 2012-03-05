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

public class LogManager extends DataManager<ArchiveLog> {

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

	public void addInformational(String message, String suffix) {
		addInformational(message, suffix, null);
	}

	public void addInformational(String message, String suffix,
			List<Detail> testInfo) {
		Entry info = new Entry();
		info.setDate(getNow());
		info.setMsg(message);
		info.setSuffix(suffix);
		info.setType(EntryType.INFO);

		DebugLog.log(message + " " + suffix);

		if (testInfo != null) {
			info.getDetail().addAll(testInfo);
		}
		data.getEntry().add(info);
	}

	public void addGenInformational(String message, String suffix,
			List<Detail> testInfo) {
		Entry info = new Entry();
		info.setDate(getNow());
		info.setMsg(message);
		info.setSuffix(suffix);
		info.setType(EntryType.GENERATION);

		DebugLog.log(message + " " + suffix);

		if (testInfo != null) {
			info.getDetail().addAll(testInfo);
		}
		data.getEntry().add(info);
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

	private XMLGregorianCalendar getNow() {
		try {
			XMLGregorianCalendar cal = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(new GregorianCalendar());
			cal.normalize();

			return cal;
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected ArchiveLog getEmptyData() {
		return new ArchiveLog();
	}

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
