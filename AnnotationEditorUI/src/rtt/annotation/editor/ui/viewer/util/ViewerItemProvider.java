package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.services.IDisposable;

public abstract class ViewerItemProvider implements IDisposable {
	
	protected static class ViewerItemLabelProvider extends ColumnLabelProvider {
		
		private int column = ViewerItemProvider.FIRST_COLUMN;
		
		protected ViewerItemLabelProvider(int column) {
			this.column = column;
		}
		
		@Override
		public String getText(Object element) {
			if (element instanceof ViewerItem) {
				return ((ViewerItem) element).getColumnText(column);
			}
			
			return super.getText(element);
		}
		
		protected static ColumnLabelProvider create(int column) {
			return new ViewerItemLabelProvider(column);
		}
		
		@Override
		public Color getForeground(Object element) {
			if (element instanceof ViewerItem) {
				return ((ViewerItem) element).getColor();
			}
			
			return super.getForeground(element);
		}
		
		@Override
		public Font getFont(Object element) {
			if (element instanceof ViewerItem) {
				return ((ViewerItem) element).getFont();
			}
			
			return super.getFont(element);
		}
		
	}
	
	protected static class ViewerItemContentProvider implements ITreeContentProvider {
		
		private ViewerItemProvider provider;
		
		private static final Object[] EMPTY_ARRAY = new Object[0];
		private ViewerItem mainItem = new TextViewerItem(null);
			
		protected ViewerItemContentProvider(ViewerItemProvider provider) {
			this.provider = provider;
		}
		
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput == null) {
				mainItem.clear();
			} 
			
			if (provider.hasElements(newInput)) {
				mainItem.clear();
				List<ViewerItem> newData = provider.setInput(newInput, mainItem);
				if (newData != null && !newData.isEmpty()) {
					mainItem.addAll(newData);
				}
			}		
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {		
			if (parentElement instanceof ViewerItem) {
				return ((ViewerItem) parentElement).getChildren().toArray();
			}
			
			if (provider.hasElements(parentElement)) {
				return mainItem.getChildren().toArray();
			}
			
			return EMPTY_ARRAY;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof ViewerItem) {
				return ((ViewerItem) element).getParent();
			}
			
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

	}
	
	public static final int FIRST_COLUMN = 0;
	public static final int SECOND_COLUMN = 1;
	public static final int THIRD_COLUMN = 2;
	public static final int FOURTH_COLUMN = 3;
	public static final int FIFTH_COLUMN = 4;

	private ViewerItemContentProvider contentProvider;
	private List<CellLabelProvider> labelProviders = new ArrayList<>(0);

	public IContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = new ViewerItemContentProvider(this);
		}

		return contentProvider;
	}

	public CellLabelProvider getLabelProvider(int column) {
		if (labelProviders.size() <= column) {
			labelProviders.add(ViewerItemLabelProvider.create(column));
		}
		
		return labelProviders.get(column);
	}

	@Override
	public void dispose() {
		if (contentProvider != null) {
			contentProvider.dispose();
			contentProvider = null;
		}
		
		for (CellLabelProvider labelProvider : labelProviders) {
			labelProvider.dispose();
		}
		
		labelProviders.clear();
		labelProviders = null;
	}

	abstract List<ViewerItem> setInput(Object input, ViewerItem parent);
	abstract boolean hasElements(Object parentElement);
}