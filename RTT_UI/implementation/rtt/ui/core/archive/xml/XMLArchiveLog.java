package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.logging.ArchiveLog;
import rtt.ui.core.archive.IFailedLogEntry;
import rtt.ui.core.archive.IInformationLogEntry;
import rtt.ui.core.archive.ILog;
import rtt.ui.core.archive.ITestrunLogEntry;

public class XMLArchiveLog implements ILog {
	
//	class XMLInfoEntry implements IInformationLogEntry {
//		
//		Info info;
//		
//		XMLInfoEntry(Info info) {
//			this.info = info;
//		}
//
//		@Override
//		public XMLGregorianCalendar getDate() {
//			return info.getDate();
//		}
//
//		@Override
//		public List<ITestInformation> getTestInformations() {
//			List<ITestInformation> testInfos = new LinkedList<ITestInformation>();
//			for (TestInformation testInfo : info.getTestInformation()) {
//				testInfos.add(new XMLTestInformation(testInfo));
//			}
//			
//			return testInfos;
//		}
//
//		@Override
//		public String getMessage() {
//			return info.getMsg();
//		}
//
//		@Override
//		public String getSuffix() {
//			return info.getSuffix();
//		}		
//	}
	
//	class XMLFailedEntry implements IFailedLogEntry {
//		
//		Failed failed;
//		
//		public XMLFailedEntry(Failed failed) {
//			this.failed = failed;
//		}
//
//		@Override
//		public String getTestName() {
//			return failed.getTest();
//		}
//
//		@Override
//		public String getPath() {
//			return failed.getPath();
//		}
//
//		@Override
//		public String getMessage() {
//			return failed.getMsg();
//		}
//
//		@Override
//		public String getTestsuiteName() {
//			return failed.getTestsuit();
//		}		
//	}
	
	ArchiveLog archiveLog;
	List<IFailedLogEntry> failed;
	List<IInformationLogEntry> infos;
	List<ITestrunLogEntry> testruns;
	
	public XMLArchiveLog(ArchiveLog archiveLog) {
		this.archiveLog = archiveLog;
		
		failed = new LinkedList<IFailedLogEntry>();
		infos = new LinkedList<IInformationLogEntry>();
		testruns = new LinkedList<ITestrunLogEntry>();
		
//		for (Object o : archiveLog.getInfoOrFailedOrTestrun()) {
//			if (o instanceof Info) {
//				infos.add(new XMLInfoEntry((Info) o));
//			}
//			
//			if (o instanceof Failed) {
//				failed.add(new XMLFailedEntry((Failed) o));
//			}
//			
//			if (o instanceof Testrun) {
//				testruns.add(new XMLTestrunLogEntry((Testrun) o));
//			}
//		}		
	}
	
	@Override
	public List<IFailedLogEntry> getFailedLogEntries() {
		return failed;
	}
	
	@Override
	public List<IInformationLogEntry> getInformationLogEntries() {
		return infos;
	}
	
	@Override
	public List<ITestrunLogEntry> getTestrunLogEntries() {
		return testruns;
	}
	


}
