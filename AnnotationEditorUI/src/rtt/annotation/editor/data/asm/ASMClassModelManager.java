package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import rtt.annotation.editor.data.ModelWriter;
import rtt.annotation.editor.data.ModelReader;
import rtt.annotation.editor.data.NameResolver;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;

public class ASMClassModelManager implements ModelReader, ModelWriter {
	
	public static final NameResolver RESOLVER = NameResolver.create(".");
	
	@Override
	public ClassModel importModel(URI input) throws IOException {
		ClassModel model = ClassModelFactory.getFactory().createClassModel();
		walkFileTree(input, new ImportModelFileWalker(model));
		
		RESOLVER.resolveModel(model);
		
		return model;
	}

	@Override
	public void exportModel(ClassModel model, URI output) throws IOException {	
		walkFileTree(output, new ExportModelFileWalker(model));
	}
	
	private void walkFileTree(URI file, FileVisitor<? super Path> fileWalker) throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "false");
		
		URI zipUri = URI.create("jar:" + file);
		try (FileSystem zipFs = FileSystems.newFileSystem(zipUri, env)) {
			Files.walkFileTree(zipFs.getPath("/"), fileWalker);
		}
	}
}
