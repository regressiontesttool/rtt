package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.services.IDisposable;

import rtt.annotation.editor.model.ModelElement;

public abstract class ViewerItemProvider implements IDisposable {
		
		private ViewerItemContentProvider contentProvider;
		private ILabelProvider labelProvider;
		
		public static class ViewerItem {
			
			private Object parent;
			private String[] columns;
			
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
				return null;
			}

			public void setModelElement(ModelElement<?> modelElement) {
				this.modelElement = modelElement;
			}
			
			public ModelElement<?> getModelElement() {
				return modelElement;
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