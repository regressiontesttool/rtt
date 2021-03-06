package rtt.annotation.editor.ui.viewer.provider;



public class TextViewerItem extends ViewerItem {

	private String[] columns;

	public TextViewerItem(ViewerItem parent, String... columns) {
		super(parent);
		this.columns = columns;
	}
	
	@Override
	public String getColumnText(int columnIndex) {
		if (columnIndex >= 0 && columnIndex < columns.length) {
			return columns[columnIndex];
		}
		
		return "";
	}
}
