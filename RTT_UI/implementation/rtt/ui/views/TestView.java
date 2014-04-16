package rtt.ui.views;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.ToolTip;
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

import rtt.core.RTTApplication;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.logging.TestrunContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.TestsuiteDirectory;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.launching.ApplicationRunnable;
import rtt.ui.utils.RttLog;
import rtt.ui.viewer.RttDoubleClickListener;
import rtt.ui.viewer.RttLabelProvider;
import rtt.ui.viewer.RttSimpleLabelProvider;
import rtt.ui.viewer.RttStructuredContentProvider;
import rtt.ui.viewer.RttTreeContentProvider;
import rtt.ui.views.utils.AbstractProjectListener;
import rtt.ui.views.utils.AbstractTestsuiteListener;
import rtt.ui.views.utils.SuiteFilter;

public class TestView extends ViewPart implements ISelectionListener {

	private class ProjectListener extends AbstractProjectListener {

		@Override
		public void refresh() {
			contentViewer.refresh(true);
			comboViewer.refresh(true);
		}

		@Override
		public void update(ProjectContent content) {
			boolean hasContent = false;
			
			if (content != null && content.getTestsuiteDirectory() != null) {
				TestsuiteDirectory suiteDirectory = content
						.getTestsuiteDirectory();
				
				hasContent = !suiteDirectory.isEmpty();
				
				if (hasContent) {
					comboViewer.setInput(suiteDirectory);
					contentViewer.setInput(content.getLogDirectory());
				} else {
					Object[] emptyArray = new Object[0];					
					comboViewer.setInput(emptyArray);
					contentViewer.setInput(emptyArray);
				}				
			} else {
				comboViewer.setInput(null);
				contentViewer.setInput(null);
			}
			
			comboViewer.getControl().setEnabled(hasContent);
			contentViewer.getControl().setEnabled(hasContent);
			generateButton.setEnabled(hasContent);
			runButton.setEnabled(hasContent);		
		}
	}

	private class TestsuiteListener extends AbstractTestsuiteListener {

		@Override
		public void refresh() {
			comboViewer.refresh(true);
			
			if (comboViewer.getSelection() != null) {
				IStructuredSelection ssel = (IStructuredSelection) comboViewer.getSelection();
				if (ssel != null && ssel.getFirstElement() != null) {
					TestsuiteContent content = (TestsuiteContent) ssel.getFirstElement();
					if (content != null) {
						generateButton.setEnabled(content.hasChildren());
						runButton.setEnabled(content.hasChildren());
					}
				}				
			}			
		}

		@Override
		public void update(TestsuiteContent content) {
			if (content != null) {
				
				generateButton.setEnabled(content.hasChildren());
				runButton.setEnabled(content.hasChildren());				
				
				comboViewer.setSelection(new StructuredSelection(content));
				contentViewer.setFilters(new ViewerFilter[] { new SuiteFilter(
						content.getText(), content.getProject().getActiveConfiguration()) });
			} else {
				generateButton.setEnabled(false);
				runButton.setEnabled(false);
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

	private TreeViewer contentViewer;
	private ComboViewer comboViewer;
	private Button generateButton;
	private Button runButton;

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

		Label suiteLabel = new Label(runGroup, SWT.NONE);
		suiteLabel.setText("Test suite:");

		comboViewer = new ComboViewer(runGroup, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		comboViewer.setLabelProvider(new RttSimpleLabelProvider());
		comboViewer.setContentProvider(new RttStructuredContentProvider());
		
		generateButton = new Button(runGroup, SWT.NONE);
		generateButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		generateButton.setText("Generate Reference Results ...");
		generateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				doButtonClick(new ApplicationRunnable(RTTApplication.GENERATE));
			}
		});

		runButton = new Button(runGroup, SWT.NONE);
		runButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2,
				1));
		runButton.setText("Run Tests ...");
		runButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				doButtonClick(new ApplicationRunnable(RTTApplication.RUN));
			}
		});

		Group historyGroup = new Group(parent, SWT.NONE);
		historyGroup.setLayout(new GridLayout(1, false));
		historyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		historyGroup.setText("History");		

		contentViewer = new TreeViewer(historyGroup, SWT.BORDER |
				SWT.SINGLE | SWT.H_SCROLL | 
				SWT.V_SCROLL | SWT.FULL_SELECTION);
		
		contentViewer.setContentProvider(new RttTreeContentProvider());
		contentViewer.setLabelProvider(new RttLabelProvider());
		ColumnViewerToolTipSupport.enableFor(contentViewer, ToolTip.NO_RECREATE);
		
		contentViewer.addDoubleClickListener(new RttDoubleClickListener(getSite().getPage()));
		
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

	protected boolean doButtonClick(ApplicationRunnable runnable) {
		Shell parentShell = getSite().getShell();
		String suiteName = comboViewer.getCombo().getText();

		if (suiteName == null || suiteName.equals("")) {
			MessageDialog.openInformation(parentShell,
					runnable.getTaskText(), "No test suite selected.");
			return false;
		}
		
		ProjectContent projectContent = RttPluginUI.getProjectManager().getCurrentContent();
		
		runnable.setProject(projectContent.getProject());
		runnable.setSuiteName(suiteName);
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(parentShell);
		
		try {
			dialog.run(true, true, runnable);
			
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				InvocationTargetException inno = (InvocationTargetException) e;
				e = inno.getCause();
			}			
			
			MessageDialog.openError(parentShell,
					runnable.getTaskText(), e.getMessage());
			RttLog.log(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e
					.getMessage(), e));
			return false;
		} finally {
			RttPluginUI.getProjectDirectory().reload(new ReloadInfo(Content.PROJECT));
		}
		
		return true;
	}

	@Override
	public void setFocus() {
		if (RttPluginUI.getProjectDirectory().hasChanged()) {
			RttPluginUI.refreshManager();
		}
		
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
