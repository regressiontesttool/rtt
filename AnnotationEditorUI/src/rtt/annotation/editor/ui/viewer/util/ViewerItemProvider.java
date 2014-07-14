package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.services.IDisposable;

public abstract class ViewerItemProvider implements IDisposable {
	
	public enum ItemColor {
		NODE("NODE_COLOR", new RGB(0, 0, 155)),
		EXTEND_NODE("EXTENDED_NODE_COLOR", new RGB(200, 0, 0)),
		EXTEND_MEMBER("EXTEND_MEMBER_COLOR", new RGB(0, 155, 0)),
		
		COMPARE("COMPARE_COLOR", new RGB(0, 0, 155)),
		INFORMATIONAL("INFORMATIONAL_COLOR", new RGB(0, 155, 255));
		
		
		private String colorKey;

		private ItemColor(String colorKey, RGB colorData) {
			System.out.println("Putting color '" + colorKey + "'");
			
			this.colorKey = colorKey;
			JFaceResources.getColorRegistry().put(colorKey, colorData);
		}
		
		public static Color getColor(ItemColor color) {
			return JFaceResources.getColorRegistry().get(color.colorKey);
		}
	}
	
	public enum ItemFont {
		DEFAULT_FONT(),
		BOLD_FONT(),
		ITALIC_FONT();
		
		public static Font getFont(ItemFont itemFont) {
			Font font = null;			
			
			switch(itemFont) {
			case BOLD_FONT:
				font = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
				break;
			case DEFAULT_FONT:
				font = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
				break;
			case ITALIC_FONT:
				font = JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
				break;				
			}
			
			return font;
		}
	}
	
	
	public static class ViewerTree extends ViewerItem {

		List<ViewerItem> items = new ArrayList<>();

		public ViewerTree(Object parent, String... columns) {
			super(parent, columns);
		}

		public void addItem(ViewerItem item) {
			items.add(item);
		}

		public void clear() {
			items.clear();
		}
	}

	private ViewerItemContentProvider contentProvider;
	private ILabelProvider labelProvider;

	public IContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = new ViewerItemContentProvider(this);
		}

		return contentProvider;
	}

	public CellLabelProvider getLabelProvider(int column) {
		//			if (labelProvider == null) {
		//				labelProvider = new ModelElementLabelProvider(column);
		//			}
		//			
		//			return labelProvider;

		return ViewerItemLabelProvider.create(column);
	}

	@Override
	public void dispose() {
		if (contentProvider != null) {
			contentProvider.dispose();
			contentProvider = null;
		}

		if (labelProvider != null) {
			labelProvider.dispose();
			labelProvider = null;
		}
	}

	protected ViewerItem createItem(ViewerItem parent, String... columns) {
		return new ViewerItem(parent, columns);
	}

	protected ViewerTree createTree(ViewerItem parent, String... columns) {
		return new ViewerTree(parent, columns);
	}

	abstract List<ViewerItem> setInput(Object input);
	abstract boolean hasRoot(Object parentElement);
}