package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.ui.viewer.util.ViewerItemProvider.ItemColor;
import rtt.annotation.editor.ui.viewer.util.ViewerItemProvider.ItemFont;

public class ViewerItem {

	private Object parent;
	private String[] columns;

	private Color foreground = null;
	private Font font = null;

	private ModelElement<?> modelElement;

	public ViewerItem(Object parent, String... columns) {
		this.parent = parent;
		this.columns = columns;
	}

	public Object getParent() {
		return parent;
	}

	public String getColumnText(int index) {
		if (index < columns.length) {
			return columns[index];
		}

		return "";
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(ItemColor foreground) {
		this.foreground = ItemColor.getColor(foreground);
	}
	
	protected Font getFont() {
		return font;
	}
	
	public void setFont(ItemFont font) {
		this.font = ItemFont.getFont(font);
	}

	public void setModelElement(ModelElement<?> modelElement) {
		this.modelElement = modelElement;
	}

	public ModelElement<?> getModelElement() {
		return modelElement;
	}			
}