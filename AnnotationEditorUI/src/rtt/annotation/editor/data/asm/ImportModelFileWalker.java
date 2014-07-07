package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import rtt.annotation.editor.data.asm.visitor.ImportClassElementVisitor;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;

final class ImportModelFileWalker extends AbstractFileWalker {
	
	public ImportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		
		ClassElement element = factory.createClassElement(model);
		ClassVisitor visitor = new ImportClassElementVisitor(element, factory);
		
		reader.accept(visitor, ClassReader.SKIP_CODE);
		
		model.addClassElement(element);
	}
}