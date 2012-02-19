package rtt.ui.core.archive.xml;

import rtt.ui.core.archive.ITestInformation;

public class XMLTestInformation implements ITestInformation {
	
//	TestInformation testInfo;

	public XMLTestInformation(){//TestInformation testInfo) {
//		this.testInfo = testInfo;
	}

	@Override
	public String getMessage() {
//		return testInfo.getMsg();
		return "";
	}

	@Override
	public String getTestName() {
//		return testInfo.getTest();
		return "";
	}

	@Override
	public Integer getPriority() {
//		return testInfo.getPriority();
		return 0;
	}
	
	

}
