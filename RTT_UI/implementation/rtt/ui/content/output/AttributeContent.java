package rtt.ui.content.output;

import rtt.core.archive.output.Attribute;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public class AttributeContent extends AbstractContent {

	private Attribute attribute;
	
	public AttributeContent(IContent parent, Attribute attribute) {
		super(parent);
		this.attribute = attribute;
	}

	@Override
	public String getText() {
		return attribute.getName() + " = " + attribute.getValue();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.ATTRIBUTE;
	}

}
