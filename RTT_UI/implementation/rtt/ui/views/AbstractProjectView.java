package rtt.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rtt.ui.content.ProjectContent;
import rtt.ui.core.IProjectListener;
import rtt.ui.core.ProjectFinder;

public abstract class AbstractProjectView extends ViewPart implements
		IProjectListener {

	@Override
	public final void updateContent(ProjectContent currentProjectContent) {
		loadContent(currentProjectContent);
	}

	@Override
	public final void createPartControl(Composite parent) {
		ProjectFinder.addProjectListener(this);
		createContentControl(parent);
		loadContent(ProjectFinder.getCurrentProjectContent());
	}

	protected abstract void createContentControl(Composite parent);
	protected abstract void loadContent(ProjectContent projectContent);
	
	@Override
	public void dispose() {
		ProjectFinder.removeProjectListener(this);
		super.dispose();
	}
}
