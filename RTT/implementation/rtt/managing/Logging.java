/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import rtt.archive.utils.ArchiveLoader;
import rtt.logging.ArchiveLog;
import rtt.logging.Failed;
import rtt.logging.GenerationInformation;
import rtt.logging.Info;
import rtt.logging.Passed;
import rtt.logging.Skipped;
import rtt.logging.TestInformation;
import rtt.logging.Testrun;
import rtt.managing.Tester.TestFailure;

/**
 * the archive logging mechanism
 * 
 * @author Peter Mucha
 * 
 */
public class Logging {
	ArchiveLog log;
	ArchiveLoader loader = null;

	protected Logging(ArchiveLoader loader) {
		this.loader = loader;
	}

	protected void load(File archivePath) throws Exception {
		log = loader.loadLog(archivePath);

	}

	protected void save(File archivePath) throws Exception {
		loader.saveLog(log, archivePath);
	}

	protected void create() {
		log = new ArchiveLog();
	}

	public void addMerge(File secondArchive) {
		addInformational("Merged with: " + secondArchive.getAbsolutePath(), "");
	}

	public void addInformational(String message, String suffix) {
		addInformational(message, suffix, null);
	}

	public void addInformational(String message, String suffix,
			List<TestInformation> testInfo) {
		Info info = new Info();
		info.setDate(getNow());
		info.setMsg(message);
		info.setSuffix(suffix);
			
		System.out.println(message + " " + suffix);

		if (testInfo != null) {
			info.getTestInformation().addAll(testInfo);
		}
		log.getInfoOrFailedOrTestrun().add(info);
	}
	public void addGenInformational(String message, String suffix,
			List<TestInformation> testInfo) {
		Info info = new GenerationInformation();
		info.setDate(getNow());
		info.setMsg(message);
		info.setSuffix(suffix);
			
		System.out.println(message + " " + suffix);

		if (testInfo != null) {
			info.getTestInformation().addAll(testInfo);
		}
		log.getInfoOrFailedOrTestrun().add(info);
	}

	public void addTestrunResult(List<Tester.TestResult> testResults,
			String currentConfiguration) {
		Testrun tr = new Testrun();
		tr.setDate(getNow());
		tr.setConfiguration(currentConfiguration);

		for (Tester.TestResult r : testResults) {
			if (r instanceof Tester.TestFailure) {
				Tester.TestFailure f = (TestFailure) r;
				Failed fe = new Failed();
				fe.setMsg(f.getShortMessage());
				fe.setPath(f.getPath());
				fe.setTest(f.getTestCase().getName());
				fe.setTestsuit(f.getTestSuite().getName());
				tr.getFailed().add(fe);
			} else if (r instanceof Tester.TestSuccess) {
				Passed p = new Passed();
				p.setTest(r.getTestCase().getName());
				p.setTestSuite(r.getTestSuite().getName());
				tr.getPassed().add(p);
			}
			if (r instanceof Tester.TestSkipped) {
				Skipped p = new Skipped();
				p.setTest(r.getTestCase().getName());
				p.setTestSuite(r.getTestSuite().getName());
				tr.getSkipped().add(p);
			}
		}
		log.getInfoOrFailedOrTestrun().add(tr);
	}

	private XMLGregorianCalendar getNow() {
		//TimeZone.getTimeZone("GMT").
		
		
		//Date now = new Date();
		try {
			XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					new GregorianCalendar());
			cal.normalize();
			
			return cal;
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		// return XMLGregorianCalendarImpl.createDateTime(now.getYear(),
		// now.getMonth(),
		// now.getDay(), now.getHours(),
		// now.getMinutes(), now.getSeconds());
	}

}
