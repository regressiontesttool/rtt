package prototype.asm;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;

public class JarASMTransform {
	
	static final String PACKAGE_SEPARATOR = "/";
	
	private ClassModel model;
	
	public void importModel(Path zipPath) throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "false");
		
		URI zipUri = URI.create("jar:" + zipPath.toUri());
		model = ClassModelFactory.getFactory().createClassModel();
		
		try (FileSystem zipFs = FileSystems.newFileSystem(zipUri, env)) {
			Files.walkFileTree(zipFs.getPath("/"), new ReadFileWalker(model));
		}
	}
	
	public void exportModel(Path origin, Path dest) throws IOException {
		if (Files.notExists(dest, LinkOption.NOFOLLOW_LINKS)) {
			Files.copy(origin, dest, StandardCopyOption.COPY_ATTRIBUTES);
		}		
		
		Map<String, String> env = new HashMap<>();
		env.put("create", "false");
		
		URI zipUri = URI.create("jar:" + dest.toUri());
		
		try (FileSystem zipFs = FileSystems.newFileSystem(zipUri, env)) {
			Files.walkFileTree(zipFs.getPath("/"), new WriteFileWalker(model));
		}
	}
	
	public ClassModel getModel() {
		return model;
	}

}
