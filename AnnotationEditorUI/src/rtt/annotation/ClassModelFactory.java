package rtt.annotation;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ClassModelFactory {

	public static ClassModel createModel(IFile inputFile) throws IOException {
		
		ClassModel model = new ClassModel();
		
		JarFile jar = new JarFile(inputFile.getLocation().toFile(), false, JarFile.OPEN_READ);
		Enumeration<JarEntry> entries = jar.entries();
		
		ClassReader reader = null;
		
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			System.out.println("Entry: " + entry.getName());
			if (entry.getName().endsWith(".class")) {
				reader = new ClassReader(jar.getInputStream(entry));
				ClassNode node = new ClassNode(Opcodes.ASM5);
				
				reader.accept(node, ClassReader.SKIP_CODE);
				
				model.add(node);
			}
		}
		
		jar.close();
		
		return model;
	}

}
