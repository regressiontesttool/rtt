package rtt.ui.content.main;

import org.eclipse.jface.resource.ImageDescriptor;

import rtt.ui.RttPluginUI;

public enum ContentIcon {
	
	NONE("icons/none.gif"),
	PLACEHOLDER("icons/place_full.gif","icons/place_empty.gif"),
	
	TESTCASE("icons/testcase.gif"),
	
	CLASSPATH("icons/classpath.gif"),
	CONFIG("icons/config.gif"),
	
	INPUT("icons/input.gif"),
	LEXER("icons/lexer.gif"),
	LEXER_OUTPUT("icons/lexeroutput.gif"),
	PARSER("icons/parser.gif"),
	PARSER_OUTPUT("icons/parseroutput.gif"),
	PROJECT("icons/project.gif"),
	TESTSUITE("icons/suites_full.gif", "icons/suites_empty.gif"),
	
	REFERENCE("icons/reference.gif"),
	
	FAILED("icons/failed.gif"),
	PASSED("icons/passed.gif"),
	SKIPPED("icons/skipped.gif"),
	
	INFO("icons/info.gif"),
	GENERATION("icons/generation.gif"),
	TESTRUN("icons/testrun.gif"),
	
	VERSION("icons/version.gif"),
	INPUT_HISTORY("icons/input_version.gif"),
	REFERENCE_HISTORY("icons/reference_version.gif"),
	TEST_HISTORY("icons/test_version.gif"),
	
	DETAIL("icons/detail.gif"), 
	AST_TREE("icons/full/obj16/LexerOutputType.gif"), 
	ATTRIBUTE("icons/full/obj16/Attribute.gif"),
	ATTRIBUTE_LIST("icons/full/obj16/AttributeList.gif"),
	CHILDREN_LIST("icons/full/obj16/ChildrenList.gif"),
	NODE("icons/full/obj16/Node.gif"), 
	TOKEN("icons/full/obj16/Token.gif");
	
	private String emptyIconPath;
	private String fullIconPath;
	
	private ContentIcon(String fullIconPath, String emptyIconPath) {
		this.fullIconPath = fullIconPath;
		this.emptyIconPath = emptyIconPath;
	}
	
	private ContentIcon(String iconPath) {
		this.fullIconPath = iconPath;
		this.emptyIconPath = iconPath;
	}
	
	public ImageDescriptor getDescriptor(boolean emptyImage) {
		String iconPath = "";
		
		if (emptyImage) {
			iconPath = emptyIconPath;
		} else {
			iconPath = fullIconPath;
		}		
		
		if (iconPath != null && !iconPath.equals("")) {
			return RttPluginUI.imageDescriptorFromPlugin(RttPluginUI.PLUGIN_ID, iconPath);
		}
		
		return ImageDescriptor.getMissingImageDescriptor();
	}
	
}