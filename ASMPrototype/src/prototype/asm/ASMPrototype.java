package prototype.asm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.objectweb.asm.Type;

import prototype.asm.model.ClassElement;
import prototype.asm.model.ClassElement.Annotation;
import prototype.asm.model.ClassModel;
import prototype.asm.model.ClassModel.PackageElement;
import rtt.annotations.Parser.Node;

public class ASMPrototype {
	
	public static final String NODE_DESC = Type.getDescriptor(Node.class);
	public static final Random r = new Random();

	public static void main(String[] args) throws Exception {
		
		Path zipPath = Paths.get("jar/asm-5.0.3.jar").toAbsolutePath();
		Path newZipPath = Paths.get("jar/asm-changed.jar").toAbsolutePath();
		
		JarASMTransform transform = new JarASMTransform();
		transform.importModel(zipPath);
		
		printModel(transform.getModel(), true);
		printModel(transform.getModel(), false);
		
		transform.exportModel(zipPath, newZipPath);
	}

	private static void printModel(ClassModel model, boolean randomNodes) {
		for (PackageElement packageElement : model.getClasses().keySet()) {
			System.out.println("Entry: " + packageElement.getName());
			for (ClassElement element : model.getClasses(packageElement)) {
				System.out.println(packageElement.getName() + "." + element.getClassName() + " - " + element.getAnnotation());
				
				if (randomNodes && r.nextInt(10) == 0) {
					element.setAnnotation(Annotation.NODE);
					element.setChanged(true);
				}
			}			
		}
	}
}
