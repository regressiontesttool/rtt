package rtt.annotation.editor.data;

import java.nio.file.Path;

import rtt.annotation.editor.model.ClassModel;

public interface AnnotationExporter {
	
	public void exportAnnotations(ClassModel model, Path path);
}
