package rtt.ui.views.utils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rtt.ui.content.logging.FailureContent;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.content.logging.TestrunContent;

public class SuiteFilter extends ViewerFilter {

	private String suiteName;

	public SuiteFilter(String suiteName) {
		this.suiteName = suiteName;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement,
			Object element) {
		
		if (element instanceof TestResultContent) {
			return true;
		}

		if (element instanceof FailureContent) {
			return true;
		}

		if (element instanceof TestrunContent) {
			TestrunContent testrunContent = (TestrunContent) element;

			if (testrunContent.getTestsuite().equals(suiteName)) {
				return true;
			}
		}	

		return false;
	}
}