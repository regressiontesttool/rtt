package rtt.ui.views;

import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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
import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IContent;
import rtt.ui.content.logging.TestrunContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.utils.AbstractTestRunnable;
import rtt.ui.utils.GenerateTestRunnable;
import rtt.ui.utils.RunTestRunnable;
import rtt.ui.viewer.ContentTreeViewer;
import rtt.ui.views.utils.SuiteFilter;

public class TestView extends ViewPart {
	
	private class TestViewProjectListener implements IRttListener<ProjectContent> {
		
		public TestViewProjectListener() {
			RttPluginUI.getProjectManager().addListener(this);
		}
		
		protected void removeListener() {
			RttPluginUI.getProjectManager().removeListener(this);
		}
		
		@Override
		public void refresh() {
			contentViewer.refresh(true);
		}

		@Override
		public void update(ProjectContent content) {
			if (content != null) {
				contentViewer.setInput(content.getLogDirectory());
				List<IContent> contentList = content.getTestsuiteContents();

				if (contentList.size() > 0) {
					int i = 0;
					String suiteNames[] = new String[contentList.size()];

					for (IContent suiteContent : contentList) {
						suiteNames[i] = suiteContent.getText();
						i++;
					}

					suiteCombo.setItems(suiteNames);
					suiteCombo.select(0);
					suiteCombo.setEnabled(true);

					contentViewer.setFilters(new ViewerFilter[] { new SuiteFilter(
							suiteNames[0]) });
				} else {
					suiteCombo.setItems(new String[0]);
					suiteCombo.setEnabled(false);
				}
			}
		}
		
	}
	
	private class TestViewTestsuiteListener implements IRttListener<TestsuiteContent> {
		
		public TestViewTestsuiteListener() {
			RttPluginUI.getSuiteManager().addListener(this);
		}
		
		protected void removeListener() {
			RttPluginUI.getSuiteManager().removeListener(this);
		}

		@Override
		public void refresh() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(TestsuiteContent content) {
			if (content != null) {
				System.out.println("Update: " + content.getText());
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

	public final static String ID = "rtt.ui.views.TestView";

	public TestView() {
	}

	private ContentTreeViewer contentViewer;
	private Combo suiteCombo;
	
	private TestViewProjectListener projectListener;
	private TestViewTestsuiteListener suiteListener;

	// protected void loadContent(final ProjectContent currentContent) {
	// if (currentContent != null && currentContent.getProject() != null) {
	// RttProject project = currentContent.getProject();
	//
	// LogManager logManager = project.getArchive().getLogManager();
	//
	// if (logManager != null) {
	// ArchiveLog log = logManager.getData();
	//
	// for (Entry entry : log.getEntry()) {
	// if (entry.getType() == EntryType.TESTRUN) {
	// childs.add(new TestrunContent(currentContent, entry));
	// }
	// }
	//
	// if (childs.isEmpty()) {
	// childs.add(new EmptyContent("No log entries found."));
	// }
	// }
	// } else {
	// childs.add(new EmptyContent("No archive log found."));
	// }

	// int lastSelection = suiteComboViewer.getCombo().getSelectionIndex();
	//
	// suiteComboViewer.setInput(currentContent.getTestsuiteContents()
	// .toArray());
	//
	// if (lastSelection > -1) {
	// suiteComboViewer.getCombo().select(lastSelection);
	// } else {
	// suiteComboViewer.getCombo().select(0);
	// }
	// }

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

		suiteCombo = new Combo(runGroup, SWT.READ_ONLY);
		suiteCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		suiteCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(suiteCombo.getText());

				contentViewer.setFilters(new ViewerFilter[] { new SuiteFilter(
						suiteCombo.getText()) });
				contentViewer.getControl().setFocus();
			}
		});

		// suiteComboViewer = new TestsuiteComboViewer(runGroup, SWT.READ_ONLY);
		// final Combo suiteCombo = suiteComboViewer.getCombo();
		// suiteCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		// false, 1, 1));
		// suiteCombo.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// contentViewer.setFilters(new ViewerFilter[] { new
		// SuiteFilter(suiteCombo.) });
		// contentViewer.getControl().setFocus();
		// }
		// });
		//
		// suiteComboViewer.addSe
		// .addSelectionChangedListener(new ISelectionChangedListener() {
		//
		// @Override
		// public void selectionChanged(SelectionChangedEvent event) {
		// ProjectContent currentContent =
		// ProjectFinder.getCurrentProjectContent();
		// if (currentContent.getProject() != null
		// && currentContent.getProject()
		// .getActiveConfiguration() != null) {
		//
		// String configName = currentContent.getProject()
		// .getActiveConfiguration().getName();
		//
		// String suiteName = suiteComboViewer
		// .getSelectedTestsuite();
		//
		// System.out.println("Filter runs: config="
		// + configName + ", suite=" + suiteName);
		//
		// contentViewer
		// .setFilters(new ViewerFilter[] { new SuiteFilter(
		// suiteName, configName) });
		// }
		// }
		// });

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
		getSite().setSelectionProvider(contentViewer);
		Menu menu = menuManager.createContextMenu(contentViewer.getControl());
		contentViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, contentViewer);

		projectListener = new TestViewProjectListener();
		projectListener.update(RttPluginUI.getProjectManager().getCurrentContent());
		
		suiteListener = new TestViewTestsuiteListener();
		suiteListener.update(RttPluginUI.getSuiteManager().getCurrentContent());
	}

	@Override
	public void dispose() {
		projectListener.removeListener();
		projectListener = null;
		
		suiteListener.removeListener();
		suiteListener = null;
		
		super.dispose();
	}

	protected void doButtonClick(AbstractTestRunnable runnable) {
		Shell parentShell = getSite().getShell();
		String suiteName = suiteCombo.getText();

		if (suiteName != null && !suiteName.equals("")) {
			runnable.setProjectContent(RttPluginUI.getProjectManager().getCurrentContent());
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
}

