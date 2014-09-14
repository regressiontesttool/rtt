package prototype.asm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.RTTAnnotation;
import rtt.annotation.editor.model.ClassModel.PackageElement;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ASMPrototype {
	
	private static final String JAR_FILE = "jar/asm-changed.jar";
	public static final Random r = new Random();

	public static void main(String[] args) throws Exception {
		
		Path zipPath = Paths.get(JAR_FILE).toAbsolutePath();
		Path newZipPath = Paths.get(JAR_FILE).toAbsolutePath();
		
		JarASMTransform transform = new JarASMTransform();
		transform.importModel(zipPath);
		
		printModel(transform.getModel(), true);
		
		transform.exportModel(zipPath, newZipPath);
	}

	private static void printModel(ClassModel model, boolean randomNodes) {
		for (PackageElement packageElement : model.getPackages()) {
			System.out.println("Package: " + packageElement.getName());
			for (ClassElement element : model.getClasses(packageElement.getName())) {
				StringBuilder text = new StringBuilder(element.getName());				
				
				if (element.getSuperClass() != null) {
					text.append(" extends ");
					text.append(element.getSuperClass().getName());
				}
				
				if (element.getAnnotation() != null) {
					text.append(" - ");
					text.append(element.getName());
				}
				
				if (randomNodes) {
					int random = r.nextInt(100);
					
					if (random > 0 && random < 30) {
						if (element.hasAnnotation()) {
							element.setAnnotation(null);
						} else {
							element.setAnnotation(RTTAnnotation.create(AnnotationType.NODE));
						}					
						element.setChanged(true);
						if (element.getAnnotation() != null) {
							text.append(" (annotation changed) ");
						}
					}
					
					if (random > 20 && random < 50) {
						text.append(" change fields: ");
						for (FieldElement field : element.getValuableFields()) {
							text.append(field.getName());
							if (field.hasAnnotation()) {
								field.setAnnotation(null);
							} else {
								RTTAnnotation annotation = RTTAnnotation.create(AnnotationType.VALUE);
								annotation.setAttribute("index", 100);
								field.setAnnotation(annotation);
							}
							field.setChanged(true);
							text.append(" = ");
							text.append(field.getAnnotation() != null ? field.getAnnotation().getName() : "null");
							text.append(", ");
						}
					}
					
					if (random > 40 && random < 60) {
						text.append(" change methods: ");
						for (MethodElement method : element.getValuableMethods()) {
							text.append(method.getName());
							if (method.hasAnnotation()) {
								method.setAnnotation(null);
							} else {
								RTTAnnotation annotation = RTTAnnotation.create(AnnotationType.VALUE);
								annotation.setAttribute("name", "aNameForMethod");
								method.setAnnotation(annotation);
							}
							method.setChanged(true);
							text.append(" = ");
							text.append(method.getAnnotation() != null ? method.getAnnotation().getName() : "null");
							text.append(", ");
						}
					}
				}
				
				System.out.println(text);				
			}			
		}
	}
}
