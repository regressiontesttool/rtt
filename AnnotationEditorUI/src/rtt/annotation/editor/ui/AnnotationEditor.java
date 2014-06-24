package rtt.annotation.editor.ui;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import rtt.annotation.editor.importer.Importer;
import rtt.annotation.editor.importer.asm.ASMImporter;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.ui.viewer.util.ClassElementColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider;
import rtt.annotation.editor.ui.viewer.util.ClassModelColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.ClassModelContentProvider;
import rtt.annotation.editor.ui.viewer.util.PropertyColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.PropertyContentProvider;
import rtt.annotation.editor.ui.viewer.util.SelectionChangedAdapter;
import rtt.annotation.editor.util.StatusFactory;

public class AnnotationEditor extends EditorPart {

	private final class NodeFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof Annotatable<?>) {
				return ((Annotatable<?>) element).getAnnotation() == Annotation.NODE;
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
			Object selectedObject = getSelection(viewer);
			if (selectedObject instanceof Annotatable<?>) {
				ControllerRegistry.apply(annotation, (Annotatable<?>) selectedObject);
				viewer.setSelection(new StructuredSelection(selectedObject), true);
				viewer.refresh();
			}
		}
		
		private Object getSelection(Viewer viewer) {
			ISelection selection = viewer.getSelection();
			
			if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
				return ((IStructuredSelection) selection).getFirstElement();
			}

			return null;
		}
	}

	private static final int MIN_COLUMN_WIDTH = 200;

	private static final int SECOND_LEVEL = 2;
	
	private TreeViewer propertyViewer;
	private TreeViewer elementViewer;
	private TreeViewer nodeViewer;
	
	private ViewerFilter nodeFilter;
	
	private ClassModel model;
	
	private Button setNodeButton;
	private Button setCompareButton;
	private Button setInformationalButton;
	private Button removeAnnotationButton;
	private Button removeNodeButton;
	private Button filterButton;

	public AnnotationEditor() {
		nodeFilter = new NodeFilter();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

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
			IFile inputFile = fileInput.getFile();
			
			if (inputFile == null) {
				throw new PartInitException(
						StatusFactory.createError("Input file was null."));
			}
			
			setPartName(getPartName() + " - " + inputFile.getName());
			
			try {
				Importer importer = new ASMImporter();
				model = importer.importModel(inputFile.getLocation().toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		super.setSite(site);
		super.setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
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
		nodeViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		nodeViewer.setContentProvider(new ClassModelContentProvider());
		
		nodeViewer.addSelectionChangedListener(new SelectionChangedAdapter() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {				
				Object selectedObject = getSelection(event);
				
				setNodeButton.setEnabled(false);
				removeNodeButton.setEnabled(false);	
				
				elementViewer.getControl().setEnabled(selectedObject != null);
				elementViewer.setInput(selectedObject);
				elementViewer.expandToLevel(SECOND_LEVEL);
				
				propertyViewer.getControl().setEnabled(selectedObject != null);
				propertyViewer.setInput(selectedObject);
				propertyViewer.expandToLevel(SECOND_LEVEL);					
				
				if (selectedObject instanceof Annotatable<?>) {
					Annotatable<?> element = (Annotatable<?>) selectedObject;
					setNodeButton.setEnabled(ControllerRegistry.canApply(Annotation.NODE, element));
					removeNodeButton.setEnabled(ControllerRegistry.canApply(Annotation.NONE, element));					
				}
			}
		});
		
		Tree nodeTree = nodeViewer.getTree();
		nodeTree.setHeaderVisible(true);
		nodeTree.setLinesVisible(true);
		
		TreeColumnLayout columnLayout = new TreeColumnLayout();
		viewerComposite.setLayout(columnLayout);
		
		TreeViewerColumn nodeViewerColumn = new TreeViewerColumn(nodeViewer, SWT.NONE);
		nodeViewerColumn.setLabelProvider(new ClassModelColumnLabelProvider());
		
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
		elementViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		elementViewer.setContentProvider(new ClassElementContentProvider());
		
		elementViewer.addSelectionChangedListener(new SelectionChangedAdapter() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setCompareButton.setEnabled(false);
				setInformationalButton.setEnabled(false);
				removeAnnotationButton.setEnabled(false);
				
				Object selectedObject = getSelection(event);
				
				propertyViewer.getControl().setEnabled(selectedObject != null);
				propertyViewer.setInput(selectedObject);				
				propertyViewer.expandToLevel(SECOND_LEVEL);
				
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
		nameViewerColumn.setLabelProvider(new ClassElementColumnLabelProvider(ClassElementColumnLabelProvider.DESCRIPTION_COLUMN));
		TreeColumn nameColumn = nameViewerColumn.getColumn();
		tcl_elementViewerComposite.setColumnData(nameColumn, new ColumnWeightData(1, MIN_COLUMN_WIDTH, true));
		nameColumn.setText("Name");
		
		TreeViewerColumn typeViewerColumn = new TreeViewerColumn(elementViewer, SWT.NONE);
		typeViewerColumn.setLabelProvider(new ClassElementColumnLabelProvider(ClassElementColumnLabelProvider.TYPE_COLUMN));
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
		propertyViewer = new TreeViewer(propertyViewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		propertyViewer.setContentProvider(new PropertyContentProvider());
		
		Tree propertyTree = propertyViewer.getTree();
		propertyTree.setHeaderVisible(true);
		propertyTree.setLinesVisible(true);		
		propertyTree.setEnabled(false);
		
		TreeColumnLayout tcl_propertyViewerComposite = new TreeColumnLayout();
		propertyViewerComposite.setLayout(tcl_propertyViewerComposite);
		
		TreeViewerColumn descriptionViewerColumn = new TreeViewerColumn(propertyViewer, SWT.NONE);
		descriptionViewerColumn.setLabelProvider(new PropertyColumnLabelProvider(PropertyColumnLabelProvider.DESCRIPTION_COLUMN));
		TreeColumn descriptionColumn = descriptionViewerColumn.getColumn();
		tcl_propertyViewerComposite.setColumnData(descriptionColumn, new ColumnPixelData(150, true, true));
		descriptionColumn.setText("Description");
		
		TreeViewerColumn valueViewerColumn = new TreeViewerColumn(propertyViewer, SWT.NONE);
		valueViewerColumn.setLabelProvider(new PropertyColumnLabelProvider(PropertyColumnLabelProvider.VALUE_COLUMN));
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
		super.dispose();
	}
}