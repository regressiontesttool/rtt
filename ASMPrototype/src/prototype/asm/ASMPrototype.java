package prototype.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ASMPrototype {
	
	private static final class MyClassVisitor extends ClassVisitor {
		private MyClassVisitor(int api) {
			super(api);
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc,
				boolean visible) {
			System.out.println("Annotation: " + desc + " - " + visible);
			
			return new AnnotationVisitor(api, super.visitAnnotation(desc, visible)) {
			};
		}
	}

	public static final File ANNOTATED = new File("./bin/testing/AnnotatedClass.class");
	public static final File NOTANNOTATED = new File("./bin/testing/NotAnnotatedClass.class");
	
	public static final File ANNOTATED_DUMP = new File("./dump/AnnotatedClass.class");
	public static final File NOTANNOTATED_DUMP = new File("./dump/NotAnnotatedClass.txt");
	
	public static void main(String[] args) throws Exception {
		visitClass(new FileInputStream(ANNOTATED), new FileOutputStream(ANNOTATED_DUMP, false));		
		visitClass(new FileInputStream(NOTANNOTATED), new FileOutputStream(NOTANNOTATED_DUMP, false));
	}

	private static void visitClass(FileInputStream input, FileOutputStream output) throws IOException, FileNotFoundException {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		ClassReader reader = new ClassReader(input);
		System.out.println(reader.getClassName());
		reader.accept(new MyClassVisitor(Opcodes.ASM5), ClassReader.SKIP_CODE);
		reader.accept(writer, 0);
		
		input.close();
		
		output.write(writer.toByteArray());
		output.close();
	}

}
