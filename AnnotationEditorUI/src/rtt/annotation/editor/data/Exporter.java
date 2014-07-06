package rtt.annotation.editor.data;

import java.io.IOException;
import java.net.URI;

import rtt.annotation.editor.model.ClassModel;

public interface Exporter {
	
	public void exportModel(ClassModel model, URI output) throws IOException;

}
