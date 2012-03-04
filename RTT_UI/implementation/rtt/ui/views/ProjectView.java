package rtt.ui.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rtt.ui.IRttListener;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.ProjectDirectoryContent;
import rtt.ui.viewer.ContentTreeViewer;

public class ProjectView extends ViewPart implements ISelectionListener, IRttListener {

	public static final String ID = "rtt.ui.views.ProjectView";
	private ContentTreeViewer projectViewer;

	public ProjectView() {}

	@Override
	public void createPartControl(Composite parent) {
		
		projectViewer = new ContentTreeViewer(parent, SWT.BORDER, this.getSite().getPage());
		
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(projectViewer.getControl());
		projectViewer.getControl().setMenu(menu);
		
		getSite().registerContextMenu(menuManager, projectViewer);
		getSite().setSelectionProvider(projectViewer);
		getSite().getPage().addSelectionListener(this);
		
		RttPluginUI.addListener(this);		
		setViewerData(RttPluginUI.getProjectDirectory());
	}
	
	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
		RttPluginUI.removeListener(this);
		super.dispose();
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
					RttPluginUI.setCurrentProjectContent(selectedContent);
				}
			}
		}
	}

	private void setViewerData(ProjectDirectoryContent content) {		
		projectViewer.setInput(content);		
		projectViewer.expandToLevel(3);
	}

	@Override
	public void refresh() {
		projectViewer.refresh(true);
	}
	
	@Override
	public void update(ProjectContent projectContent) {
//		projectViewer.refresh(true);
	}
	

//	@Override
//	public String getObserverID() {
//		return ID;
//	}

//	@Override
//	public void update(IContent content) {
//		projectViewer.refresh(true);
//		projectViewer.expandToLevel(3);
//	}
//
//	@Override
//	public void updateContent(ProjectContent currentProjectContent) {
//		projectViewer.refresh(true);
//		projectViewer.expandToLevel(3);
//	}

}
