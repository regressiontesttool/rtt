package rtt.annotation.editor.ui.viewer.util;

public class TextViewerItem extends ViewerItem {

	private String[] columns;

	public TextViewerItem(ViewerItem parent, String... columns) {
		super(parent);
		this.columns = columns;
	}
	
	@Override
	protected String getColumnText(int columnIndex) {
		if (columnIndex < columns.length) {
			return columns[columnIndex];
		}
		
		return "";
	}
}
