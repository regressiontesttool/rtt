package rtt.ui.handlers.archive;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import rtt.core.classpath.RTTClasspathContainer;

/**
 * This utility class is designed to gather all
 * relevant methods which are needed to set up a 
 * JDT java project for testing with rtt.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class JavaProjectHelper {

	/**
	 * <p>Basically this method ads the RTT Runtime to
	 * the class path of the given JDT java project.
	 * @param project
	 * @return
	 * @throws JavaModelException
	 */
	public static IJavaProject handleProject(IProject project) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		entries = getClasspathEntries(entries);

		javaProject.setRawClasspath(entries,
				new NullProgressMonitor());
		
		return javaProject;
	}
	
	private static IClasspathEntry[] getClasspathEntries(IClasspathEntry[] presentEntries) {
		// check if any other entries are present
		if (presentEntries == null) {
			return new IClasspathEntry[] {
					JavaCore.newContainerEntry(RTTClasspathContainer.ID)
			};
		}
		
		// check if rtt class path already present
		for (IClasspathEntry entry : presentEntries) {
			if (entry.getPath().equals(RTTClasspathContainer.ID)) {
				return presentEntries;
			}
		}
		
		IClasspathEntry[] newEntries = new IClasspathEntry[presentEntries.length + 1];
		System.arraycopy(presentEntries, 0, newEntries, 0, presentEntries.length);
		newEntries[presentEntries.length] = JavaCore.newContainerEntry(RTTClasspathContainer.ID);
		
		return newEntries;
	}

}
