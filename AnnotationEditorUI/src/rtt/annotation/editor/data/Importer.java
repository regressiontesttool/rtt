package rtt.annotation.editor.data;

import java.io.IOException;
import java.net.URI;

import rtt.annotation.editor.model.ClassModel;

public interface Importer {
	
	public ClassModel importModel(URI input) throws IOException;

//	public void importClass(InputStream in, ClassModel model) throws IOException;
}
