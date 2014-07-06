package rtt.annotation.test.rtt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotations.Parser;

@Parser
public class RTTParser {
	
	ClassModel model;
	
	public RTTParser() {
		model = ClassModelFactory.getFactory().createClassModel();
	}
	
	@Parser.Initialize
	public void initialize(InputStream in) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<String> fileNames = new ArrayList<String>();
		
		String line = reader.readLine();
		while (line != null) {
			if (!line.equals("") && !fileNames.contains(line)) {
				fileNames.add(line);
			}
			line = reader.readLine();
		}
		
		reader.close();	
		
		Bundle bundle = Platform.getBundle(AnnotationEditorPlugin.PLUGIN_ID);
		if (bundle == null) {
			throw new IllegalStateException("Bundle not found");
		}
		
		for (String fileName : fileNames) {
			URL resourceURL = bundle.getEntry(fileName);
			System.out.println("Reading: " + resourceURL);

//			Importer importer = new ASMImporter();
//			importer.importClass(resourceURL.openStream(), model);
		}		
	}
	
	@Parser.AST
	public ClassModel getModel() {
		return model;
	}

}
