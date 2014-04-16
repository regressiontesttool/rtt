package rtt.ui.content.main;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.ui.RttPluginUI;

public enum ContentIcon {
	
	NONE("icons/none.gif"),
	PLACEHOLDER("icons/place_full.gif","icons/place_empty.gif"),
	
	TESTCASE("icons/testcase.gif"),
	PARAMETER("icons/parser.gif"),
	
	CLASSPATH("icons/classpath.gif"),
	CONFIG("icons/config_full.gif", "icons/config_empty.gif"),
	
	INPUT("icons/input.gif"),
	LEXER("icons/lexer.gif"),
	PARSER("icons/parser.gif"),
	PROJECT("icons/project.gif"),
	TESTSUITE("icons/suites_full.gif", "icons/suites_empty.gif"),
	
	REFERENCE("icons/reference.gif"),
	
	FAILED("icons/failed.gif"),
	PASSED("icons/passed.gif"),
	SKIPPED("icons/skipped.gif"),
	
	INFO("icons/info.gif"),
	GENERATION("icons/generation.gif"),
	TESTRUN("icons/testrun.gif"), 
	ARCHIVE("icons/archive.gif"),
	
	DETAIL("icons/detail.gif"),
	COMMENT("icons/comment.gif"),
	
	VERSION("icons/version.gif"),
	INPUT_HISTORY("icons/input_version.gif"),
	REFERENCE_HISTORY("icons/reference_version.gif"),
	TEST_HISTORY("icons/test_version.gif");
	
	private String emptyIconPath;
	private String fullIconPath;
	
	private static final LocalResourceManager manager = new LocalResourceManager(JFaceResources.getResources());
	
	private ContentIcon(String fullIconPath, String emptyIconPath) {
		this.fullIconPath = fullIconPath;
		this.emptyIconPath = emptyIconPath;
	}

	private ContentIcon(String iconPath) {
		this.fullIconPath = iconPath;
		this.emptyIconPath = iconPath;
	}
	
	public ImageDescriptor getImageDescriptor(boolean emptyImage) {
		String iconPath = "";
		
		if (emptyImage) {
			iconPath = emptyIconPath;
		} else {
			iconPath = fullIconPath;
		}
		
		if (iconPath != null && !iconPath.equals("")) {
			return RttPluginUI.getImageDescriptor(iconPath); 
		}
		
		return null;
	}
	
	public Image getImage(boolean emptyImage) {
		ImageDescriptor descriptor = getImageDescriptor(emptyImage);
		if (descriptor != null) {
			return manager.createImage(descriptor);			 
		}
		
		return manager.createImage(ImageDescriptor.getMissingImageDescriptor());
	}
}