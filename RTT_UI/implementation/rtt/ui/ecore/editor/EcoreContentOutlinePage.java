package rtt.ui.ecore.editor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * A simple {@link ContentOutlinePage} which displays
 * the complete content of the selected ecore file.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class EcoreContentOutlinePage extends ContentOutlinePage {
	
	private TreeViewer viewer;
	private IContentProvider contentProvider;
	private IBaseLabelProvider labelProvider;
	private Object input;
	
	public EcoreContentOutlinePage(AdapterFactory adapterFactory) {
		super();
		this.contentProvider = new AdapterFactoryContentProvider(adapterFactory);
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	@Override
	public final void createControl(final Composite parent) {
		super.createControl(parent);
		
		viewer = getTreeViewer();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		
		viewer.addSelectionChangedListener(this);
		viewer.setInput(input);
		
		viewer.expandToLevel(EcoreEditor.ECLASS_LEVEL);
	}

	public final void setInput(final Object input) {
		if (input != null && !input.equals(this.input)) {
			this.input = input;
			if (getTreeViewer() != null) {
				getTreeViewer().setInput(input);
			}
		}		
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
		
		viewer = null;
		
		super.dispose();
	}
}
