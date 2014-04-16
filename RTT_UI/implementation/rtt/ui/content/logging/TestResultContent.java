package rtt.ui.content.logging;

import rtt.core.archive.logging.Comment;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.FailureType;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.Testrun;
import rtt.ui.content.main.ContentIcon;

public class TestResultContent extends AbstractLogContent {

	private Testrun testrun;
	private Result result;
	private String suiteName;
	private String caseName;
	
	public TestResultContent(TestrunContent parent, Result result) {
		super(parent);
		this.testrun = parent.getTestrun();
		this.result = result;
		this.suiteName = result.getTestsuite();
		this.caseName = result.getTestcase();
		
		Failure lexerFailure = null;
		Failure parserFailure = null;
		
		for (Failure failure : result.getFailure()) {
			if (failure.getType() == FailureType.LEXER && lexerFailure == null) {
				lexerFailure = failure;
			}
			
			if (failure.getType() == FailureType.PARSER && parserFailure == null) {
				parserFailure = failure;
			}
		}
		
		if (lexerFailure != null) {
			childs.add(new FailureContent(this, lexerFailure));
		}
		
		if (parserFailure != null) {
			childs.add(new FailureContent(this, parserFailure));
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
	public String getToolTip() {
		return "Data used for testing: \n"
				+ "- Reference Data: Version " + result.getRefVersion() + "\n"
				+ "- Test Data: Version "+ result.getTestVersion();
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
	
	public Testrun getTestrun() {
		return testrun;
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
		return "Testcase: " + caseName + ", Testsuite: " + suiteName;
	}

	@Override
	public String getTitle() {
		return result.getType().toString();
	}

	
}
