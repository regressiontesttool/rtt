package rtt.ui.utils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rtt.core.archive.logging.EntryType;
import rtt.ui.content.internal.log.DetailContent;
import rtt.ui.content.internal.log.LogEntryContent;
import rtt.ui.content.internal.log.TestResultContent;

public class ContentViewerFilter extends ViewerFilter {
	
	private enum FilterType {
		NONE(null), INFO(EntryType.INFO), GENERATION(EntryType.GENERATION), TESTRUN(
				EntryType.TESTRUN);

		private EntryType type;

		private FilterType(EntryType type) {
			this.type = type;
		}

		public boolean show(LogEntryContent content) {
			if (type != null) {
				return content.getType() == this.type;
			}

			return true;
		}

		public static FilterType getType(int selectionIndex) {
			switch (selectionIndex) {
			case 1:
				return INFO;
			case 2:
				return GENERATION;
			case 3:
				return TESTRUN;

			default:
				return NONE;
			}
		}
	}

	private int selectionIndex;

	public ContentViewerFilter(int selectionIndex) {
		this.selectionIndex = selectionIndex;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if (element instanceof DetailContent) {
			return true;
		}

		if (element instanceof LogEntryContent) {
			FilterType type = FilterType.getType(selectionIndex);
			return type.show((LogEntryContent) element);
		}

		if (element instanceof TestResultContent) {
			return true;
		}

		return false;
	}

}
