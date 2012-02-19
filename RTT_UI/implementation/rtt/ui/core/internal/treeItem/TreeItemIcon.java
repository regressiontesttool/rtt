package rtt.ui.core.internal.treeItem;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import rtt.ui.RttPluginUI;

public enum TreeItemIcon {
	
	NONE(""),
	TESTCASE("icons/testcase.gif"),
	CLASSPATH("icons/classpath.gif"),
	CONFIG("icons/config.gif"),
	INPUT("icons/input.gif"),
	LEXER("icons/lexer.gif"),
	LEXER_OUTPUT("icons/lexeroutput.gif"),
	PARSER("icons/parser.gif"),
	PARSER_OUTPUT("icons/parseroutput.gif"),
	RTT_PROJECT("icons/project.gif"),
	TESTSUITE("icons/suites_full.gif", "icons/suites_empty.gif");
	
	private String emptyIconPath;
	private String fullIconPath;
	
	private TreeItemIcon(String fullIconPath, String emptyIconPath) {
		this.fullIconPath = fullIconPath;
		this.emptyIconPath = emptyIconPath;
	}
	
	private TreeItemIcon(String iconPath) {
		this.fullIconPath = iconPath;
		this.emptyIconPath = iconPath;
	}
	
	public Image createImage(boolean emptyIcon) {
		String iconPath = "";
		
		if (emptyIcon) {
			iconPath = emptyIconPath;
		} else {
			iconPath = fullIconPath;
		}		
		
		if (iconPath != null && !iconPath.equals("")) {
			ImageDescriptor descriptor = RttPluginUI.imageDescriptorFromPlugin(RttPluginUI.PLUGIN_ID, iconPath);
			if (descriptor != null) {
				return descriptor.createImage();
			}
		}

		return ImageDescriptor.getMissingImageDescriptor().createImage();
	}

}
