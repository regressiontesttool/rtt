package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.ui.services.IDisposable;

public abstract class ViewerItemProvider implements IDisposable {

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
	abstract boolean hasRoot(Object parentElement);
}