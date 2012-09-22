package rtt.ui.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

import rtt.ui.RttPluginUI;
import rtt.ui.content.logging.AbstractLogContent;
import rtt.ui.content.logging.LogDirectory;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.viewer.ContentDoubleClickListener;
import rtt.ui.viewer.RttColumnLabelProvider;
import rtt.ui.viewer.RttTreeContentProvider;
import rtt.ui.viewer.RttViewerFilter;
import rtt.ui.views.utils.IRttListener;

public class LogView extends ViewPart implements IRttListener<ProjectContent> {
	
	private static class ContentViewerComperator extends ViewerComparator {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof AbstractLogContent && e2 instanceof AbstractLogContent) {
				AbstractLogContent entry1 = (AbstractLogContent) e1;
				AbstractLogContent entry2 = (AbstractLogContent) e2;
				
				return entry1.compareTo(entry2);
			}
			
			return super.compare(viewer, e1, e2);
		}
	}

	public static final String ID = "rtt.ui.views.LogView";
	private TreeViewer contentViewer;
	private Combo combo;

	public LogView() {}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1, 1));

		Label typeLabel = new Label(composite, SWT.NONE);
		typeLabel.setText("Filter:");

		combo = new Combo(composite, SWT.READ_ONLY);
		combo.setItems(new String[] { "Complete log ... ", "Archive only",
				"Generation only", "Testrun only", "Information only" });
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				contentViewer.setFilters(new ViewerFilter[] { new RttViewerFilter(combo.getSelectionIndex()) });
				setFocus();
			}
		});

		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
		treeComposite.setLayout(treeColumnLayout);

		contentViewer = new TreeViewer(treeComposite, SWT.BORDER);
		Tree tree = contentViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);

		addColumn("Type", 20, 100, treeColumnLayout);
		addColumn("Message", 60, 100, treeColumnLayout);
		addColumn("Date", 20, 100, treeColumnLayout);
		
		contentViewer.setComparator(new ContentViewerComperator());
		contentViewer.setContentProvider(new RttTreeContentProvider());
		contentViewer.addDoubleClickListener(new ContentDoubleClickListener(getSite().getPage()));
		
		// register context menu for comments
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(contentViewer.getControl());
		contentViewer.getControl().setMenu(menu);		
		getSite().registerContextMenu(menuManager, contentViewer);
		getSite().setSelectionProvider(contentViewer);
		
		update(RttPluginUI.getProjectManager().getCurrentContent());
		RttPluginUI.getProjectManager().addListener(this);
	}

	@Override
	public void setFocus() {
		if (RttPluginUI.getProjectDirectory().hasChanged()) {
			RttPluginUI.refreshManager();
		}
		contentViewer.getControl().setFocus();
	}

	private void addColumn(String columnName, int weight, int minimumWidth,
			TreeColumnLayout treeColumnLayout) {
		
		TreeViewerColumn typeColumn = new TreeViewerColumn(contentViewer, SWT.NONE);
		typeColumn.setLabelProvider(new RttColumnLabelProvider());

		TreeColumn trclmnType = typeColumn.getColumn();
		trclmnType.setText(columnName);

		treeColumnLayout.setColumnData(typeColumn.getColumn(),
				new ColumnWeightData(weight, minimumWidth, true));
	}

	@Override
	public void refresh() {
		contentViewer.refresh(true);
	}
	
	@Override
	public void dispose() {
		RttPluginUI.getProjectManager().removeListener(this);
		super.dispose();
	}

	@Override
	public void update(ProjectContent projectContent) {
		boolean hasContent = false;
		LogDirectory logDirectory = null;
		
		if (projectContent != null) {
			logDirectory = projectContent.getLogDirectory();
			if (logDirectory != null) {
				hasContent = !logDirectory.isEmpty();
			}
		}		
		
		contentViewer.setInput(logDirectory);		
		combo.setEnabled(hasContent);
		contentViewer.getControl().setEnabled(hasContent);
		
	}

}
