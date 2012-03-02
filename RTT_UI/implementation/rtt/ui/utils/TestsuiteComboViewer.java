package rtt.ui.utils;

import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import rtt.ui.content.IContent;
import rtt.ui.content.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;

public class TestsuiteComboViewer extends ComboViewer {

	private class TestsuiteComboContentProvider implements
			IStructuredContentProvider {

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Object[]) {
				return (Object[]) inputElement;
			}

			if (inputElement instanceof ProjectContent) {
				List<IContent> testSuites = ((ProjectContent) inputElement)
						.getTestsuiteContents();

				if (testSuites == null || testSuites.isEmpty()) {
					return new String[] { "No test suites found." };
				}

				return testSuites.toArray();
			}

			return new Object[0];
		}
	}

	private class TestsuiteComboLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IContent) {
				return ((IContent) element).getText();
			}

			return super.getText(element);
		}
	}

	public TestsuiteComboViewer(Composite parent, int style) {
		super(parent, style);

		this.setContentProvider(new TestsuiteComboContentProvider());
		this.setLabelProvider(new TestsuiteComboLabelProvider());

//		this.setInput(ProjectFinder.getCurrentProjectContent());
//		this.getCombo().select(0);
//
//		if (!this.getCombo().getItem(0).equals("No test suites found.")) {
//			selectedTestsuite = this.getCombo().getItem(0);
//		}
	}

	private TestsuiteContent getSelectedTestsuiteContent() {
		if (this.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) this.getSelection();
			if (selection.getFirstElement() instanceof TestsuiteContent) {
				return ((TestsuiteContent) selection.getFirstElement());
			}
		}
		
		return null;
	}
	
	public boolean isTestsuiteSelected() {
		return getSelectedTestsuiteContent() != null;
	}
	
	public String getSelectedTestsuite() {
		TestsuiteContent content = getSelectedTestsuiteContent();
		
		if (content != null) {
			return getSelectedTestsuiteContent().getText();
		}
		
		return "";		
	}

//	@Override
//	public void updateContent(ProjectContent currentProjectContent) {
//		this.setInput(currentProjectContent);
//		this.getCombo().select(0);
//	}

	

}
