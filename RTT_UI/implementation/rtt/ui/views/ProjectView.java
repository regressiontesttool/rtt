package rtt.ui.views;

import java.util.Collection;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rtt.ui.content.IContent;
import rtt.ui.content.IContentObserver;
import rtt.ui.content.internal.ProjectContent;
import rtt.ui.content.viewer.ContentTreeViewer;
import rtt.ui.core.IProjectListener;
import rtt.ui.core.ProjectFinder;

public class ProjectView extends ViewPart implements ISelectionListener,
		IContentObserver, IProjectListener {

	public static final String ID = "rtt.ui.views.ProjectView";
	private ContentTreeViewer projectViewer;

	public ProjectView() {}

	@Override
	public void createPartControl(Composite parent) {

		getSite().getPage().addSelectionListener(this);

		MenuManager menuManager = new MenuManager();

		projectViewer = new ContentTreeViewer(parent, SWT.BORDER, this.getSite().getPage());

		getSite().setSelectionProvider(projectViewer);
		Menu menu = menuManager.createContextMenu(projectViewer.getControl());
		projectViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, projectViewer);

		setViewerData();
		
		ProjectFinder.addProjectListener(this);
	}
	
	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
		ProjectFinder.removeProjectListener(this);
		super.dispose();
	}

	private void setViewerData() {
		Collection<ProjectContent> projects = ProjectFinder.getProjectContents();
		for (ProjectContent project : projects) {
			project.addObserver(this);
		}
		
		projectViewer.setInput(projects.toArray());		
		projectViewer.expandToLevel(3);
	}

	@Override
	public void setFocus() {
		projectViewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this && selection instanceof IStructuredSelection) {
			IStructuredSelection sselection = (IStructuredSelection) selection;
			Object selectedObject = sselection.getFirstElement();

			if (selectedObject != null && (selectedObject instanceof IContent)) {
				ProjectContent selectedContent = ((IContent) selectedObject)
						.getContent(ProjectContent.class);
				
				if (selectedContent != null) {
					ProjectFinder.setCurrentProjectContent(selectedContent);
				}
			}
		}
	}

	@Override
	public String getObserverID() {
		return ID;
	}

	@Override
	public void update(IContent content) {
		projectViewer.refresh(true);
		projectViewer.expandToLevel(3);
	}

	@Override
	public void updateContent(ProjectContent currentProjectContent) {
		projectViewer.refresh(true);
		projectViewer.expandToLevel(3);
	}

}
