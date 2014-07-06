package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;

public abstract class AbstractFileWalker extends SimpleFileVisitor<Path> {
	
	protected ClassModelFactory factory;
	protected ClassModel model;
	
	public AbstractFileWalker(ClassModel model) {
		this.model = model;
		this.factory = ClassModelFactory.getFactory();
	}
	
	@Override
	public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		
		String fileName = file.getFileName().toString();
		
		if (attrs.isRegularFile() && fileName.endsWith(".class")) {
			processData(file);
		}

		return FileVisitResult.CONTINUE;
	}
	
	protected abstract void processData(Path file) throws IOException;

}
