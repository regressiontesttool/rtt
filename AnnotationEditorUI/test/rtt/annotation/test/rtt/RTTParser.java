package rtt.annotation.test.rtt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.osgi.framework.Bundle;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.data.asm.visitor.ImportClassElementVisitor;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotations.Node;

@Node
public class RTTParser {
	
	ClassModel model;
	
	public RTTParser() {
		model = ClassModelFactory.getFactory().createClassModel();
	}
	
	@Node.Initialize
	public void initialize(InputStream in) throws Exception {
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in));
		List<String> fileNames = new ArrayList<String>();
		
		String line = bufferedreader.readLine();
		while (line != null) {
			if (!line.equals("") && !fileNames.contains(line)) {
				fileNames.add(line);
			}
			line = bufferedreader.readLine();
		}
		
		bufferedreader.close();	
		
		Bundle bundle = Platform.getBundle(AnnotationEditorPlugin.PLUGIN_ID);
		if (bundle == null) {
			throw new IllegalStateException("Bundle not found");
		}
		
		ClassModelFactory factory = ClassModelFactory.getFactory();
		model = factory.createClassModel();
		
		for (String fileName : fileNames) {
			URL resourceURL = bundle.getEntry(fileName);
			System.out.println("Reading: " + resourceURL);			
			
			ClassReader reader = new ClassReader(resourceURL.openStream());
			
			ClassElement element = factory.createClassElement(model);
			ClassVisitor visitor = new ImportClassElementVisitor(element, factory);
			
			reader.accept(visitor, ClassReader.SKIP_CODE);
			
			model.addClassElement(element);
		}		
	}
	
	@Node.Value
	public ClassModel getModel() {
		return model;
	}

}
