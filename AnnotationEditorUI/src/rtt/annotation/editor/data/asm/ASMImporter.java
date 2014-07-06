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

import rtt.annotation.editor.data.Exporter;
import rtt.annotation.editor.data.Importer;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;

public class ASMImporter implements Importer, Exporter {
	
	private ClassModelFactory factory;
	
	public ASMImporter() {
		factory = ClassModelFactory.getFactory();
	}

	@Override
	public ClassModel importModel(URI input) throws IOException {
		ClassModel model = factory.createClassModel();
		walkFileTree(input, new ImportModelFileWalker(model));
		
		return model;
	}

	@Override
	public void exportModel(ClassModel model, URI output) throws IOException {
//		if (Files.notExists(output, LinkOption.NOFOLLOW_LINKS)) {
//			Files.copy(origin, dest, StandardCopyOption.COPY_ATTRIBUTES);
//		}
		
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
