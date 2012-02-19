package rtt.ui.content.internal;

import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;

public class EmptyContent extends AbstractContent {

	private String text;
	
	public EmptyContent(String text) {
		this(null, text);
	}
	
	public EmptyContent(IContent parent, String text) {
		super(parent);
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.NONE;
	}
	
	@Override
	public <T> T getContent(Class<T> clazz) {
		return null;
	}
}
