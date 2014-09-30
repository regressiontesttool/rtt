package rtt.annotation.editor.ui;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.data.AnnotationExporter;
import rtt.annotation.editor.data.AnnotationImporter;
import rtt.annotation.editor.data.ModelReader;
import rtt.annotation.editor.data.ModelWriter;
import rtt.annotation.editor.data.asm.ASMClassModelManager;
import rtt.annotation.editor.data.jaxb.JAXBAnnotationManager;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.ui.viewer.provider.EditableViewerItem;
import rtt.annotation.editor.ui.viewer.provider.MemberViewerItemProvider;
import rtt.annotation.editor.ui.viewer.provider.ModelElementViewerItem;
import rtt.annotation.editor.ui.viewer.provider.NodeViewerItemProvider;
import rtt.annotation.editor.ui.viewer.provider.PropertyViewerItemProvider;
import rtt.annotation.editor.ui.viewer.provider.ViewerItemProvider;
import rtt.annotation.editor.ui.viewer.util.ViewerSelectionUtil;

/**
 * The annotation editor.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
public class AnnotationEditor extends EditorPart implements Observer {

	private final class NodeFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			Annotation annotation = null;
			if (element instanceof Annotatable) {
				annotation = ((Annotatable<?>) element).getAnnotation();
			} else if (element instanceof ModelElementViewerItem<?>) {
				ModelElementViewerItem<?> item = (ModelElementViewerItem<?>) element;
				if (item.getModelElement() instanceof Annotatable) {
					annotation = ((Annotatable<?>) element).getAnnotation();
				}
			}
			
			return annotation instanceof NodeAnnotation;
		}
	}

	private abstract class SetAnnotationSelectionAdapter 
		extends SelectionAdapter {
		
		private Mode mode;
		
		public SetAnnotationSelectionAdapter(Mode mode) {
			this.mode = mode;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			Viewer viewer = getViewer();
			ModelElement modelElement = ViewerSelectionUtil.
					getModelElement(viewer.getSelection());
			
			if (modelElement instanceof Annotatable<?>) {
				ControllerRegistry.execute(mode, 
						getAnnotation().getType(), (Annotatable<?>) modelElement);
			}
			
			nodeViewer.refresh();
			memberViewer.refresh();
			propertyViewer.refresh();
			
			viewer.setSelection(viewer.getSelection(), true);			
		}

		public abstract Viewer getViewer();
		public abstract AnnotationType getAnnotation();
	}

	private static final int MIN_COLUMN_WIDTH = 200;

	private static final int SECOND_LEVEL = 2;
	
	private TreeViewer propertyViewer;
	private TreeViewer memberViewer;
	private TreeViewer nodeViewer;
	
	private ViewerItemProvider nodeProvider;
	private ViewerItemProvider memberProvider;
	private ViewerItemProvider propertyProvider;
	
	private ViewerFilter nodeFilter;	
	
	private ClassModel model;
	private AnnotationType selectedAnnotation = AnnotationType.VALUE;
	
	private Button valueAnnotationButton;
	private Button initializeAnnotationButton;
	private Button setNodeButton;
	private Button setAnnotationButton;
	private Button removeAnnotationButton;
	private Button removeNodeButton;
	private Button filterButton;

	private boolean dirty = false;

	private IFile inputFile;	

	public AnnotationEditor() {
		nodeFilter = new NodeFilter();
	}
	
	public AnnotationType getSelectedAnnotation() {
		return selectedAnnotation;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		ModelWriter exporter = new ASMClassModelManager();
		URI fileLocation = inputFile.getLocationURI();
		
		try {
			exporter.exportModel(model, fileLocation);
			dirty = false;
			firePropertyChange(PROP_DIRTY);
			inputFile.refreshLocal(IResource.DEPTH_ZERO, monitor);			
		} catch (IOException | CoreException e) {
			AnnotationEditorPlugin.logException(
					"Could not save file: " + fileLocation, e);
		}		
	}

	@Override
	public void doSaveAs() {
//		if (Files.notExists(output, LinkOption.NOFOLLOW_LINKS)) {
//			Files.copy(origin, dest, StandardCopyOption.COPY_ATTRIBUTES);
//		}		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;
			inputFile = fileInput.getFile();
			
			if (inputFile == null) {
				throw new PartInitException("Input file was null.");
			}
			
			setPartName(getPartName() + " - " + inputFile.getName());
			
			try {
				ModelReader importer = new ASMClassModelManager();
				model = importer.importModel(inputFile.getLocationURI());
				model.addObserver(this);
			} catch (IOException e) {
				throw new PartInitException(
						"Could not read input file.", e);
			}
		}		
		
		super.setSite(site);
		super.setInput(input);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 2;
		gl_parent.marginHeight = 2;
		parent.setLayout(gl_parent);
		
		createAnnotationImportExportPanel(parent);	
		
		SashForm verticalSash = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);
		verticalSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createEditorPanel(verticalSash);
		createDetailsPanel(verticalSash);		

		verticalSash.setWeights(new int[] {2, 1});
	}
	
	private void createAnnotationImportExportPanel(Composite composite) {
		Group annotationGroup = new Group(composite, SWT.NONE);
		
		FillLayout fl_annotationGroup = new FillLayout(SWT.HORIZONTAL);
		fl_annotationGroup.marginHeight = 2;
		fl_annotationGroup.spacing = 7;
		fl_annotationGroup.marginWidth = 2;
		annotationGroup.setLayout(fl_annotationGroup);
		annotationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		annotationGroup.setText("Annotations");
		
		Button importButton = new Button(annotationGroup, SWT.NONE);
		importButton.setText("Import");
		importButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				AnnotationImporter importer = JAXBAnnotationManager.getInstance();
				FileDialog dialog = new FileDialog(getSite().getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] {importer.getExtension()});
				
				String result = dialog.open();
				if (result != null) {
					Path importPath = Paths.get(result);					
					importer.importAnnotations(model, importPath);
					
					if (nodeViewer != null) {
						nodeViewer.setInput(model);
					}
				}				
			}
		});
		
		Button exportButton = new Button(annotationGroup, SWT.NONE);
		exportButton.setText("Export");
		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AnnotationExporter exporter = JAXBAnnotationManager.getInstance();
				FileDialog dialog = new FileDialog(getSite().getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] {exporter.getExtension()});
				dialog.setOverwrite(true);
				
				String result = dialog.open();
				if (result != null) {
					Path exportPath = Paths.get(result);
					exporter.exportAnnotations(model, exportPath);
				}
			}
		});
	}
	
	private void createEditorPanel(Composite composite) {
		
		SashForm sashForm = new SashForm(composite, SWT.SMOOTH);
		
		createLeftPanel(sashForm);
		createRightPanel(sashForm);	
		
		sashForm.setWeights(new int[] {2, 3});
	}
	
	private void createLeftPanel(Composite composite) {
		
		Group nodesGroup = new Group(composite, SWT.NONE);
		nodesGroup.setText("Nodes");
		nodesGroup.setLayout(new GridLayout(1, false));
		
		Composite filterComposite = new Composite(nodesGroup, SWT.NONE);
		filterComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		filterComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		
		filterButton = new Button(filterComposite, SWT.CHECK);
		filterButton.setText("Show Nodes only");
		filterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nodeViewer.resetFilters();				
				if (filterButton.getSelection()) {
					nodeViewer.setFilters(new ViewerFilter[] { nodeFilter });
				}
			}
		});
		
		Composite nodeViewerComposite = new Composite(nodesGroup, SWT.NONE);
		nodeViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));	
		
		createNodeViewer(nodeViewerComposite);
		
		Composite nodeButtonsComposite = new Composite(nodesGroup, SWT.NONE);
		FillLayout fl_nodeButtonsComposite = new FillLayout(SWT.HORIZONTAL);
		fl_nodeButtonsComposite.spacing = 2;
		nodeButtonsComposite.setLayout(fl_nodeButtonsComposite);
		nodeButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		createNodeButtons(nodeButtonsComposite);		
	}
	
	private void createNodeViewer(Composite viewerComposite) {		
		nodeProvider = new NodeViewerItemProvider();		
		
		nodeViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		nodeViewer.setContentProvider(nodeProvider.getContentProvider());
		
		nodeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ModelElement element = ViewerSelectionUtil.getModelElement(event.getSelection());
				
				setNodeButton.setEnabled(false);
				removeNodeButton.setEnabled(false);
				
				if (element instanceof Annotatable) {
					Annotatable<?> annotatable = (Annotatable<?>) element;
					setNodeButton.setEnabled(ControllerRegistry.canExecute(
							Mode.SET, NodeAnnotation.class, annotatable));
					removeNodeButton.setEnabled(ControllerRegistry.canExecute(
							Mode.UNSET, NodeAnnotation.class, annotatable));					
				}
				
				propertyViewer.getControl().setEnabled(element != null);
				propertyViewer.setInput(element);
				propertyViewer.expandToLevel(TreeViewer.ALL_LEVELS);
				
				if (element instanceof ClassElement) {
					valueAnnotationButton.setEnabled(true);
					initializeAnnotationButton.setEnabled(true);	
					
					memberViewer.getControl().setEnabled(true);
					memberViewer.setInput(element);
					memberViewer.expandToLevel(SECOND_LEVEL);
				} else {
					valueAnnotationButton.setEnabled(false);
					initializeAnnotationButton.setEnabled(false);	
					
					memberViewer.getControl().setEnabled(false);
					memberViewer.setInput(null);
				}
			}
		});
		
		Tree nodeTree = nodeViewer.getTree();
		nodeTree.setHeaderVisible(true);
		nodeTree.setLinesVisible(true);
		
		TreeColumnLayout columnLayout = new TreeColumnLayout();
		viewerComposite.setLayout(columnLayout);
		
		TreeViewerColumn nodeViewerColumn = new TreeViewerColumn(nodeViewer, SWT.NONE);
		nodeViewerColumn.setLabelProvider(nodeProvider.getLabelProvider(ViewerItemProvider.FIRST_COLUMN));
		
		TreeColumn nodesColumn = nodeViewerColumn.getColumn();
		columnLayout.setColumnData(nodesColumn, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		nodesColumn.setText("Nodes");
		
		if (model != null) {
			nodeViewer.setInput(model);
			nodeViewer.expandToLevel(SECOND_LEVEL);
		}
	}
	
	private void createNodeButtons(Composite composite) {
		setNodeButton = new Button(composite, SWT.NONE);
		setNodeButton.setEnabled(false);
		setNodeButton.setText(AnnotationType.NODE.getName());
		setNodeButton.addSelectionListener(new SetAnnotationSelectionAdapter(Mode.SET) {
			@Override public Viewer getViewer() { return nodeViewer; }
			@Override public AnnotationType getAnnotation() { 
				return AnnotationType.NODE;
			}		
		});
		
		removeNodeButton = new Button(composite, SWT.NONE);
		removeNodeButton.setEnabled(false);
		removeNodeButton.setText("Remove");
		removeNodeButton.addSelectionListener(new SetAnnotationSelectionAdapter(Mode.UNSET) {
			@Override public Viewer getViewer() { return nodeViewer; }
			@Override public AnnotationType getAnnotation() { 
				return AnnotationType.NODE; 
			}
		});
	}
	
	private void createRightPanel(Composite composite) {
		
		Group membersGroup = new Group(composite, SWT.NONE);
		membersGroup.setText("Annotatable Members");
		membersGroup.setLayout(new GridLayout(1, false));
		
		Composite annotationSelectionComposite = new Composite(membersGroup, SWT.NONE);
		RowLayout rl_annotationSelectionComposite = new RowLayout(SWT.HORIZONTAL);
		rl_annotationSelectionComposite.spacing = 10;
		annotationSelectionComposite.setLayout(rl_annotationSelectionComposite);
		annotationSelectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		createAnnotationSelection(annotationSelectionComposite);
		
		Composite memberViewerComposite = new Composite(membersGroup, SWT.NONE);
		memberViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createMemberViewer(memberViewerComposite);
		
		Composite annotationComposite = new Composite(membersGroup, SWT.NONE);
		annotationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FillLayout fl_annotationComposite = new FillLayout(SWT.HORIZONTAL);
		fl_annotationComposite.spacing = 2;
		annotationComposite.setLayout(fl_annotationComposite);
		
		createAnnotationButtons(annotationComposite);
	}

	private void createAnnotationSelection(Composite annotationSelectionComposite) {
		Label annotationSelectionLabel = new Label(annotationSelectionComposite, SWT.NONE);
		annotationSelectionLabel.setText("Annotation:");
		
		valueAnnotationButton = new Button(annotationSelectionComposite, SWT.RADIO);
		valueAnnotationButton.setEnabled(false);
		valueAnnotationButton.setText(AnnotationType.VALUE.getName());
		valueAnnotationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAnnotation = AnnotationType.VALUE;
				memberViewer.setInput(memberViewer.getInput());
				memberViewer.expandToLevel(SECOND_LEVEL);
				
				setAnnotationButton.setText(selectedAnnotation.getName());
			}
		});
		valueAnnotationButton.setSelection(true);		
		
		initializeAnnotationButton = new Button(annotationSelectionComposite, SWT.RADIO);
		initializeAnnotationButton.setEnabled(false);
		initializeAnnotationButton.setText(AnnotationType.INITIALIZE.getName());
		initializeAnnotationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAnnotation = AnnotationType.INITIALIZE;
				memberViewer.setInput(memberViewer.getInput());
				memberViewer.expandToLevel(SECOND_LEVEL);
				
				setAnnotationButton.setText(selectedAnnotation.getName());
			}
		});
	}
	
	private void createMemberViewer(Composite viewerComposite) {
		memberProvider = new MemberViewerItemProvider(this);
		
		memberViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		memberViewer.setContentProvider(memberProvider.getContentProvider());
		
		memberViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setAnnotationButton.setEnabled(false);
				removeAnnotationButton.setEnabled(false);
				
				ModelElement selectedObject = ViewerSelectionUtil.
						getModelElement(event.getSelection());
				
				propertyViewer.getControl().setEnabled(selectedObject != null);
				propertyViewer.setInput(selectedObject);				
				propertyViewer.expandToLevel(TreeViewer.ALL_LEVELS);
				
				if (selectedObject instanceof Annotatable) {
					Annotatable<?> annotatable = (Annotatable<?>) selectedObject;
					setAnnotationButton.setEnabled(ControllerRegistry.canExecute(
							Mode.SET, selectedAnnotation.getType(), annotatable));
					removeAnnotationButton.setEnabled(ControllerRegistry.canExecute(
							Mode.UNSET,selectedAnnotation.getType(), annotatable));
				}
			}
		});
		
		Tree memberTree = memberViewer.getTree();
		memberTree.setHeaderVisible(true);
		memberTree.setLinesVisible(true);		
		memberTree.setEnabled(false);
		
		TreeColumnLayout tcl_memberViewerComposite = new TreeColumnLayout();
		viewerComposite.setLayout(tcl_memberViewerComposite);
		
		TreeViewerColumn nameViewerColumn = new TreeViewerColumn(memberViewer, SWT.NONE);
		nameViewerColumn.setLabelProvider(memberProvider.getLabelProvider(ViewerItemProvider.FIRST_COLUMN));
		TreeColumn nameColumn = nameViewerColumn.getColumn();
		tcl_memberViewerComposite.setColumnData(nameColumn, new ColumnWeightData(1, MIN_COLUMN_WIDTH, true));
		nameColumn.setText("Name");
		
		TreeViewerColumn typeViewerColumn = new TreeViewerColumn(memberViewer, SWT.NONE);
		typeViewerColumn.setLabelProvider(memberProvider.getLabelProvider(ViewerItemProvider.SECOND_COLUMN));
		TreeColumn typeColumn = typeViewerColumn.getColumn();
		tcl_memberViewerComposite.setColumnData(typeColumn, new ColumnWeightData(1, MIN_COLUMN_WIDTH, true));
		typeColumn.setText("Type");
	}
	
	private void createAnnotationButtons(Composite composite) {
		setAnnotationButton = new Button(composite, SWT.NONE);
		setAnnotationButton.setEnabled(false);
		setAnnotationButton.setText(selectedAnnotation.getName());
		setAnnotationButton.addSelectionListener(new SetAnnotationSelectionAdapter(Mode.SET) {
			@Override public Viewer getViewer() { return memberViewer; }
			@Override public AnnotationType getAnnotation() { 
				return selectedAnnotation; 
			}
		});
		
		removeAnnotationButton = new Button(composite, SWT.NONE);
		removeAnnotationButton.setEnabled(false);
		removeAnnotationButton.setText("Remove");
		removeAnnotationButton.addSelectionListener(new SetAnnotationSelectionAdapter(Mode.UNSET) {
			@Override public Viewer getViewer() { return memberViewer; }
			@Override public AnnotationType getAnnotation() { 
				return selectedAnnotation; 
			}
		});
	}
	
	private void createDetailsPanel(Composite composite) {		
		Group propertiesGroup = new Group(composite, SWT.NONE);
		propertiesGroup.setLayout(new GridLayout(1, false));
		propertiesGroup.setText("Details");
		
		Composite propertyViewerComposite = new Composite(propertiesGroup, SWT.NONE);
		propertyViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createPropertyViewer(propertyViewerComposite);		
	}	
	
	private void createPropertyViewer(Composite propertyViewerComposite) {
		propertyProvider = new PropertyViewerItemProvider();
		
		propertyViewer = new TreeViewer(propertyViewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		propertyViewer.setContentProvider(propertyProvider.getContentProvider());
		
		final Tree propertyTree = propertyViewer.getTree();
		propertyTree.setHeaderVisible(true);
		propertyTree.setLinesVisible(true);		
		propertyTree.setEnabled(false);
		
		TreeColumnLayout tcl_propertyViewerComposite = new TreeColumnLayout();
		propertyViewerComposite.setLayout(tcl_propertyViewerComposite);
		
		TreeViewerColumn descriptionViewerColumn = new TreeViewerColumn(propertyViewer, SWT.NONE);
		descriptionViewerColumn.setLabelProvider(propertyProvider.getLabelProvider(ViewerItemProvider.FIRST_COLUMN));
		TreeColumn descriptionColumn = descriptionViewerColumn.getColumn();
		tcl_propertyViewerComposite.setColumnData(descriptionColumn, new ColumnPixelData(150, true, true));
		descriptionColumn.setText("Description");
		
		TreeViewerColumn valueViewerColumn = new TreeViewerColumn(propertyViewer, SWT.NONE);
		valueViewerColumn.setLabelProvider(propertyProvider.getLabelProvider(ViewerItemProvider.SECOND_COLUMN));
		valueViewerColumn.setEditingSupport(new EditingSupport(propertyViewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((EditableViewerItem) element).setValue(value);
				propertyViewer.update(element, null);
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((EditableViewerItem) element).getValue();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return ((EditableViewerItem) element).getCellEditor(propertyTree);
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return element instanceof EditableViewerItem;
			}
		});
		TreeColumn valueColumn = valueViewerColumn.getColumn();
		tcl_propertyViewerComposite.setColumnData(valueColumn, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		valueColumn.setText("Value");
	}

	@Override
	public void setFocus() {
		nodeViewer.getControl().setFocus();
	}
	
	@Override
	public void dispose() {		
		if (model != null) {
			model.deleteObserver(this);
			model = null;
		}

		if (nodeProvider != null) {
			nodeProvider.dispose();
			nodeProvider = null;
		}

		if (memberProvider != null) {
			memberProvider.dispose();
			memberProvider = null;
		}

		if (propertyProvider != null) {
			propertyProvider.dispose();
			propertyProvider = null;
		}
		
		super.dispose();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		dirty = true;
		firePropertyChange(PROP_DIRTY);		
	}
}