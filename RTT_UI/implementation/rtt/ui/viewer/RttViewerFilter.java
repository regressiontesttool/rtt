package rtt.ui.viewer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rtt.core.archive.logging.EntryType;
import rtt.ui.content.logging.FailureContent;
import rtt.ui.content.logging.LogDetailContent;
import rtt.ui.content.logging.LogEntryContent;
import rtt.ui.content.logging.TestResultContent;

public class RttViewerFilter extends ViewerFilter {
	
	private enum FilterType {
		NONE(null), INFO(EntryType.INFO), GENERATION(EntryType.GENERATION), TESTRUN(
				EntryType.TESTRUN), ARCHIVE(EntryType.ARCHIVE);

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
				return ARCHIVE;
			case 2:
				return GENERATION;
			case 3:
				return TESTRUN;
			case 4:
				return INFO;
				
			default:
				return NONE;
			}
		}
	}

	private int selectionIndex;

	public RttViewerFilter(int selectionIndex) {
		this.selectionIndex = selectionIndex;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if (element instanceof LogDetailContent) {
			return true;
		}
		
		if (element instanceof TestResultContent) {
			return true;
		}
		
		if (element instanceof FailureContent) {
			return true;
		}

		if (element instanceof LogEntryContent) {
			FilterType type = FilterType.getType(selectionIndex);
			return type.show((LogEntryContent) element);
		}		

		return false;
	}

}
