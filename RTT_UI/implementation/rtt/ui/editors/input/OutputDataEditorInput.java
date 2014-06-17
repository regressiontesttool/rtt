package rtt.ui.editors.input;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rtt.core.exceptions.RTTException;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.Manager;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.model.RttProject;
import rtt.ui.utils.RttLog;
import rtt.ui.utils.RttPluginUtil;

public class OutputDataEditorInput implements IEditorInput {
	
	public static final URI LEXER_URI = URI.createURI("rtt/output/lexer.rtt");
	public static final URI PARSER_URI = URI.createURI("rtt/output/parser.rtt");

	private int version;
	private String projectName;
	private String configName;
	private String suiteName;
	private String caseName;
	private OutputDataType type;
	
	protected ResourceSet resourceSet;

	public OutputDataEditorInput(RttProject project, String suiteName,
			String caseName, int version, OutputDataType type) {

		this.projectName = project.getName();
		this.suiteName = suiteName;
		this.caseName = caseName;
		this.configName = project.getActiveConfiguration().getName();
		this.version = version;
		this.type = type;
		
		resourceSet = new ResourceSetImpl();
		
		Manager manager = null;
		
		try {
			manager = project.getManager();		
			ArchiveLoader loader = manager.getArchive().getLoader();
			
			OutputDataManager outputManager = new OutputDataManager(
					loader, suiteName, caseName, project.getActiveConfiguration(), type);
			
			RttPluginUtil.loadResource(resourceSet, LEXER_URI, outputManager.getLexerInputStream(version));
			RttPluginUtil.loadResource(resourceSet, PARSER_URI, outputManager.getParserInputStream(version));
		} catch (RTTException e) {
			RttLog.log(e);
		} finally {
			if (manager != null) {
				manager.close();
			}
		}
	}

	public OutputDataType getType() {
		return type;
	}
	
	
	

//	@Override
//	public boolean equals(Object obj) {
//		if (obj.getClass() != this.getClass()) {
//			return false;
//		}
//
//		if (obj instanceof OutputDataEditorInput) {
//			OutputDataEditorInput input = (OutputDataEditorInput) obj;
//			
//			return projectName.equals(input.projectName) && 
//					configName.equals(input.configName) &&
//					suiteName.equals(input.suiteName) &&
//					caseName.equals(input.caseName) &&
//					type.equals(input.type);			
//		}
//
//		return false;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((caseName == null) ? 0 : caseName.hashCode());
		result = prime * result
				+ ((configName == null) ? 0 : configName.hashCode());
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result
				+ ((suiteName == null) ? 0 : suiteName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutputDataEditorInput other = (OutputDataEditorInput) obj;
		
		return projectName.equals(other.projectName) && 
				configName.equals(other.configName) &&
				suiteName.equals(other.suiteName) &&
				caseName.equals(other.caseName) &&
				type.equals(other.type) &&
				version == other.version;		
		
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return type.getText() + " data [" + suiteName + "/"
				+ caseName + "] (Ver. " + version + ")";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	public ResourceSet getResourceSet() {
		return resourceSet;
	}
	
	public Resource getResource(URI uri) {
		return resourceSet.getResource(uri, false);
	}
}
