package rtt.ui.ecore.util;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;

import rtt.ui.ecore.editor.EcoreEditor;
import rtt.ui.utils.RttLog;

/**
 * This class is used to track resource changes and
 * disable the {@link EcoreEditor} if any change has occurred.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class ResourceManager implements IResourceChangeListener {

	protected static final String CHANGED_RESOURCES_TITLE = 
			"rtt.ecore.editor.conflict.title";
	protected static final String CHANGED_RESOURCES_MESSAGE = 
			"rtt.ecore.editor.conflict.message";
	
	
	protected class ResourceDeltaVisitor implements IResourceDeltaVisitor {

		public boolean visit(IResourceDelta delta) {
			if (delta.getResource().getType() == IResource.FILE) {
				URI deltaURI = URI.createPlatformResourceURI(delta.getResource().getFullPath().toString(), true);
				
				for (Resource resource : editor.getEditingDomain().getResourceSet().getResources()) {
					if (resource.getURI().equals(deltaURI)) {
						editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								editor.getEditingDomain().getCommandStack().flush();
								editor.getLeftTreeViewer().setSelection(StructuredSelection.EMPTY);
								editor.getLeftTreeViewer().getControl().setEnabled(false);
								editor.getRightTreeViewer().setSelection(StructuredSelection.EMPTY);
								editor.getRightTreeViewer().getControl().setEnabled(false);
								
								MessageDialog.openError(editor.getSite().getShell(), 
										Messages.getString(CHANGED_RESOURCES_TITLE),
										Messages.getString(CHANGED_RESOURCES_MESSAGE));								
							}
						});
					}
				}
			}
			
			return true;
		}
	}
	
	private EcoreEditor editor;
	private boolean enable = false;

	public ResourceManager(EcoreEditor rttEcoreEditor) {
		this.editor = rttEcoreEditor;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		if (enable) {
			final ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();			
			
			try {
				delta.accept(visitor);
			} catch (CoreException exception) {
				RttLog.log(exception);
			}
		}		
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
