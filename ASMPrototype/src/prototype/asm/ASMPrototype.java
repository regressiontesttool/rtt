package prototype.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

import prototype.asm.ClassElement.Annotation;
import rtt.annotations.Parser.Node;

public class ASMPrototype {
	
	private static final class AddAnnotationAdapter extends ClassVisitor {
	
		private boolean hasAnnotation = false;

		public AddAnnotationAdapter(ClassVisitor visitor) {
			super(Opcodes.ASM5, visitor);
		}
		
		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			System.out.println("Annotation: " + desc);
			if (NODE_DESC.equals(desc)) {
				hasAnnotation  = true;
				System.out.println("Node annotation found.");
			}
			
			return cv.visitAnnotation(desc, visible);
		}
		
		@Override
		public void visitEnd() {
			if (hasAnnotation == false) {
				AnnotationVisitor av = cv.visitAnnotation(NODE_DESC, true);
				if (av != null) {
					av.visitEnd();
				}
			}
			
			// TODO Auto-generated method stub
			super.visitEnd();
		}
	}
	
	private static final String NODE_DESC = Type.getDescriptor(Node.class);

	public static final File ANNOTATED = new File("./dump/input/AnnotatedClass.class");
	public static final File NOTANNOTATED = new File("./dump/input/NotAnnotatedClass.class");
	
	public static final File ANNOTATED_DUMP = new File("./dump/output/AnnotatedClass.class");
	public static final File NOTANNOTATED_DUMP = new File("./dump/output/NotAnnotatedClass.class");
	
	public static void main(String[] args) throws Exception {
		File binFolder = new File("./bin/");
		if (!binFolder.exists() && !binFolder.isDirectory()) {
			throw new IllegalStateException("Could not find binary folder");
		}
		
		List<File> classFiles = getFiles(binFolder);
		for (File classFile : classFiles) {
			System.out.println(classFile.getName());
		}
		
		List<ClassElement> elements = readClasses(classFiles);
		for (ClassElement classElement : elements) {
			System.out.println(classElement.getClassName() + " - " + classElement.getAnnotation());
		}
		
		writeClasses(elements);
		
		
		
		
		
		
//		visitClass(new FileInputStream(ANNOTATED), new FileOutputStream(ANNOTATED_DUMP, false));		
//		visitClass(new FileInputStream(NOTANNOTATED), new FileOutputStream(NOTANNOTATED_DUMP, false));
	}

	private static void writeClasses(List<ClassElement> elements) {
		// TODO Auto-generated method stub
		
	}

	private static List<ClassElement> readClasses(List<File> classFiles) throws Exception {
		List<ClassElement> elements = new ArrayList<ClassElement>();
		for (File classFile : classFiles) {
			elements.add(readClass(new FileInputStream(classFile)));
		}
		
		return elements;
	}

	private static ClassElement readClass(FileInputStream fileInputStream) throws Exception {
		final ClassElement result = new ClassElement();
		
		ClassReader reader = new ClassReader(fileInputStream);
		result.setName(reader.getClassName());
		result.setSuperClass(reader.getSuperName());
		result.setInterfaces(reader.getInterfaces());
		
		reader.accept(new ClassVisitor(Opcodes.ASM5) {
			@Override
			public AnnotationVisitor visitAnnotation(String desc,
					boolean visible) {
				if (NODE_DESC.equals(desc)) {
					result.setAnnotation(Annotation.NODE);
				}
				
				return null;
			}
			
		}, ClassReader.SKIP_CODE);
		
		return result;
	}

	private static List<File> getFiles(File folder) {
		List<File> files = new ArrayList<File>();
		
		String[] fileNames = folder.list();
		for (String fileName : fileNames) {
			String path = folder.getPath() + File.separator + fileName;
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory()) {
					files.addAll(getFiles(file));
				} else if (file.getName().endsWith(".class")) {
					files.add(file);
				}				
			}			
		}

		return files;
	}

	private static void visitClass(FileInputStream input, FileOutputStream output) throws IOException, FileNotFoundException {
		ClassReader reader = new ClassReader(input);		
		ClassWriter writer = new ClassWriter(reader, 0);
				
		CheckClassAdapter checkAdapter = new CheckClassAdapter(writer);
		
		reader.accept(new AddAnnotationAdapter(checkAdapter), 0);	
		
		output.write(writer.toByteArray());
	}

}
