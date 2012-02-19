package rtt.core.classpath;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

import de.schlichtherle.io.File;

public class RTTClasspathContainer implements IClasspathContainer {

	public final static IPath ID = new Path("RTT.ClasspathContainer");
	private List<IClasspathEntry> entries;

	public RTTClasspathContainer(IPath containerID, IJavaProject javaProject) {
		System.out.println("ContainerID: " + containerID);
		System.out.println("Project: " + javaProject);

		entries = new ArrayList<IClasspathEntry>();

		Bundle rttBundle = Platform.getBundle("rtt.core");
		if (rttBundle != null) {
			try {
				URL localUrl = FileLocator.toFileURL(rttBundle
						.getEntry("/lib/rtt-interface.jar"));
				String fullPath = new File(localUrl.getPath())
						.getAbsolutePath();
				IPath path = Path.fromOSString(fullPath);
				
				IAccessRule rule = JavaCore.newAccessRule(new Path("rtt/annotations/*"), IAccessRule.K_ACCESSIBLE);

				entries.add(JavaCore
						.newLibraryEntry(path, null, null,
								new IAccessRule[] { rule },
								new IClasspathAttribute[0], false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		return entries.toArray(new IClasspathEntry[entries.size()]);
	}

	@Override
	public String getDescription() {
		return "RTT Runtime";
	}

	@Override
	public int getKind() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IPath getPath() {
		return ID;
	}

}
