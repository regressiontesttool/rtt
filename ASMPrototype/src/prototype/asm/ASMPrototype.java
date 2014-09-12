package prototype.asm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.objectweb.asm.Type;

import annotation.MyAnnotation;
import prototype.asm.model.ClassElement;
import prototype.asm.model.ClassElement.Annotation;
import prototype.asm.model.ClassModel;
import prototype.asm.model.ClassModel.PackageElement;
import rtt.annotations.Node;

@MyAnnotation(index=2, informational=true)
public class ASMPrototype {
	
	public static final String NODE_DESC = Type.getDescriptor(Node.class);
	public static final Random r = new Random();

	public static void main(String[] args) throws Exception {
		
		Path zipPath = Paths.get("jar/asm-changed.jar").toAbsolutePath();
		Path newZipPath = Paths.get("jar/asm-changed.jar").toAbsolutePath();
		
		JarASMTransform transform = new JarASMTransform();
		transform.importModel(zipPath);
		
		printModel(transform.getModel(), true);
		
		transform.exportModel(zipPath, newZipPath);
	}

	private static void printModel(ClassModel model, boolean randomNodes) {
		for (PackageElement packageElement : model.getClasses().keySet()) {
			System.out.println("Package: " + packageElement.getName());
			for (ClassElement element : model.getClasses(packageElement)) {
				String text = element.getClassName();
				text += " - " + element.getAnnotation();
				
				if (randomNodes && r.nextInt(5) == 0) {
					if (element.hasAnnotation()) {
						element.setAnnotation(Annotation.EMPTY);
					} else {
						element.setAnnotation(Annotation.NODE);
					}					
					element.setChanged(true);
					text += "(*) = " + element.getAnnotation();
				}
				
				System.out.println(text);				
			}			
		}
	}
}
