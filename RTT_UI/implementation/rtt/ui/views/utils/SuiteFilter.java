package rtt.ui.views.utils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.logging.FailureContent;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.content.logging.TestrunContent;

public class SuiteFilter extends ViewerFilter {

	private String suiteName;
	private String configName;

	public SuiteFilter(String suiteName, Configuration configuration) {
		this.suiteName = suiteName;
		this.configName = "";
		if (configuration != null) {
			this.configName = configuration.getName();
		}
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

			if (testrunContent.getTestsuite().equals(suiteName) &&
					testrunContent.getConfiguration().equals(configName)) {
				return true;
			}
		}	

		return false;
	}
}