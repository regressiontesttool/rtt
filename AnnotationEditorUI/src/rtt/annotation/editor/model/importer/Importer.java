package rtt.annotation.editor.model.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import rtt.annotation.editor.model.ClassModel;

public interface Importer {
	
	public ClassModel importModel(File inputFile) throws IOException;

	public void importClass(InputStream in, ClassModel model) throws IOException;
}
