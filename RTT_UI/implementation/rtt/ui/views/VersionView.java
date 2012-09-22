package rtt.ui.views;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Configuration;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.history.InputManager;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IContent;
import rtt.ui.content.history.HistoryContent;
import rtt.ui.content.history.HistoryContent.VersionType;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.TestsuiteDirectory;
import rtt.ui.content.testsuite.TestcaseContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.viewer.ContentDoubleClickListener;
import rtt.ui.viewer.RttColumnLabelProvider;
import rtt.ui.viewer.RttSimpleLabelProvider;
import rtt.ui.viewer.RttStructuredContentProvider;
import rtt.ui.viewer.RttTreeContentProvider;
import rtt.ui.views.utils.AbstractProjectListener;
import rtt.ui.views.utils.AbstractTestsuiteListener;

public class VersionView extends ViewPart implements ISelectionListener {
	
	protected static final Object[] EMPTY_ARRAY = new Object[0];
	
	private class ProjectListener extends AbstractProjectListener {
		
		@Override
		public void refresh() {
			suiteComboViewer.refresh();
//			treeViewer.refresh(true);
			
			super.refresh();
		}

		@Override
		public void update(ProjectContent content) {
			boolean hasContent = false;
			TestsuiteDirectory suiteDirectory = null;
			
			if (content != null) {
				suiteDirectory = content.getTestsuiteDirectory();
				if (suiteDirectory != null) {
					hasContent = !suiteDirectory.isEmpty();
				}
			}
			
			if (hasContent) {
				suiteComboViewer.setInput(suiteDirectory);
			} else {
				suiteComboViewer.setInput(EMPTY_ARRAY);
				caseComboViewer.setInput(EMPTY_ARRAY);
			}
			
			suiteComboViewer.getControl().setEnabled(hasContent);
			caseComboViewer.getControl().setEnabled(hasContent);
			
			treeViewer.setInput(EMPTY_ARRAY);
			treeViewer.getControl().setEnabled(false);
		}
	}
	
	private class TestsuiteListener extends AbstractTestsuiteListener {
		
		private void setFirstTestcase(TestsuiteContent content) {
			IContent[] childs = content.getChildren();
			
			boolean hasCases = false;
			
			if (childs != null && childs.length > 0) {
				caseComboViewer.setSelection(new StructuredSelection(childs[0]));
				hasCases = true;
			}
			
			caseComboViewer.getControl().setEnabled(hasCases);
			historyLoadButton.setEnabled(hasCases);
			treeViewer.setInput(EMPTY_ARRAY);
			treeViewer.getControl().setEnabled(false);
		}
		
		@Override
		public void refresh() {
			suiteComboViewer.refresh(true);
			caseComboViewer.refresh(true);
			
			Object input = caseComboViewer.getInput();
			if (input != null && input instanceof TestsuiteContent) {
				TestsuiteContent content = (TestsuiteContent) input;
				setFirstTestcase(content);
			}
		}
		
		@Override
		public void update(TestsuiteContent content) {
			if (content != null) {
				suiteComboViewer.setSelection(new StructuredSelection(content));
				caseComboViewer.setInput(content);
				
				setFirstTestcase(content);
			}
		}
	}

	public static final String ID = "rtt.ui.views.VersionView";
	
	public VersionView() { }
	
	private ProjectListener projectListener;
	private TestsuiteListener suiteListener;

	private TreeViewer treeViewer;
	private ComboViewer suiteComboViewer;
	private ComboViewer caseComboViewer;
	private Button historyLoadButton;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label suiteLabel = new Label(composite, SWT.NONE);
		suiteLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		suiteLabel.setText("Test suite:");

		suiteComboViewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo combo = suiteComboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		suiteComboViewer.setLabelProvider(new RttSimpleLabelProvider());
		suiteComboViewer.setContentProvider(new RttStructuredContentProvider());
		
