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
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

import rtt.annotations.Parser.Node;

public class ASMPrototype {
	
	private static final class AddAnnotationAdapter extends ClassVisitor {
		
		private static final String NODE_DESC = Type.getDescriptor(Node.class);
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

	public static final File ANNOTATED = new File("./dump/input/AnnotatedClass.class");
	public static final File NOTANNOTATED = new File("./dump/input/NotAnnotatedClass.class");
	
	public static final File ANNOTATED_DUMP = new File("./dump/output/AnnotatedClass.class");
	public static final File NOTANNOTATED_DUMP = new File("./dump/output/NotAnnotatedClass.class");
	
	public static void main(String[] args) throws Exception {
		visitClass(new FileInputStream(ANNOTATED), new FileOutputStream(ANNOTATED_DUMP, false));		
		visitClass(new FileInputStream(NOTANNOTATED), new FileOutputStream(NOTANNOTATED_DUMP, false));
	}

	private static void visitClass(FileInputStream input, FileOutputStream output) throws IOException, FileNotFoundException {
		ClassReader reader = new ClassReader(input);		
		ClassWriter writer = new ClassWriter(reader, 0);
				
		CheckClassAdapter checkAdapter = new CheckClassAdapter(writer);
		
		reader.accept(new AddAnnotationAdapter(checkAdapter), 0);	
		
		output.write(writer.toByteArray());
	}

}
