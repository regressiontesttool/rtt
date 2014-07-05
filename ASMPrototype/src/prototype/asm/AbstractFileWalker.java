package prototype.asm;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import prototype.asm.model.ClassModel;

public abstract class AbstractFileWalker extends SimpleFileVisitor<Path> {
	
	protected ClassModel model;
	
	public AbstractFileWalker(ClassModel model) {
		this.model = model;
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

	protected final String computePackageName(String completeName) {
		int packageBorder = computePackageBorder(completeName);
		if (packageBorder > 0) {
			return completeName.substring(0, packageBorder);
		}
		
		return "";
	}

	protected final String computeClassName(String completeName) {
		int packageBorder = computePackageBorder(completeName);
		if (packageBorder > 0) {
			return completeName.substring(packageBorder + 1);
		}
		
		return "";
	}
	
	private int computePackageBorder(String completeName) {
		return completeName.lastIndexOf(JarASMTransform.PACKAGE_SEPARATOR);
	}

}
