package rtt.ui.content.internal.log;

import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.Result;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.internal.ContentIcon;

public class TestResultContent extends AbstractContent {

	Result result;
	ContentIcon icon;
	
	public TestResultContent(IContent parent, Result result) {
		super(parent);
		this.result = result;
		icon = getContentIcon(result);	
		
		for (Failure failure : result.getFailure()) {
			childs.add(new FailureContent(this, failure));
		}
	}

	@Override
	public String getText() {
		return "[" + result.getTestsuite() + "/" + result.getTestcase() + "] "; // + result.getType().toString() + " (Ver.=" + result.getDataVersion()+")";
	}

	@Override
	protected ContentIcon getIcon() {
		return icon;
	}

//	@Override
//	public String getText(int columnIndex) {
//		switch (columnIndex) {
//		case 0:
//			return "RESULT";
//		
//		case 1:
//			return "[" + result.getTestsuite() + "/" + result.getTestcase() + "] " + "(V=" + result.getDataVersion()+")";
//			
//		case 2:
//			return "DATE?!";
//
//		default:
//			return "TestResult";
//		}
//	}

//	@Override
//	public Image getImage(int columnIndex, LocalResourceManager resourceManager) {
//		return getImage(resourceManager);
//	}
	
	private ContentIcon getContentIcon(Result result) {
		switch (result.getType()) {
		case FAILED:
			return ContentIcon.FAILED;
			
		case PASSED:
			return ContentIcon.PASSED;
			
		case SKIPPED:
			return ContentIcon.SKIPPED;

		default:
			return ContentIcon.PLACEHOLDER;
		}
	}
}
