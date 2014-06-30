package prototype.asm;

import java.io.File;
import java.io.FileInputStream;

import org.objectweb.asm.ClassReader;

public class ASMPrototype {
	
	public static final File ANNOTATED = new File("./bin/testing/AnnotatedClass.class");
	public static final File NOTANNOTATED = new File("./bin/testing/NotAnnotatedClass.class");
	
	public static void main(String[] args) throws Exception {
		ClassReader annotatedReader = new ClassReader(new FileInputStream(ANNOTATED));
		System.out.println(annotatedReader.getClassName());
		
		ClassReader notAnnotatedReader= new ClassReader(new FileInputStream(NOTANNOTATED));
		System.out.println(notAnnotatedReader.getClassName());
	}

}
