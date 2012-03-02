package rtt.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import rtt.core.classpath.RTTClasspathContainer;
import rtt.ui.core.ProjectFinder;
import rtt.ui.core.RttNature;
import rtt.ui.perspectives.ProjectPerspectiveFactory;

public class InitArchiveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IJavaProject jProject = getSelectedObject(IJavaProject.class, event);
		if (jProject != null) {
			setRttNature(jProject.getProject());
			setClasspath(jProject);			
		} else {
			System.out.println("Could not find Project.");
			return null;
		}

		// CHRISTIAN reload projects !!!
		ProjectFinder.loadProjects();
		ProjectFinder.fireChangeEvent();

		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			workbench.showPerspective(ProjectPerspectiveFactory.ID,
					workbench.getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			throw new ExecutionException("Could not open perspective", e);
		}
		
		return null;
	}

	private boolean setClasspath(IJavaProject jProject) {
		try {
			// Set classpath for RTT runtime engine
			List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
			for (IClasspathEntry entry : jProject.getRawClasspath()) {
				if (entry.getPath().equals(RTTClasspathContainer.ID)) {
					return false;
				}
				
				entries.add(entry);
			}
			
			entries.add(JavaCore.newContainerEntry(RTTClasspathContainer.ID));

			jProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
			return true;
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private boolean setRttNature(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();

			for (int i = 0; i < natures.length; ++i) {
				if (RttNature.NATURE_ID.equals(natures[i])) {
					// Remove the nature
//					String[] newNatures = new String[natures.length - 1];
//					System.arraycopy(natures, 0, newNatures, 0, i);
//					System.arraycopy(natures, i + 1, newNatures, i,
//							natures.length - i - 1);
//					description.setNatureIds(newNatures);
//					project.setDescription(description, null);
					
					return false;
				}
			}

			// Add the nature
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = RttNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
			
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