		historyLoadButton = new Button(composite, SWT.PUSH);
		historyLoadButton.setText("Load History ...");
		GridData gd_historyLoadButton = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 2);
		gd_historyLoadButton.widthHint = 200;
		gd_historyLoadButton.minimumWidth = 200;
		historyLoadButton.setLayoutData(gd_historyLoadButton);
		historyLoadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection sel = caseComboViewer.getSelection();
				if (sel instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection)sel).getFirstElement();
					if (item instanceof TestcaseContent) {
						loadTestcase((TestcaseContent) item);
					}
				}
			}
		});
		
		Label caseLabel = new Label(composite, SWT.NONE);
		caseLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		caseLabel.setText("Test case:");
		
		caseComboViewer = new ComboViewer(composite, SWT.READ_ONLY);
		combo = caseComboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		caseComboViewer.setLabelProvider(new RttSimpleLabelProvider());
		caseComboViewer.setContentProvider(new RttStructuredContentProvider());
		
		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TreeColumnLayout treeLayout = new TreeColumnLayout();
		treeComposite.setLayout(treeLayout);
		
		treeViewer = new TreeViewer(treeComposite, 
				SWT.BORDER | SWT.H_SCROLL | 
				SWT.V_SCROLL | SWT.SINGLE |
				SWT.FULL_SELECTION);
		treeViewer.setContentProvider(new RttTreeContentProvider());
		treeViewer.addDoubleClickListener(new ContentDoubleClickListener(getSite().getPage()));
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setEnabled(false);
		
		TreeViewerColumn nameViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		nameViewerColumn.getColumn().setText("History");
		nameViewerColumn.setLabelProvider(new RttColumnLabelProvider());
		treeLayout.setColumnData(nameViewerColumn.getColumn(), new ColumnWeightData(30, 250, true));
		
		TreeViewerColumn inputBaseViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		inputBaseViewerColumn.getColumn().setText("Input Data Base");
		inputBaseViewerColumn.setLabelProvider(new RttColumnLabelProvider());
		treeLayout.setColumnData(inputBaseViewerColumn.getColumn(), new ColumnWeightData(30, 250, true));
		
		TreeViewerColumn dateViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		dateViewerColumn.getColumn().setText("Creation Date");
		dateViewerColumn.setLabelProvider(new RttColumnLabelProvider());
		treeLayout.setColumnData(dateViewerColumn.getColumn(), new ColumnWeightData(30, 250, true));
		
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);
		
//		Tree tree = treeViewer.getTree();
//		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		getSite().setSelectionProvider(suiteComboViewer);
		getSite().getPage().addSelectionListener(ID, this);
		
		projectListener = new ProjectListener();
		suiteListener = new TestsuiteListener();
	}
	
	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(ID, this);
		
		projectListener.removeListener();
		projectListener = null;
		
		suiteListener.removeListener();
		projectListener = null;

		super.dispose();
	}
	
	protected void loadTestcase(TestcaseContent item) {
		if (item != null) {
			final String caseName = item.getCaseName();
			final String suiteName = item.getSuiteName();
			
			final ProjectContent currentProject = RttPluginUI.getProjectManager().getCurrentContent();
			
			Archive archive = currentProject.getProject().getArchive();
			final Configuration activeConfig = currentProject.getProject().getActiveConfiguration();
			final List<IContent> childs = new ArrayList<IContent>();
			
			if (archive != null) {
				
				final ArchiveLoader loader = archive.getLoader();				
				
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());
				try {
					dialog.run(true, false, new IRunnableWithProgress() {
						
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							monitor.beginTask("Loading history ...", IProgressMonitor.UNKNOWN);
							
							InputManager inputManager = new InputManager(loader, suiteName, caseName);
							childs.add(new HistoryContent(currentProject, inputManager, VersionType.INPUT));
							
							OutputDataManager refManager = new OutputDataManager(loader, suiteName, caseName, activeConfig, OutputDataType.REFERENCE);
							childs.add(new HistoryContent(currentProject, refManager, VersionType.REFERENCE));
							
							OutputDataManager testManager = new OutputDataManager(loader, suiteName, caseName, activeConfig, OutputDataType.TEST);
							childs.add(new HistoryContent(currentProject, testManager, VersionType.TEST));
							
							monitor.done();
						}
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}		
			
			treeViewer.setInput(childs.toArray());
			treeViewer.getTree().setEnabled(true);
		} else {
			treeViewer.setInput(EMPTY_ARRAY);
			treeViewer.getTree().setEnabled(false);
		}
	}

	@Override
	public void setFocus() {
		if (RttPluginUI.getProjectDirectory().hasChanged()) {
			RttPluginUI.refreshManager();
		}
		
		treeViewer.getControl().setFocus();
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
