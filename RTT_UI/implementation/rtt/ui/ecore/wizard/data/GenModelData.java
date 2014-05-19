package rtt.ui.ecore.wizard.data;

import java.util.Observable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class GenModelData extends Observable {
	
	IFile modelFile = null;
	
	public static boolean checkModelFile(IFile file) {
		if (file != null) {
			String extension = file.getFileExtension();
			return extension.equalsIgnoreCase("genmodel");
		}
		
		return false;
	}
	
	public void setModelFile(IFile modelFile) {
		this.modelFile = modelFile;
		setChanged();
		
		notifyObservers(modelFile);
	}
	
	public IFile getModelFile() {
		return modelFile;
	}
	
	public IProject getModelProject() {
		if (modelFile != null) {
			return modelFile.getProject();
		}
		
		return null;
	}

}
