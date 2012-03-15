package rtt.ui.views;

import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rtt.core.archive.Archive;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.TestsuiteDirectory;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.viewer.BaseContentLabelProvider;
import rtt.ui.viewer.BaseContentProvider;
import rtt.ui.viewer.ContentTreeViewer;
import rtt.ui.views.utils.AbstractProjectListener;
import rtt.ui.views.utils.AbstractTestsuiteListener;

public class VersionView extends ViewPart implements ISelectionListener {
	
	private class ProjectListener extends AbstractProjectListener {
		
		@Override
		public void refresh() {
			comboViewer.refresh();
//			treeViewer.refresh(true);
			
			super.refresh();
		}

		@Override
		public void update(ProjectContent content) {
			if (content != null) {
				TestsuiteDirectory suiteDirectory = content.getTestsuiteDirectory();
				comboViewer.setInput(suiteDirectory);
				comboViewer.getControl().setEnabled(!suiteDirectory.isEmpty());
				
				if (suiteDirectory.isEmpty() == false) {
//					comboViewer.setSelection(new StructuredSelection(
//							suiteDirectory.getTestsuite(0)));
					
//					RttPluginUI.getSuiteManager().setCurrentContent(suiteDirectory.getTestsuite(0));
				}
			}
		}
	}
	
	private class TestsuiteListener extends AbstractTestsuiteListener {
		
		@Override
		public void update(TestsuiteContent content) {
			if (content != null) {
				comboViewer.setSelection(new StructuredSelection(content));
			}
		}
	}

	public static final String ID = "rtt.ui.views.VersionView";
	
	public VersionView() { }
	
	private ProjectListener projectListener;
	private TestsuiteListener suiteListener;

	private ContentTreeViewer treeViewer;
	private ComboViewer comboViewer;
	private Combo caseCombo;
	
	private String[] suiteNames;
	private String[] caseNames;
	
	protected final String[] EMPTY_ARRAY = new String[0];

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label suiteLabel = new Label(composite, SWT.NONE);
		suiteLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		suiteLabel.setText("Testsuite:");

		comboViewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		comboViewer.setLabelProvider(new BaseContentLabelProvider());
		comboViewer.setContentProvider(new BaseContentProvider());
		
		Label caseLabel = new Label(composite, SWT.NONE);
		caseLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		caseLabel.setText("Testcase:");

		caseCombo = new Combo(composite, SWT.READ_ONLY);
		caseCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		caseCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadTestcase(caseCombo.getSelectionIndex());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		caseCombo.setItems(new String[] {"Select a project ..."});
//		caseCombo.select(0);
//		caseCombo.setEnabled(false);

		treeViewer = new ContentTreeViewer(parent, SWT.BORDER, getSite().getPage());
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setEnabled(false);
		
		getSite().setSelectionProvider(comboViewer);
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
	
	protected void loadContent(ProjectContent currentProject) {
		
	}

	protected void loadTestsuite(int selectionIndex) {
		if (selectionIndex > 0) {
			ProjectContent currentProject = RttPluginUI.getProjectManager().getCurrentContent();
			String suiteName = suiteNames[selectionIndex];
			
			if (suiteName != null && suiteName.equals("") == false) {
				Archive a = currentProject.getProject().getArchive();
				Testsuite suite = a.getTestsuite(suiteName, false);
				
				if (suite != null && suite.getTestcase() != null) {
					List<Testcase> cases = suite.getTestcase();
					caseNames = new String[cases.size() + 1];
					caseNames[0] = "Select ...";
					
					for (int i = 1; i < cases.size() + 1; i++) {
						caseNames[i] = cases.get(i - 1).getName();
					}
					
					caseCombo.setItems(caseNames);
					caseCombo.select(0);
					caseCombo.setEnabled(true);					
				}
			}
		} else {
			caseCombo.setItems(new String[0]);
			caseCombo.setEnabled(false);
		}
		
		treeViewer.setInput(EMPTY_ARRAY);
		treeViewer.getTree().setEnabled(false);		
	}
	
	protected void loadTestcase(int selectionIndex) {
		if (selectionIndex > 0) {
//			final String caseName = caseNames[selectionIndex];
//			final String suiteName = suiteNames[suiteCombo.getSelectionIndex()];
//			
//			final ProjectContent currentProject = RttPluginUI.getProjectManager().getCurrentContent();
//			
//			Archive archive = currentProject.getProject().getArchive();
//			final Configuration activeConfig = currentProject.getProject().getActiveConfiguration();
//			final List<IContent> childs = new ArrayList<IContent>();
//			
//			System.out.println("Archive: " + archive);
//			System.out.println("Config:"  +  activeConfig);
//			
//			if (archive != null) {
//				
//				final ArchiveLoader loader = archive.getLoader();
//				
//				
//				ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());
//				try {
//					dialog.run(true, false, new IRunnableWithProgress() {
//						
//						@Override
//						public void run(IProgressMonitor monitor) throws InvocationTargetException,
//								InterruptedException {
//							monitor.beginTask("Loading history ...", IProgressMonitor.UNKNOWN);
//							
//							InputManager inputManager = new InputManager(loader, suiteName, caseName);
//							childs.add(new HistoryContent(currentProject, inputManager, VersionType.INPUT));
//							
//							ReferenceManager refManager = new ReferenceManager(loader, suiteName, caseName, activeConfig);
//							childs.add(new HistoryContent(currentProject, refManager, VersionType.REFERENCE));
//							
//							TestManager testManager = new TestManager(loader, suiteName, caseName, activeConfig);
//							childs.add(new HistoryContent(currentProject, testManager, VersionType.TEST));
//							
//							monitor.done();
//						}
//					});
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}				
//			}		
//			
//			treeViewer.setInput(childs.toArray());
//			treeViewer.getTree().setEnabled(true);
//		} else {
//			treeViewer.setInput(EMPTY_ARRAY);
//			treeViewer.getTree().setEnabled(false);
		}
	}

	@Override
	public void setFocus() {
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
