package rtt.ui.content.testsuite;

import java.util.List;

import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;

public class ParameterContent extends AbstractContent {

	public ParameterContent(IContent parent, List<String> params) {

		super(parent);
		
		for (String param : params) {
			if (param != null && !param.equals("")) {
				childs.add(new SimpleTypedContent(this, ContentType.PARAMETER, param));
			}
		}
	}

	@Override
	public String getText() {
		return "Parameters (" + childs.size() + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PARAMETER;
	}

}
