package rtt.ui.content.internal.configuration;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Path;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.internal.ContentIcon;
import rtt.ui.content.internal.EmptyContent;
import rtt.ui.content.internal.SimpleTypedContent;
import rtt.ui.content.internal.SimpleTypedContent.ContentType;

public class ClasspathContent extends AbstractContent implements IContent {

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
