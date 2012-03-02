package rtt.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import rtt.core.archive.logging.ArchiveLog;
import rtt.core.archive.logging.Entry;
import rtt.core.manager.data.LogManager;
import rtt.ui.content.EmptyContent;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.ProjectContent;
import rtt.ui.content.SimpleTypedContent;
import rtt.ui.content.SimpleTypedContent.ContentType;
import rtt.ui.content.logging.DetailContent;
import rtt.ui.content.logging.LogEntryContent;
import rtt.ui.model.RttProject;
import rtt.ui.utils.ContentViewerFilter;
import rtt.ui.viewer.ContentTreeViewer;

public class LogView extends AbstractProjectView {

	public static final String ID = "rtt.ui.views.LogView";
	private TreeViewer contentViewer;
	private Combo combo;
	private int columnCount = 0;
	private LocalResourceManager resourceManager;

	

	public LogView() {
	}

	@Override
	public void createContentControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lblType = new Label(composite, SWT.NONE);
		lblType.setText("Type Selection:");

		combo = new Combo(composite, SWT.READ_ONLY);
		combo.setItems(new String[] { "Complete log ... ", "Information only",
				"Generation only", "Testrun only" });
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		combo.select(0);
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				contentViewer.setFilters(new ViewerFilter[] { new ContentViewerFilter(combo.getSelectionIndex()) });
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
		treeComposite.setLayout(treeColumnLayout);

		contentViewer = new TreeViewer(treeComposite, SWT.BORDER
				| SWT.FULL_SELECTION);
		Tree tree = contentViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);

		resourceManager = new LocalResourceManager(
				JFaceResources.getResources(), contentViewer.getControl());

		addColumn("Type", 20, 100, treeColumnLayout);
		addColumn("Message", 60, 100, treeColumnLayout);
		addColumn("Date", 20, 100, treeColumnLayout);
		
		contentViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof LogEntryContent && e2 instanceof LogEntryContent) {
					LogEntryContent entry1 = (LogEntryContent) e1;
					LogEntryContent entry2 = (LogEntryContent) e2;
					
					return -(entry1.getCalendar().compareTo(entry2.getCalendar()));					
				}
				
				if (e1 instanceof DetailContent && e2 instanceof DetailContent) {
					DetailContent detail1 = (DetailContent) e1;
					DetailContent detail2 = (DetailContent) e2;
					
					return detail1.getPriority().compareTo(detail2.getPriority());
				}
				
				return super.compare(viewer, e1, e2);
			}
		});

		contentViewer.setContentProvider(new ContentTreeViewer.TreeContentProvider());
		contentViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					if (selection.getFirstElement() instanceof IClickableContent) {
						((IClickableContent) selection.getFirstElement()).doDoubleClick(getSite().getPage());
					}
				}
			}
		});
	}

	@Override
	public void setFocus() {
		contentViewer.getControl().setFocus();
	}

	private void addColumn(String columnName, int weight, int minimumWidth,
			TreeColumnLayout treeColumnLayout) {
		TreeViewerColumn typeColumn = new TreeViewerColumn(contentViewer, SWT.NONE);
		typeColumn.setLabelProvider(new ColumnLabelProvider() {
			private final int column = columnCount;

			public Image getImage(Object element) {
				if (element instanceof IColumnableContent) {
					return ((IColumnableContent) element).getImage(column,
							resourceManager);
				}

				if (element instanceof IContent) {
					return ((IContent) element).getImage(resourceManager);
				}

				return null;
			}

			public String getText(Object element) {
				if (element instanceof IColumnableContent) {
					return ((IColumnableContent) element).getText(column);
				}

				if (element instanceof IContent) {
					return ((IContent) element).getText();
				}

				return element == null ? "" : element.toString();
			}
		});

		TreeColumn trclmnType = typeColumn.getColumn();
		trclmnType.setText(columnName);

		treeColumnLayout.setColumnData(typeColumn.getColumn(),
				new ColumnWeightData(weight, minimumWidth, true));

		columnCount++;
	}

	@Override
	protected void loadContent(ProjectContent currentContent) {
		List<IContent> childs = new ArrayList<IContent>();

		if (currentContent != null && currentContent.getProject() != null) {
			RttProject project = currentContent.getProject();

			LogManager logManager = project.getArchive().getLogManager();

			if (logManager != null) {
				ArchiveLog log = logManager.getData();

				if (log == null || log.getEntry().isEmpty()) {
					childs.add(new EmptyContent("No log entries found."));
				} else {
					for (Entry entry : log.getEntry()) {
						childs.add(new LogEntryContent(currentContent, entry));
					}
				}

			}
		} else {
			childs.add(new EmptyContent("No archive log found."));
		}

		contentViewer.setInput(new SimpleTypedContent(currentContent,
				ContentType.LOG_DIRECTORY, childs));
		
		contentViewer.expandAll();
	}

}
