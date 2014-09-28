package rtt.annotation.editor.data;

import java.nio.file.Path;

import rtt.annotation.editor.model.ClassModel;

public interface AnnotationImporter {
	
	public void importAnnotations(ClassModel model, Path path);

}
