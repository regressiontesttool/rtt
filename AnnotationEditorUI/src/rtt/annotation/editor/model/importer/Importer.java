package rtt.annotation.editor.model.importer;

import java.io.IOException;

import org.eclipse.core.resources.IFile;

import rtt.annotation.editor.model.ClassModel;

public interface Importer {
	
	public ClassModel importModel(IFile inputFile) throws IOException;
}
