package rtt.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class RTTClasspathContainerInitializer extends
		ClasspathContainerInitializer {

	public RTTClasspathContainerInitializer() {	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		
		RTTClasspathContainer rttContainer = new RTTClasspathContainer(
				containerPath, project);

		JavaCore.setClasspathContainer(containerPath,
				new IJavaProject[] { project },
				new IClasspathContainer[] { rttContainer }, null);
	}

	@Override
	public boolean canUpdateClasspathContainer(IPath containerPath,
			IJavaProject project) {

		return true;
	}

	@Override
	public void requestClasspathContainerUpdate(IPath containerPath,
			IJavaProject project, IClasspathContainer containerSuggestion)
			throws CoreException {

		JavaCore.setClasspathContainer(containerPath,
				new IJavaProject[] { project },
				new IClasspathContainer[] { containerSuggestion }, null);
	}

}
