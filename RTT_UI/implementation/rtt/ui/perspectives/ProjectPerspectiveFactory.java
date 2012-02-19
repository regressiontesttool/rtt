package rtt.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rtt.ui.views.LogView;
import rtt.ui.views.ProjectView;
import rtt.ui.views.TestView;
import rtt.ui.views.VersionView;

public class ProjectPerspectiveFactory implements IPerspectiveFactory {
	
	public static final String ID = "rtt.ui.perspective.projectPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
//		ProjectRegistry.loadData();
		
		String refId = layout.getEditorArea();	

		layout.addShowViewShortcut(ProjectView.ID);
		layout.addShowViewShortcut(LogView.ID);
		layout.addShowViewShortcut(VersionView.ID);
		layout.addShowViewShortcut(TestView.ID);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, refId);
		bottom.addView(LogView.ID);
		bottom.addView(VersionView.ID);
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.25f, refId);
		left.addView(ProjectView.ID);
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.65f, refId);
		right.addView(TestView.ID);
		right.addView("org.eclipse.pde.runtime.LogView");
		
				
	}

}
