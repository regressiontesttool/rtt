package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

public class ModelElementViewerTree extends ModelElementViewerItem {
	
	List<ModelElementViewerItem> items = new ArrayList<>();
	
	public ModelElementViewerTree(Object parent, String... columns) {
		super(parent, columns);
	}
	
	public void addItem(ModelElementViewerItem item) {
		items.add(item);
	}
	
	public void clear() {
		items.clear();
	}
}