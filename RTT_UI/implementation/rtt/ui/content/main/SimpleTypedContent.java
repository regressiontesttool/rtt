package rtt.ui.content.main;

import java.util.ArrayList;
import java.util.List;

import rtt.ui.content.IContent;


public class SimpleTypedContent extends AbstractContent {

	public enum ContentType {
		
		NO_ARCHIVE("No Archive found.", ContentIcon.ARCHIVE),
		
		INITIALNODE("Initial Node: ", ContentIcon.INITIALNODE), 
		CLASSPATHENTRY("", ContentIcon.CLASSPATH),
		
		PARAMETER("", ContentIcon.PARAMETER);
		
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
