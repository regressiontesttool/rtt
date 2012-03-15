package rtt.ui.views;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.content.logging.TestrunContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.TestsuiteDirectory;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.utils.AbstractTestRunnable;
import rtt.ui.utils.GenerateTestRunnable;
import rtt.ui.utils.RunTestRunnable;
import rtt.ui.viewer.BaseContentLabelProvider;
import rtt.ui.viewer.BaseContentProvider;
import rtt.ui.viewer.ContentTreeViewer;
import rtt.ui.views.utils.AbstractProjectListener;
import rtt.ui.views.utils.AbstractTestsuiteListener;
import rtt.ui.views.utils.SuiteFilter;

public class TestView extends ViewPart implements ISelectionListener {

	private class ProjectListener extends AbstractProjectListener {

		@Override
		public void refresh() {
			contentViewer.refresh(true);
			comboViewer.refresh(true);

			super.refresh();
		}

		@Override
		public void update(ProjectContent content) {
			if (content != null) {
				TestsuiteDirectory suiteDirectory = content
						.getTestsuiteDirectory();
				comboViewer.setInput(suiteDirectory);
				comboViewer.getControl().setEnabled(!suiteDirectory.isEmpty());
				
				contentViewer.setInput(content.getLogDirectory());

				if (suiteDirectory.isEmpty() == false) {
					TestsuiteContent suite = suiteDirectory.getTestsuite(0);
//					RttPluginUI.getSuiteManager().setCurrentContent(suite);
					
//					comboViewer.setSelection(new StructuredSelection(suite));
					contentViewer
							.setFilters(new ViewerFilter[] { new SuiteFilter(
									suite.getText()) });
				}

				

				// List<IContent> contentList = content.getTestsuiteContents();
				// if (contentList.size() > 0) {
				//
				// IContent firstElement = contentList.get(0);
				//
				// comboViewer.getControl().setEnabled(true);
				//
				// contentViewer.setInput(content.getLogDirectory());
				// contentViewer
				// .setFilters(new ViewerFilter[] { new SuiteFilter(
				// firstElement.getText()) });
				// contentViewer.getControl().setEnabled(true);
				// } else {
				// comboViewer.getControl().setEnabled(false);
				// contentViewer.setInput(new Object[0]);
				// contentViewer.getControl().setEnabled(false);
				// }
			}
		}
	}

	private class TestsuiteListener extends AbstractTestsuiteListener {

		@Override
		public void refresh() {
			comboViewer.refresh(true);

			super.refresh();
		}

		@Override
		public void update(TestsuiteContent content) {
			if (content != null) {
				comboViewer.setSelection(new StructuredSelection(content));
				contentViewer.setFilters(new ViewerFilter[] { new SuiteFilter(
						content.getText()) });
			}
		}
	}

	private static class TestrunComparator extends ViewerComparator {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof TestrunContent && e2 instanceof TestrunContent) {
				TestrunContent testrun1 = (TestrunContent) e1;
				TestrunContent testrun2 = (TestrunContent) e2;

				return -(testrun1.getCalendar().compareTo(testrun2
						.getCalendar()));
			}

			return super.compare(viewer, e1, e2);
		}
	}

	public static final String ID = "rtt.ui.views.TestView";

	private static final String descrText = "Select a test suite "
			+ "from the current project. Click \"Generate tests\" "
			+ "to generate new reference data or click \"Run tests\" "
			+ "to start a new test run.";

	private ContentTreeViewer contentViewer;
	private ComboViewer comboViewer;

	private ProjectListener projectListener;
	private TestsuiteListener suiteListener;

	public TestView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Group runGroup = new Group(parent, SWT.NONE);
		runGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		runGroup.setText("Testing");
		runGroup.setLayout(new GridLayout(2, false));

		Label descrLabel = new Label(runGroup, SWT.WRAP);
		descrLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 2, 1));
		descrLabel.setText(descrText);

		Label suiteLabel = new Label(runGroup, SWT.NONE);
		suiteLabel.setText("Testsuite:");

		comboViewer = new ComboViewer(runGroup, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		comboViewer.setLabelProvider(new BaseContentLabelProvider());
		comboViewer.setContentProvider(new BaseContentProvider());
		
		Button btnGenerate = new Button(runGroup, SWT.NONE);
		btnGenerate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		btnGenerate.setText("Generate tests ...");
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				doButtonClick(new GenerateTestRunnable());
			}
		});

		Button btnRun = new Button(runGroup, SWT.NONE);
		btnRun.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2,
				1));
		btnRun.setText("Run tests ...");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				doButtonClick(new RunTestRunnable());
			}
		});

		Group historyGroup = new Group(parent, SWT.NONE);
		historyGroup.setLayout(new GridLayout(1, false));
		historyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		historyGroup.setText("History");

		contentViewer = new ContentTreeViewer(historyGroup, SWT.BORDER,
				getSite().getPage());
		Tree tree = contentViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		contentViewer.setComparator(new TestrunComparator());

		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(contentViewer.getControl());
		contentViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, contentViewer);
		
		getSite().getPage().addSelectionListener(ID, this);
		getSite().setSelectionProvider(comboViewer);

		projectListener = new ProjectListener();
		suiteListener = new TestsuiteListener();
	}

	@Override
	public void dispose() {
		
		getSite().getPage().removeSelectionListener(ID, this);

		projectListener.removeListener();
		projectListener = null;

		suiteListener.removeListener();
		suiteListener = null;

		super.dispose();
	}

	protected void doButtonClick(AbstractTestRunnable runnable) {
		Shell parentShell = getSite().getShell();
		String suiteName = comboViewer.getCombo().getText();

		if (suiteName != null && !suiteName.equals("")) {
			runnable.setProjectContent(RttPluginUI.getProjectManager()
					.getCurrentContent());
			runnable.setSuiteName(suiteName);
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(
					parentShell);

			try {
				dialog.run(true, false, runnable);
				RttPluginUI.refreshManager();
			} catch (Exception e) {
				MessageDialog.openError(parentShell,
						runnable.getMessageTitle(), e.getMessage());
				RttLog.log(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e
						.getMessage(), e));
			}
		} else {
			MessageDialog.openInformation(parentShell,
					runnable.getMessageTitle(), "No test suite selected.");
		}
	}

	@Override
	public void setFocus() {
		contentViewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IStructuredSelection sSelection = (IStructuredSelection) selection;

		if (sSelection.getFirstElement() instanceof TestsuiteContent) {
			TestsuiteContent content = (TestsuiteContent) sSelection.getFirstElement();
			RttPluginUI.getSuiteManager().setCurrentContent(content);
		}
	}
}
