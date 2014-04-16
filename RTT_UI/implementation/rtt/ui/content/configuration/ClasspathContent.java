package rtt.ui.content.configuration;

import rtt.core.archive.configuration.Classpath;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;

public class ClasspathContent extends AbstractContent {

	public ClasspathContent(IContent parent, Classpath classpath) {

		super(parent);

		for (String path : classpath.getPath()) {
			if (path != null && !path.equals("")) {
				childs.add(new SimpleTypedContent(this,
						ContentType.CLASSPATHENTRY, path));
			}
		}
	}

	@Override
	public String getText() {
		return "Classpath (" + childs.size() + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.CLASSPATH;
	}

}
