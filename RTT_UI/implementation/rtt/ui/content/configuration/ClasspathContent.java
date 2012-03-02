package rtt.ui.content.configuration;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Path;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.EmptyContent;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;

public class ClasspathContent extends AbstractContent {

	public ClasspathContent(IContent parent, Classpath classpath) {

		super(parent);

		for (Path path : classpath.getPath()) {
			if (path.getValue() != null) {
				childs.add(new SimpleTypedContent(this,
						ContentType.CLASSPATHENTRY, path.getValue()));
			}
		}

		if (childs.size() == 0) {
			childs.add(new EmptyContent("No classpath entries set."));
		}

	}

	@Override
	public String getText() {
		return "Classpath";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.CLASSPATH;
	}

}
