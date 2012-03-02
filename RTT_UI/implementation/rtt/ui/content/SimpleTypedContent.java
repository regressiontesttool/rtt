package rtt.ui.content;

import java.util.ArrayList;
import java.util.List;


public class SimpleTypedContent extends AbstractContent {

	public enum ContentType {
		CONFIGURATION_DIRECTORY("Configurations", ContentIcon.CONFIG), 
		TESTSUITE_DIRECTORY("Testsuites", ContentIcon.TESTSUITE), 
		LOG_DIRECTORY("Log", ContentIcon.PLACEHOLDER),
		
		LEXERCLASS("Lexer: ", ContentIcon.LEXER), 
		PARSERCLASS("Parser: ", ContentIcon.PARSER), 
		CLASSPATHENTRY("", ContentIcon.CLASSPATH), 
		
		DETAIL("", ContentIcon.PLACEHOLDER), 
		ATTRIBUTES("Attributes", ContentIcon.ATTRIBUTE_LIST), 
		CHILDREN("Children", ContentIcon.CHILDREN_LIST);

		protected String text;
		protected ContentIcon icon;

		private ContentType(String text, ContentIcon icon) {
			this.text = text;
			this.icon = icon;
		}
	}

	private String text;
	private ContentType type;
	private ContentIcon icon;

	public SimpleTypedContent(IContent parent, ContentType type, String text,
			List<IContent> childs) {

		super(parent);

		this.childs = childs;

		this.type = type;
		
		this.text = type.text + text;
		this.icon = type.icon;
	}

	public SimpleTypedContent(IContent parent, ContentType type,
			List<IContent> childs) {
		this(parent, type, "", childs);
	}

	public SimpleTypedContent(IContent parent, ContentType type, String text) {
		this(parent, type, text, new ArrayList<IContent>());
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	protected ContentIcon getIcon() {
		return icon;
	}
	
	public ContentType getType() {
		return type;
	}
}
