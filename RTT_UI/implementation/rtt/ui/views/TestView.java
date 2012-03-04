package rtt.ui.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.ui.part.ViewPart;

import rtt.ui.IRttListener;
import rtt.ui.RttPluginUI;
import rtt.ui.content.logging.FailureContent;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.content.logging.TestrunContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.utils.AbstractTestRunnable;
import rtt.ui.utils.GenerateTestRunnable;
import rtt.ui.utils.RunTestRunnable;
import rtt.ui.viewer.ContentTreeViewer;
import rtt.ui.viewer.TestsuiteComboViewer;

public class TestView extends ViewPart implements IRttListener {
	
	private static class TestrunComparator extends ViewerComparator {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof TestrunContent
					&& e2 instanceof TestrunContent) {
				TestrunContent testrun1 = (TestrunContent) e1;
				TestrunContent testrun2 = (TestrunContent) e2;

				return -(testrun1.getCalendar().compareTo(testrun2
						.getCalendar()));
			}

			return super.compare(viewer, e1, e2);
		}
	}
	
	private static class TestrunFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			
			if (element instanceof TestrunContent) {
				return true;
			}
			
			if (element instanceof TestResultContent) {
				return true;
			}
			
			if (element instanceof FailureContent) {
				return true;
			}
			
			return false;
		}		
	}
	
	

	protected class SuiteFilter extends ViewerFilter {

		private String suiteName;
		private String configName;

		public SuiteFilter(String suiteName, String configName) {
			this.suiteName = suiteName;
			this.configName = configName;
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
				if (testrunContent.getTestsuite() == null
						|| testrunContent.getConfiguration() == null) {
					return true;
				}

				if (!testrunContent.getTestsuite().equals(suiteName)) {
					return false;
				}

				if (!testrunContent.getConfiguration().equals(configName)) {
					return false;
				}

				return true;
			}

			return false;
		}
	}

	public final static String ID = "rtt.ui.views.TestView";
	private TestsuiteComboViewer suiteComboViewer;

	public TestView() {	}

	private ContentTreeViewer contentViewer;

//	protected void loadContent(final ProjectContent currentContent) {	
//		if (currentContent != null && currentContent.getProject() != null) {
//			RttProject project = currentContent.getProject();
//
//			LogManager logManager = project.getArchive().getLogManager();
//
//			if (logManager != null) {
//				ArchiveLog log = logManager.getData();
//
//				for (Entry entry : log.getEntry()) {
//					if (entry.getType() == EntryType.TESTRUN) {
//						childs.add(new TestrunContent(currentContent, entry));
//					}
//				}
//
//				if (childs.isEmpty()) {
//					childs.add(new EmptyContent("No log entries found."));
//				}
//			}
//		} else {
//			childs.add(new EmptyContent("No archive log found."));
//		}

		
		
		
//		int lastSelection = suiteComboViewer.getCombo().getSelectionIndex();
//
//		suiteComboViewer.setInput(currentContent.getTestsuiteContents()
//				.toArray());
//		
//		if (lastSelection > -1) {
//			suiteComboViewer.getCombo().select(lastSelection);
//		} else {
//			suiteComboViewer.getCombo().select(0);
//		}
//	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Group runGroup = new Group(parent, SWT.NONE);
		runGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		runGroup.setText("Testing");
		runGroup.setLayout(new GridLayout(2, false));

		Label descriptionLabel = new Label(runGroup, SWT.WRAP);
		descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 2, 1));
		descriptionLabel
				.setText("Select a test suite from the current project. Click \"Generate tests\" to generate new reference data or click \"Run tests\" to start a new test run.");

		Label suiteLabel = new Label(runGroup, SWT.NONE);
		suiteLabel.setText("Testsuite:");

		Combo suiteCombo = new Combo(runGroup, SWT.READ_ONLY);
		suiteCombo.setItems(new String[] { "eins", "zwei" });
		suiteCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		suiteCombo.select(0);
		suiteCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				contentViewer.setFilters(new ViewerFilter[] {
//						
//				});
				contentViewer.getControl().setFocus();
			}
		});
		
//		suiteComboViewer = new TestsuiteComboViewer(runGroup, SWT.READ_ONLY);
//		final Combo suiteCombo = suiteComboViewer.getCombo();
//		suiteCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
//				false, 1, 1));
//		suiteCombo.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				contentViewer.setFilters(new ViewerFilter[] { new SuiteFilter(suiteCombo.) });
//				contentViewer.getControl().setFocus();
//			}
//		});
//
//		suiteComboViewer.addSe
//				.addSelectionChangedListener(new ISelectionChangedListener() {
//
//					@Override
//					public void selectionChanged(SelectionChangedEvent event) {
//						ProjectContent currentContent = ProjectFinder.getCurrentProjectContent();
//						if (currentContent.getProject() != null
//								&& currentContent.getProject()
//										.getActiveConfiguration() != null) {
//
//							String configName = currentContent.getProject()
//									.getActiveConfiguration().getName();
//
//							String suiteName = suiteComboViewer
//									.getSelectedTestsuite();
//
//							System.out.println("Filter runs: config="
//									+ configName + ", suite=" + suiteName);
//
//							contentViewer
//									.setFilters(new ViewerFilter[] { new SuiteFilter(
//											suiteName, configName) });
//						}
//					}
//				});

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
		contentViewer.setFilters(new ViewerFilter[] { new TestrunFilter() });

		MenuManager menuManager = new MenuManager();
		getSite().setSelectionProvider(contentViewer);
		Menu menu = menuManager.createContextMenu(contentViewer.getControl());
		contentViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, contentViewer);
		
		update(RttPluginUI.getCurrentProjectContent());
		RttPluginUI.addListener(this);
	}
	
	@Override
	public void dispose() {
		RttPluginUI.removeListener(this);
		super.dispose();
	}

	protected void doButtonClick(AbstractTestRunnable runnable) {
		Shell parentShell = getSite().getShell();
		if (suiteComboViewer.isTestsuiteSelected()) {
//			runnable.setProjectContent(ProjectFinder.getCurrentProjectContent());
//			runnable.setSuiteName(suiteComboViewer.getSelectedTestsuite());
//
//			ProgressMonitorDialog dialog = new ProgressMonitorDialog(
//					parentShell);
//
//			try {
//				dialog.run(true, false, runnable);
//				ProjectFinder.fireChangeEvent();
//			} catch (Exception e) {
//				MessageDialog.openError(parentShell,
//						runnable.getMessageTitle(), e.getMessage());
//				RttLog.log(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e
//						.getMessage(), e));
//			}
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
	public void refresh() {
		contentViewer.refresh(true);
	}

	@Override
	public void update(ProjectContent projectContent) {
//		if (projectContent != null) {
//			List<IContent> childs = new ArrayList<IContent>();
//			for (IContent content : projectContent.getLogDirectory().getChildren()) {
//				System.out.println("Content: " + content.getClass());
//				if (content instanceof TestrunContent) {
//					childs.add(content);
//				}
//			}
//			
//			contentViewer.setInput(new SimpleTypedContent(projectContent,
//					ContentType.LOG_DIRECTORY, childs));
//		}
		
		contentViewer.setInput(projectContent.getLogDirectory());
	}
}
