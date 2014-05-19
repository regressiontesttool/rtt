package rtt.ui.ecore.wizard.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import rtt.ui.utils.RttLog;

public class ProjectData extends Observable {
	
	List<IProject> availableProjects;
	IProject targetProject;
	
	List<IPackageFragment> availablePackages;
	IPackageFragment targetPackage;
	
	private ProjectData(List<IProject> availableProjects) {
		this.availableProjects = availableProjects;
		this.availablePackages = new ArrayList<IPackageFragment>();
	}
	
	public static ProjectData create(IProject[] projectData) {
		return new ProjectData(Arrays.asList(projectData));
	}

	public void setTargetProject(IProject newTargetProject) {
		try {
			updateAvailablePackages(newTargetProject);
		} catch (Exception e) {
			RttLog.log(e);
		}
		
		this.targetProject = newTargetProject;
		this.targetPackage = null;
		
		setChanged();		
		notifyObservers();
	}

	private void updateAvailablePackages(IProject newProject) throws Exception {
		availablePackages.clear();
		if (newProject.isAccessible() && newProject.hasNature(JavaCore.NATURE_ID)) {		
			IJavaProject javaProject = JavaCore.create(newProject);
			
			IPackageFragment[] fragments = javaProject.getPackageFragments();
			for (IPackageFragment packageFragment : fragments) {
				if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
					availablePackages.add(packageFragment);
				}
			}
		}
		
	}		
	
	public IProject getTargetProject() {
		return targetProject;
	}
	
	public IPackageFragment getTargetPackage() {
		return targetPackage;
	}
	
	public List<IProject> getAvailableProjects() {
		return availableProjects;
	}
	
	public List<IPackageFragment> getAvailablePackages() {
		return availablePackages;
	}
	
	public void setTargetPackage(IPackageFragment targetPackage) {
		this.targetPackage = targetPackage;
		
		setChanged();		
		notifyObservers();
	}

	public boolean hasTargetProject() {
		return targetProject != null;
	}
	
	public boolean hasTargetPackage() {
		return targetPackage != null;
	}
}
