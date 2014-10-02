package rtt.annotation.editor.ui.viewer.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

public abstract class ViewerItem {
	
	public enum ItemColor {
		ANNOTATED("ANNOTATED_COLOR", new RGB(0, 0, 155)),
		ANNOTATED_EXTEND("ANNOTATED_EXTEND_COLOR", new RGB(200, 0, 0)),
		ANNOTATED_MEMBER("ANNOTATED_MEMBER_COLOR", new RGB(0, 155, 0)),
		
		VALUE("VALUE_COLOR", new RGB(0, 0, 155)),
		INITIALIZE("INITIALIZE_COLOR", new RGB(0, 155, 255));
		
		
		private String colorKey;

		private ItemColor(String colorKey, RGB colorData) {
			this.colorKey = colorKey;
			JFaceResources.getColorRegistry().put(colorKey, colorData);
		}
		
		public static Color getColor(ItemColor color) {
			if (color != null) {
				return JFaceResources.getColorRegistry().get(color.colorKey);
			}
			
			return null;		
		}
	}
	
	public enum ItemFont {
		DEFAULT_FONT(),
		BOLD_FONT(),
		ITALIC_FONT();
		
		public static Font getFont(ItemFont itemFont) {
			Font font = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
			
			if (itemFont == BOLD_FONT) {
				font = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
			}
			
			if (itemFont == ITALIC_FONT) {
				font = JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
			}
			
			return font;
		}
	}
	
	private ViewerItem parent;
	private List<ViewerItem> children;
	
	public ViewerItem(ViewerItem parent) {
		this.parent = parent;
		children = new ArrayList<>(0);
	}
	
	protected ViewerItem getParent() {
		return parent;
	}
	
	protected void setParent(ViewerItem parent) {
		this.parent = parent;		
	}
	
	public abstract String getColumnText(int columnIndex);	
	
	protected final Color getColor() {
		return ItemColor.getColor(getItemColor());
	}

	protected ItemColor getItemColor() {
		return null;
	}
	
	protected final Font getFont() {
		return ItemFont.getFont(getItemFont());
	}

	protected ItemFont getItemFont() {
		return null;
	}
	
	protected void clear() {
		children.clear();
	}
	
	protected void add(ViewerItem item) {
		children.add(item);
	}
	
	protected void addAll(List<ViewerItem> newData) {
		children.addAll(newData);
	}
	
	protected List<ViewerItem> getChildren() {
		return children;
	}

	public boolean isEmpty() {
		return children.isEmpty();
	}

	
}
