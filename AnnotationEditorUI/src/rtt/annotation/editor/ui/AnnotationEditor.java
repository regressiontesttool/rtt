package rtt.annotation.editor.ui;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.Exporter;
import rtt.annotation.editor.data.Importer;
import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.ui.viewer.util.ElementViewerItemProvider;
import rtt.annotation.editor.ui.viewer.util.ModelElementViewerItem;
import rtt.annotation.editor.ui.viewer.util.NodeViewerItemProvider;
import rtt.annotation.editor.ui.viewer.util.PropertyViewerItemProvider;
import rtt.annotation.editor.ui.viewer.util.ViewerItemProvider;
import rtt.annotation.editor.ui.viewer.util.ViewerSelectionUtil;
import rtt.annotation.editor.util.StatusFactory;

public class AnnotationEditor extends EditorPart {

	private final class NodeFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof Annotatable<?>) {
				return ((Annotatable<?>) element).getAnnotation() == Annotation.NODE;
			}
			
			if (element instanceof ModelElementViewerItem<?>) {
				ModelElementViewerItem<?> item = (ModelElementViewerItem<?>) element;
				if (item.getModelElement() instanceof Annotatable<?>) {
					Annotatable<?> annotatable = (Annotatable<?>) item.getModelElement();
					return annotatable.hasAnnotation();
				}
			}
			
			return true;
		}
	}

	private final class SetAnnotationSelectionAdapter extends SelectionAdapter {
		
		private Annotation annotation;
		private Viewer viewer;
		
		public SetAnnotationSelectionAdapter(Viewer viewer, Annotation annotation) {
			this.viewer = viewer;
			this.annotation = annotation;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			ModelElement<?> modelElement = ViewerSelectionUtil.getModelElement(viewer.getSelection());
			if (modelElement instanceof Annotatable<?>) {
				ControllerRegistry.apply(annotation, (Annotatable<?>) modelElement);
				
				// TODO implement improved change detection
				dirty = true;
				firePropertyChange(PROP_DIRTY);
			}
			
			nodeViewer.refresh();
			elementViewer.refresh();
			propertyViewer.refresh();
			
			viewer.setSelection(viewer.getSelection(), true);			
		}
	}

	private static final int MIN_COLUMN_WIDTH = 200;

	private static final int SECOND_LEVEL = 2;
	
	private TreeViewer propertyViewer;
	private TreeViewer elementViewer;
	private TreeViewer nodeViewer;
	
	private ViewerItemProvider nodeProvider;
	private ViewerItemProvider elementProvider;
	private ViewerItemProvider propertyProvider;
	
	private ViewerFilter nodeFilter;	
	
	private ClassModel model;
	
	private Button setNodeButton;
	private Button setCompareButton;
	private Button setInformationalButton;
	private Button removeAnnotationButton;
	private Button removeNodeButton;
	private Button filterButton;

	private boolean dirty = false;

	private IFile inputFile;

	public AnnotationEditor() {
		nodeFilter = new NodeFilter();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Exporter exporter = new ASMConverter();
		try {
			exporter.exportModel(model, inputFile.getLocationURI());
			dirty = false;
			firePropertyChange(PROP_DIRTY);
			
			inputFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
			
		} catch (IOException | CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;
			inputFile = fileInput.getFile();
			
			if (inputFile == null) {
				throw new PartInitException(
						StatusFactory.createError("Input file was null."));
			}
			
			setPartName(getPartName() + " - " + inputFile.getName());
			
			try {
				Importer importer = new ASMConverter();
				model = importer.importModel(inputFile.getLocationURI());
			} catch (IOException e) {
				e.printStackTrace();
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		
		SashForm verticalSash = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);
		
		createTopPanel(verticalSash);
		createBottomPanel(verticalSash);		

		verticalSash.setWeights(new int[] {2, 1});
	}
	
	private void createTopPanel(Composite composite) {
		
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
		filterButton.setText("Nodes only");
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
				ModelElement<?> element = ViewerSelectionUtil.getModelElement(event.getSelection());
				
				setNodeButton.setEnabled(false);
				removeNodeButton.setEnabled(false);	
				
				elementViewer.getControl().setEnabled(element != null);
				elementViewer.setInput(element);
				elementViewer.expandToLevel(SECOND_LEVEL);
				
				propertyViewer.getControl().setEnabled(element != null);
				propertyViewer.setInput(element);
				propertyViewer.expandToLevel(TreeViewer.ALL_LEVELS);					
				
				if (element instanceof Annotatable<?>) {
					Annotatable<?> annotatable = (Annotatable<?>) element;
					setNodeButton.setEnabled(ControllerRegistry.canApply(Annotation.NODE, annotatable));
					removeNodeButton.setEnabled(ControllerRegistry.canApply(Annotation.NONE, annotatable));					
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
		setNodeButton.setText("Node");
		setNodeButton.addSelectionListener(new SetAnnotationSelectionAdapter(nodeViewer, Annotation.NODE));
		
		removeNodeButton = new Button(composite, SWT.NONE);
		removeNodeButton.setEnabled(false);
		removeNodeButton.setText("Remove");
		removeNodeButton.addSelectionListener(new SetAnnotationSelectionAdapter(nodeViewer, Annotation.NONE));
	}
	
	private void createRightPanel(Composite composite) {
		
		Group elementsGroup = new Group(composite, SWT.NONE);
		elementsGroup.setText("Elements");
		elementsGroup.setLayout(new GridLayout(1, false));
		
		Composite elementViewerComposite = new Composite(elementsGroup, SWT.NONE);
		elementViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createElementViewer(elementViewerComposite);
		
		Composite annotationComposite = new Composite(elementsGroup, SWT.NONE);
		annotationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		annotationComposite.setBounds(0, 0, 64, 64);
		FillLayout fl_annotationComposite = new FillLayout(SWT.HORIZONTAL);
		fl_annotationComposite.spacing = 2;
		annotationComposite.setLayout(fl_annotationComposite);
		
		createAnnotationButtons(annotationComposite);
	}
	
	private void createElementViewer(Composite viewerComposite) {
		elementProvider = new ElementViewerItemProvider();
		
		elementViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		elementViewer.setContentProvider(elementProvider.getContentProvider());
		
		elementViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setCompareButton.setEnabled(false);
				setInformationalButton.setEnabled(false);
				removeAnnotationButton.setEnabled(false);
				
				ModelElement<?> selectedObject = ViewerSelectionUtil.getModelElement(event.getSelection());
				
				propertyViewer.getControl().setEnabled(selectedObject != null);
				propertyViewer.setInput(selectedObject);				
				propertyViewer.expandToLevel(TreeViewer.ALL_LEVELS);
				
				if (selectedObject instanceof Annotatable<?>) {
					Annotatable<?> annotatable = (Annotatable<?>) selectedObject;
					
					setCompareButton.setEnabled(ControllerRegistry.canApply(Annotation.COMPARE, annotatable));
					setInformationalButton.setEnabled(ControllerRegistry.canApply(Annotation.INFORMATIONAL, annotatable));
					removeAnnotationButton.setEnabled(ControllerRegistry.canApply(Annotation.NONE, annotatable));
				}
			}
		});
		
		Tree elementTree = elementViewer.getTree();
		elementTree.setHeaderVisible(true);
		elementTree.setLinesVisible(true);		
		elementTree.setEnabled(false);
		
		TreeColumnLayout tcl_elementViewerComposite = new TreeColumnLayout();
		viewerComposite.setLayout(tcl_elementViewerComposite);
		
		TreeViewerColumn nameViewerColumn = new TreeViewerColumn(elementViewer, SWT.NONE);
		nameViewerColumn.setLabelProvider(elementProvider.getLabelProvider(ViewerItemProvider.FIRST_COLUMN));
		TreeColumn nameColumn = nameViewerColumn.getColumn();
		tcl_elementViewerComposite.setColumnData(nameColumn, new ColumnWeightData(1, MIN_COLUMN_WIDTH, true));
		nameColumn.setText("Name");
		
		TreeViewerColumn typeViewerColumn = new TreeViewerColumn(elementViewer, SWT.NONE);
		typeViewerColumn.setLabelProvider(elementProvider.getLabelProvider(ViewerItemProvider.SECOND_COLUMN));
		TreeColumn typeColumn = typeViewerColumn.getColumn();
		tcl_elementViewerComposite.setColumnData(typeColumn, new ColumnWeightData(1, MIN_COLUMN_WIDTH, true));
		typeColumn.setText("Type");
	}
	
	private void createAnnotationButtons(Composite composite) {
		setCompareButton = new Button(composite, SWT.NONE);
		setCompareButton.setEnabled(false);
		setCompareButton.setText("Compare");
		setCompareButton.addSelectionListener(
				new SetAnnotationSelectionAdapter(elementViewer, Annotation.COMPARE));
		
		setInformationalButton = new Button(composite, SWT.NONE);
		setInformationalButton.setEnabled(false);
		setInformationalButton.setText("Informational");
		setInformationalButton.addSelectionListener(
				new SetAnnotationSelectionAdapter(elementViewer, Annotation.INFORMATIONAL));
		
		removeAnnotationButton = new Button(composite, SWT.NONE);
		removeAnnotationButton.setEnabled(false);
		removeAnnotationButton.setText("Remove");
		removeAnnotationButton.addSelectionListener(
				new SetAnnotationSelectionAdapter(elementViewer, Annotation.NONE));
	}
	
	private void createBottomPanel(Composite composite) {		
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
		
		Tree propertyTree = propertyViewer.getTree();
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

		if (nodeProvider != null) {
			nodeProvider.dispose();
			nodeProvider = null;
		}

		if (elementProvider != null) {
			elementProvider.dispose();
			elementProvider = null;
		}

		if (propertyProvider != null) {
			propertyProvider.dispose();
			propertyProvider = null;
		}
		
		super.dispose();
	}
}