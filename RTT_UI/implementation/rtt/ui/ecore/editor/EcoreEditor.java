package rtt.ui.ecore.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import rtt.ui.ecore.EcoreAnnotation;
import rtt.ui.ecore.EcoreController;
import rtt.ui.ecore.editor.util.AbstractSelectionChangedListener;
import rtt.ui.ecore.editor.util.AnnotationViewerFilter;
import rtt.ui.ecore.editor.util.selectionAdapter.AddAnnotationSelectionAdapter;
import rtt.ui.ecore.editor.util.selectionAdapter.RemoveAnnotationSelectionAdapter;
import rtt.ui.ecore.util.ResourceManager;
import rtt.ui.utils.RttPluginUtil;

/**
 * The new RTT Ecore Annotation Editor.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class EcoreEditor extends EditorPart 
		implements IEditingDomainProvider, CommandStackListener {

	/**
	 * Used for {@link TreeViewer#expandToLevel(int)}
	 */
	protected static final int ECLASS_LEVEL = 4;

	/**
	 * The ID of the editor.
	 */
	public static final String ID = "rtt.ui.editors.annotation.ecore";

	protected static final int NODE_SELECTION = 0;
	
	// Used for Ecore-related tasks
	
	/**
	 * The current {@link EditingDomain}.
	 */
	private AdapterFactoryEditingDomain editingDomain;
	
	/**
	 * The current {@link AdapterFactory}.
	 */
	private ComposedAdapterFactory adapterFactory;
	
	// Used for RTT-related tasks
	
	/**
	 * The current {@link EcoreController}.
	 */
	private EcoreController controller;
	
	// UI
	
	/**
	 * The TreeViewer which contains the complete ecore model.
	 */
	private TreeViewer leftTreeViewer;
	
	/**
	 * The {@link TreeViewer} which contains only parts 
	 * of the ecore model, which are annotated.
	 */
	private TreeViewer rightTreeViewer;	
	
	/**
	 * The current {@link AdapterFactoryContentProvider}.
	 */
	private AdapterFactoryContentProvider contentProvider;
	
	/**
	 * The current {@link ILabelProvider}.
	 * Will be an {@link AdapterFactoryLabelProvider}.
	 */
	private ILabelProvider labelProvider;

	private EcoreContentOutlinePage contentOutlinePage;
	
	private IPartListener partListener;
	
	private ResourceManager resourceManager;
	
	private SashForm sashForm;

	private Composite leftButtonPanel;

	private StackLayout leftStackLayout;

	private Composite nodePanel;

	private Composite parserPanel;

	/**
	 * Creates a new {@link EcoreEditor}.
	 */
	public EcoreEditor() {
		super();
		
		// Create an adapter factory that yields item providers.
	    //
	    adapterFactory = RttPluginUtil.createFactory();
	    editingDomain = RttPluginUtil.createEditingDomain(adapterFactory);
	    
	    editingDomain.getCommandStack().addCommandStackListener(this);
	    
	    controller = new EcoreController(editingDomain);
	    contentProvider = new AdapterFactoryContentProvider(adapterFactory);
	    labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	    
	    contentOutlinePage = new EcoreContentOutlinePage(
	    		adapterFactory);
	    
	    partListener = new IPartListener() {
			
			@Override
			public void partOpened(IWorkbenchPart part) {}
			
			@Override
			public void partClosed(IWorkbenchPart part) {}
			
			@Override
			public void partBroughtToTop(IWorkbenchPart part) {}
			
			@Override
			public void partActivated(IWorkbenchPart part) {
				resourceManager.setEnable(!EcoreEditor.this.equals(part));
			}
			
			@Override
			public void partDeactivated(IWorkbenchPart part) {
				resourceManager.setEnable(!EcoreEditor.this.equals(part));
			}
		};
	}
	
	public TreeViewer getLeftTreeViewer() {
		return leftTreeViewer;
	}
	
	public TreeViewer getRightTreeViewer() {
		return rightTreeViewer;
	}
	
	public EcoreController getController() {
		return controller;
	}

	@Override
	public final void doSave(final IProgressMonitor monitor) {
		// Save only resources that have actually changed.
	    final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
	    saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
	    
	    // Do the work within an operation because this 
	    // is a long running activity that modifies the workbench.
	    
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			// This is the method that gets invoked when the operation runs.
			@Override
			public void execute(final IProgressMonitor monitor) {
				// Save the resources to the file system.
				boolean first = true;
				for (Resource resource : editingDomain.getResourceSet().getResources()) {
					if ((first || !resource.getContents().isEmpty() || isPersisted(resource))
							&& !editingDomain.isReadOnly(resource)) {
						try {
							resource.save(saveOptions);
						} catch (Exception exception) {
							throw new RuntimeException(exception);
						}
						first = false;
					}
				}
			}
		};
	      
		try {
			// This runs the options, and shows progress.
			new ProgressMonitorDialog(getSite().getShell()).run(true, false,
					operation);

			// Refresh the necessary state.
			((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (Exception exception) {
			// Something went wrong that shouldn't.
			throw new RuntimeException(exception);
		}
	}
	
	/**
	   * This returns whether something has been persisted to the URI of 
	   * the specified resource.
	   * The implementation uses the URI converter from the 
	   * editor's resource set to try to open an input stream. 
	   * <!-- begin-user-doc -->
	   * <!-- end-user-doc -->
	   * @param resource 
	   * @return true, if persisted
	   */
	  protected final boolean isPersisted(final Resource resource) {
	    boolean result = false;
	    try {
	      InputStream stream = editingDomain.getResourceSet().
	    		  getURIConverter().createInputStream(resource.getURI());
	      if (stream != null) {
	        result = true;
	        stream.close();
	      }
	    } catch (IOException e) {
	      // Ignore
	    }
	    return result;
	  }

	@Override
	public final void doSaveAs() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
	    saveAsDialog.create();
	    saveAsDialog.setMessage("Save as ...");
	    saveAsDialog.open();
	    IPath path = saveAsDialog.getResult();
	    if (path != null) {
	      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	      if (file != null) {
	        ResourceSet resourceSet = editingDomain.getResourceSet();
	        Resource currentResource = resourceSet.getResources().get(0);
	        String currentExtension = currentResource.getURI().fileExtension();

	        URI newURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
	        String newExtension = newURI.fileExtension();

	        if (currentExtension.equals(newExtension)) {
	          currentResource.setURI(newURI);
	        } else {
	          Resource newResource = resourceSet.createResource(newURI);
	          newResource.getContents().addAll(currentResource.getContents());
	          resourceSet.getResources().remove(0);
	          resourceSet.getResources().move(0, newResource);
	        }

	        IFileEditorInput modelFile = new FileEditorInput(file);
	        setInputWithNotify(modelFile);
	        setPartName(file.getName());
	        doSave(new NullProgressMonitor());
	      }
	    }
	}

	@Override
	public final void init(final IEditorSite site, final IEditorInput input)
			throws PartInitException {

		setSite(site);
		setInputWithNotify(input);
		setPartName(input.getName());
		
		resourceManager = new ResourceManager(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceManager, 
				IResourceChangeEvent.POST_CHANGE);
		
//		site.setSelectionProvider(new SelectionProvider());
//		site.getPage().addPartListener(partListener);
		
		getSite().getPage().addPartListener(partListener);
		
		createModel();
	}

	@Override
	public final boolean isDirty() {
		return controller.isSaveNeeded();
	}

	@Override
	public final boolean isSaveAsAllowed() {
		return true ;
	}

	@Override
	public final void createPartControl(final Composite parent) {
		GridLayout parentLayout = new GridLayout(1, false);
		parentLayout.verticalSpacing = 2;
		parentLayout.horizontalSpacing = 2;
		parent.setLayout(parentLayout);
		
//		createParserSelectionPanel(parent);
		
		sashForm = new SashForm(parent, SWT.SMOOTH);
		sashForm.setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createLeftPanel(sashForm);
		createRightPanel(sashForm);
	}
	
	private void createLeftPanel(final SashForm sashForm) {
		// layout
		
		Group ecoreGroup = new Group(sashForm, SWT.NONE);
		ecoreGroup.setText("Ecore");
		ecoreGroup.setLayout(new GridLayout(1, false));
		
		Composite annotationPanel = new Composite(ecoreGroup, SWT.NONE);
		annotationPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		annotationPanel.setLayout(new GridLayout(2, false));
		
		Label annotationLabel = new Label(annotationPanel, SWT.NONE);
		annotationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		annotationLabel.setText("Annotations:");
		
		final Combo annotationCombo = new Combo(annotationPanel, SWT.READ_ONLY);
		annotationCombo.setItems(new String[] {"Node", "Parser"});
		annotationCombo.select(NODE_SELECTION);
		annotationCombo.setVisibleItemCount(2);		
		annotationCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		annotationCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (leftStackLayout != null) {
					int index = annotationCombo.getSelectionIndex();
					if (index == NODE_SELECTION) {
						leftStackLayout.topControl = nodePanel;
					} else {
						leftStackLayout.topControl = parserPanel;
					}
					leftButtonPanel.layout();
				}
			};
		});
		
		
		Composite leftViewComposite = new Composite(ecoreGroup, SWT.NONE);
		leftViewComposite.setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		leftViewComposite.setLayout(new TreeColumnLayout());
		
		leftTreeViewer = new TreeViewer(leftViewComposite, SWT.BORDER);
		leftTreeViewer.setContentProvider(contentProvider);
		leftTreeViewer.setLabelProvider(labelProvider);
		leftTreeViewer.setInput(editingDomain.getResourceSet());
		leftTreeViewer.expandToLevel(ECLASS_LEVEL);
		leftTreeViewer.setSelection(
				new StructuredSelection(controller.getResource(0)), true);
		getSite().setSelectionProvider(leftTreeViewer);
		
		leftButtonPanel = new Composite(ecoreGroup, SWT.NONE);
		leftButtonPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		leftStackLayout = new StackLayout();		
		leftButtonPanel.setLayout(leftStackLayout);
		
		nodePanel = new Composite(leftButtonPanel, SWT.NONE);
		nodePanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		leftStackLayout.topControl = nodePanel;
		
		final Button nodeButton = new Button(nodePanel, SWT.NONE);
		nodeButton.setEnabled(false);
		nodeButton.setText("Node");
		nodeButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.NODE));
		
		final Button infoButton = new Button(nodePanel, SWT.NONE);
		infoButton.setEnabled(false);
		infoButton.setText("Informational");
		infoButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.NODE_INFORMATIONAL));
		
		final Button compareButton = new Button(nodePanel, SWT.NONE);
		compareButton.setEnabled(false);
		compareButton.setText("Compare");
		compareButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.NODE_COMPARE));
		
		final Button childrenButton = new Button(nodePanel, SWT.NONE);
		childrenButton.setEnabled(false);
		childrenButton.setText("Children");
		childrenButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.NODE_CHILDREN));
		
		parserPanel = new Composite(leftButtonPanel, SWT.NONE);
		parserPanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		final Button parserButton = new Button(parserPanel, SWT.NONE);
		parserButton.setEnabled(false);
		parserButton.setText("Parser");
		parserButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.PARSER));
		
		final Button astButton = new Button(parserPanel, SWT.NONE);
		astButton.setEnabled(false);
		astButton.setText("AST");
		astButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.PARSER_AST));
		
		final Button initButton = new Button(parserPanel, SWT.NONE);
		initButton.setEnabled(false);
		initButton.setText("Initialize");
		initButton.addSelectionListener(new AddAnnotationSelectionAdapter(
				this, EcoreAnnotation.PARSER_INIT));		
		
		// let the tree viewer change the button states
		
		leftTreeViewer.addSelectionChangedListener(
				new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(final Object object) {			
				
				boolean isEClass = object instanceof EClass;
				
				boolean hasNodeAnnotation = isEClass
						&& EcoreAnnotation.NODE.isPresentAt((EClass) object);
				boolean hasParserAnnotation = isEClass
						&& EcoreAnnotation.PARSER.isPresentAt((EClass) object);
				
				nodeButton.setEnabled(isEClass && !hasNodeAnnotation);
				parserButton.setEnabled(isEClass && !hasParserAnnotation);
				
				infoButton.setEnabled(EcoreAnnotation.NODE_INFORMATIONAL.canSetTo(object));
				compareButton.setEnabled(EcoreAnnotation.NODE_COMPARE.canSetTo(object));
				childrenButton.setEnabled(EcoreAnnotation.NODE_CHILDREN.canSetTo(object));
				
				astButton.setEnabled(EcoreAnnotation.PARSER_AST.canSetTo(object));
				initButton.setEnabled(EcoreAnnotation.PARSER_INIT.canSetTo(object));
			}
		});
	}
	
	private void createRightPanel(final SashForm sashForm) {
		// layout 
		
		Group nodeGroup = new Group(sashForm, SWT.NONE);
		nodeGroup.setText("Annotations");
		nodeGroup.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(nodeGroup, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
				
		Composite rightViewComposite = new Composite(nodeGroup, SWT.NONE);
		rightViewComposite.setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		rightViewComposite.setLayout(new TreeColumnLayout());
				
		rightTreeViewer = new TreeViewer(rightViewComposite, SWT.BORDER);
		rightTreeViewer.setContentProvider(contentProvider);
		rightTreeViewer.setLabelProvider(labelProvider);
		rightTreeViewer.setFilters(new ViewerFilter[] {
				new AnnotationViewerFilter()
		});
		rightTreeViewer.setInput(editingDomain.getResourceSet());
		rightTreeViewer.expandToLevel(ECLASS_LEVEL);
		rightTreeViewer.setSelection(
				new StructuredSelection(controller.getResource(0)), true);
//		new AdapterFactoryTreeEditor(rightTreeViewer.getTree(), adapterFactory);
		
		Composite rightButtonBar = new Composite(nodeGroup, SWT.NONE);
		rightButtonBar.setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FillLayout rightButtonBarLayout = new FillLayout(SWT.HORIZONTAL);
		rightButtonBarLayout.spacing = 5;
		rightButtonBar.setLayout(rightButtonBarLayout);
		
		final Button removeButton = new Button(rightButtonBar, SWT.NONE);
		removeButton.setEnabled(false);
		removeButton.setText("Remove");
		
		sashForm.setWeights(new int[] {1, 1});
		
		// behavior

		rightTreeViewer.addSelectionChangedListener(
				new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(final Object object) {
				boolean isRTTAnnotation = false;
				
				if (object instanceof EAnnotation) {
					EAnnotation annotation = (EAnnotation) object;
					isRTTAnnotation = 
							(EcoreAnnotation.convert(annotation) != null);
				}
				
				removeButton.setEnabled(isRTTAnnotation);
			}
		});
		
		removeButton.addSelectionListener(
				new RemoveAnnotationSelectionAdapter(this));
	}	

	private void createModel() {
		editingDomain.getResourceSet().getURIConverter().getURIMap().putAll(
				EcorePlugin.computePlatformURIMap());		
		createModelGen();
	}
	

	private void createModelGen() {
		URI resourceURI = EditUIUtil.getURI(getEditorInput());
	    Resource resource = null;
	    
	    try {
	      // Load the resource through the editing domain.
	      resource = editingDomain.getResourceSet().getResource(resourceURI, true);
	    } catch (Exception e) {
	      resource = editingDomain.getResourceSet().getResource(resourceURI, false);
	    }
	    
	    if (resource == null) {
	    	throw new RuntimeException("Could not load resource");
	    }
	}

	@Override
	public final void setFocus() {
		leftTreeViewer.getTree().setFocus();		
	}
	
	@Override
	public final void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				resourceManager);
		getSite().getPage().removePartListener(partListener);
		
		if (contentOutlinePage != null) {
			contentOutlinePage.dispose();
			contentOutlinePage = null;
		}
		
		if (editingDomain != null) {
			editingDomain.getCommandStack().removeCommandStackListener(this);
			editingDomain = null;
		}
		
		if (adapterFactory != null) {
			adapterFactory.dispose();
			adapterFactory = null;
		}
		
		if (contentProvider != null) {
			contentProvider.dispose();
			contentProvider = null;
		}
		
		if (labelProvider != null) {
			labelProvider.dispose();
			labelProvider = null;
		}	
		
		super.dispose();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public final Object getAdapter(final Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			contentOutlinePage.setInput(leftTreeViewer.getInput());
			return contentOutlinePage;
		}	

		return super.getAdapter(adapter);
	}

	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	protected final void setParserRelatedText(
			final Text textElement, final EObject object) {
		
		if (object != null) {
			textElement.setText(labelProvider.getText(object));
		} else {
			textElement.setText("None");
		}		
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		
		leftTreeViewer.refresh();
		rightTreeViewer.refresh();	
	}
}
