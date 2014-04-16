package rtt.ui.content.main;

import rtt.ui.content.IContent;

public class ArchiveContent extends AbstractContent {

	public ArchiveContent(IContent parent) {
		super(parent);
	}

	@Override
	public String getText() {
		return "No Archive found.";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.ARCHIVE;
	}

}
