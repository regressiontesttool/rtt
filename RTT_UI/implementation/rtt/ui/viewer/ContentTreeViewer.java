package rtt.ui.viewer;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;

import rtt.ui.content.IClickableContent;
import rtt.ui.content.IDecoratableContent;

public class ContentTreeViewer extends TreeViewer {

	private class ContentLabelDecorator extends BaseLabelProvider implements
			ILabelDecorator {
		@Override
		public String decorateText(String text, Object element) {
			if (element instanceof IDecoratableContent) {
				IDecoratableContent content = (IDecoratableContent) element;
				return content.decorateText(text, content);
			}

			return text;
		}

		@Override
		public Image decorateImage(Image image, Object element) {
			if (element instanceof IDecoratableContent) {
				IDecoratableContent content = (IDecoratableContent) element;
				return content.decorateImage(resourceManager, image, content);
			}

			return image;
		}
	}

	LocalResourceManager resourceManager;

	public ContentTreeViewer(Composite parent, int style,
			final IWorkbenchPage currentPage) {
		super(parent, style | SWT.VIRTUAL);

		initViewer(currentPage);
		ColumnViewerToolTipSupport.enableFor(this, ToolTip.NO_RECREATE);
	}

	private void initViewer(final IWorkbenchPage currentPage) {
		resourceManager = new LocalResourceManager(
				JFaceResources.getResources(), this.getControl());

		setContentProvider(new RttTreeContentProvider());
		setLabelProvider(new DecoratingLabelProvider(
				new RttLabelProvider(),
				new ContentLabelDecorator()));

		addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				if (event.getSelection() != null
						&& event.getSelection() instanceof IStructuredSelection) {

					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();

					if (selection.getFirstElement() instanceof IClickableContent) {
						((IClickableContent) selection.getFirstElement())
								.doDoubleClick(currentPage);
					}
				}
			}
		});
	}

	public ContentTreeViewer(Tree tree, final IWorkbenchPage currentPage) {
		super(tree);
		initViewer(currentPage);
	}
}
