package rtt.ui.content.logging;

import rtt.core.archive.logging.Comment;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.Result;
import rtt.ui.content.IContent;
import rtt.ui.content.main.ContentIcon;

public class TestResultContent extends AbstractLogContent {

	private Result result;
	private String suiteName;
	private String caseName;
	
	public TestResultContent(IContent parent, Result result) {
		super(parent);
		this.result = result;
		this.suiteName = result.getTestsuite();
		this.caseName = result.getTestcase();
		
		for (Failure failure : result.getFailure()) {
			childs.add(new FailureContent(this, failure));
		}
		
		for (Comment comment : result.getComment()) {
			childs.add(new CommentContent(this, comment));
		}
	}

	@Override
	public String getText() {
		return "[" + suiteName + "/" + caseName + "]";
	}

	@Override
	public ContentIcon getIcon() {
		return getContentIcon(result);
	}
	
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

	public Result getTestresult() {
		return result;
	}

	@Override
	public int compareTo(AbstractLogContent o) {
		if (o instanceof TestResultContent) {
			TestResultContent result = (TestResultContent) o;					
			return caseName.compareToIgnoreCase(result.caseName);
		}
		return 0;
	}

	@Override
	public String getMessage() {
		return "Testcase: " + caseName + " - Testsuite: " + suiteName;
	}

	@Override
	public String getTitle() {
		return result.getType().toString();
	}
}
