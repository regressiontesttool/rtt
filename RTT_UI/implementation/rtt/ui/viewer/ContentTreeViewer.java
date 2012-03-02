package rtt.ui.viewer;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;

import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;

public class ContentTreeViewer extends TreeViewer {

	public static class TreeContentProvider extends BaseContentProvider
			implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			return getElements(parentElement);
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof IContent) {
				return ((IContent) element).getParent();
			}

			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof IContent) {
				return ((IContent) element).hasChildren();
			}

			return false;
		}
	}

	public class ContentLabelProviderWithImage extends BaseContentLabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element instanceof IContent) {
				return ((IContent) element).getImage(resourceManager);
			}

			return super.getImage(element);
		}
	}

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

	private LocalResourceManager resourceManager;

	public ContentTreeViewer(Composite parent, int style,
			final IWorkbenchPage currentPage) {
		super(parent, style | SWT.VIRTUAL);

		initViewer(currentPage);
	}

	private void initViewer(final IWorkbenchPage currentPage) {
		resourceManager = new LocalResourceManager(
				JFaceResources.getResources(), this.getControl());

		setContentProvider(new TreeContentProvider());
		setLabelProvider(new DecoratingLabelProvider(
				new ContentLabelProviderWithImage(),
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
