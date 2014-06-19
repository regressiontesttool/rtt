package rtt.annotation.editor.ui;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import rtt.annotation.editor.importer.Importer;
import rtt.annotation.editor.importer.asm.ASMImporter;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.ui.viewer.util.ClassElementColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider;
import rtt.annotation.editor.ui.viewer.util.ClassModelColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.ClassModelContentProvider;
import rtt.annotation.editor.ui.viewer.util.ModelElementSelectionChangedListener;
import rtt.annotation.editor.ui.viewer.util.PropertyColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.PropertyContentProvider;
import rtt.annotation.editor.ui.viewer.util.ClassElementColumnLabelProvider.ColumnKey;
import rtt.annotation.editor.util.StatusFactory;

public class AnnotationEditor extends EditorPart {

	private static final int SECOND_LEVEL = 2;
	
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	private TreeViewer propertyViewer;
	private TreeViewer elementViewer;
	private TreeViewer nodeViewer;
	
	private ClassModel model;
	private Button setNodeButton;

	public AnnotationEditor() {
		// TODO Auto-generated constructor stub
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
		
		if (contentProvider == null) {
			contentProvider = new BaseWorkbenchContentProvider();
		}
		
		if (labelProvider == null) {
			labelProvider = new WorkbenchLabelProvider();
		}		
		
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
		nodesGroup.setLayout(new GridLayout(2, false));
		
		Label descriptionLabel = new Label(nodesGroup, SWT.WRAP);
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		descriptionLabel.setText("Displaying classes (and nodes)");
		
		Composite filterComposite = new Composite(nodesGroup, SWT.NONE);
		filterComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		filterComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Button filterButton = new Button(filterComposite, SWT.CHECK);
		filterButton.setText("Nodes only");
		
		Composite nodeViewerComposite = new Composite(nodesGroup, SWT.NONE);
		nodeViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));	
		
		createNodeViewer(nodeViewerComposite);
		
		Composite nodeButtonsComposite = new Composite(nodesGroup, SWT.NONE);
		FillLayout fl_nodeButtonsComposite = new FillLayout(SWT.HORIZONTAL);
		fl_nodeButtonsComposite.spacing = 2;
		nodeButtonsComposite.setLayout(fl_nodeButtonsComposite);
		nodeButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		setNodeButton = new Button(nodeButtonsComposite, SWT.NONE);
		setNodeButton.setEnabled(false);
		setNodeButton.setText("Set Node");
		
		Button removeNodeButton = new Button(nodeButtonsComposite, SWT.NONE);
		removeNodeButton.setEnabled(false);
		removeNodeButton.setText("Remove Node");
	}
	
	private void createNodeViewer(Composite viewerComposite) {		
		nodeViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		nodeViewer.setContentProvider(new ClassModelContentProvider());
		
		nodeViewer.addSelectionChangedListener(new ModelElementSelectionChangedListener() {
			
			@Override
			protected void handleSelection(Object selectedObject) {
				setNodeButton.setEnabled(false);
				if (selectedObject instanceof ClassElement) {
					ClassElement element = (ClassElement) selectedObject;
					setNodeButton.setEnabled(!element.hasAnnotation());
					
					elementViewer.setInput(selectedObject);
					elementViewer.getControl().setEnabled(true);
					elementViewer.expandToLevel(SECOND_LEVEL);
				}
				
				propertyViewer.setInput(selectedObject);
				propertyViewer.getControl().setEnabled(true);
				propertyViewer.expandToLevel(SECOND_LEVEL);				
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
		
		Button compareAnnotationButton = new Button(annotationComposite, SWT.NONE);
		compareAnnotationButton.setEnabled(false);
		compareAnnotationButton.setText("Compare");
		
		Button informationalAnnotationButton = new Button(annotationComposite, SWT.NONE);
		informationalAnnotationButton.setEnabled(false);
		informationalAnnotationButton.setText("Informational");
		
		Button removeAnnotationButton = new Button(annotationComposite, SWT.NONE);
		removeAnnotationButton.setEnabled(false);
		removeAnnotationButton.setBounds(0, 0, 75, 25);
		removeAnnotationButton.setText("Remove");		
	}
	
	private void createElementViewer(Composite viewerComposite) {
		elementViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		elementViewer.setContentProvider(new ClassElementContentProvider());
		
		elementViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				propertyViewer.setInput(event.getSelection());
				propertyViewer.getControl().setEnabled(true);
			}
		});
		
		Tree elementTree = elementViewer.getTree();
		elementTree.setHeaderVisible(true);
		elementTree.setLinesVisible(true);		
		elementTree.setEnabled(false);
		
		TreeColumnLayout tcl_elementViewerComposite = new TreeColumnLayout();
		viewerComposite.setLayout(tcl_elementViewerComposite);
		
		TreeViewerColumn kindViewerColumn = new TreeViewerColumn(elementViewer, SWT.NONE);
		kindViewerColumn.setLabelProvider(new ClassElementColumnLabelProvider(ColumnKey.KIND_COLUMN));
		TreeColumn kindColumn = kindViewerColumn.getColumn();
		tcl_elementViewerComposite.setColumnData(kindColumn, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		kindColumn.setText("Kind");
		
		TreeViewerColumn nameViewerColumn = new TreeViewerColumn(elementViewer, SWT.NONE);
		nameViewerColumn.setLabelProvider(new ClassElementColumnLabelProvider(ColumnKey.NAME_COLUMN));
		TreeColumn nameColumn = nameViewerColumn.getColumn();
		tcl_elementViewerComposite.setColumnData(nameColumn, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		nameColumn.setText("Name");
		
		TreeViewerColumn typeViewerColumn = new TreeViewerColumn(elementViewer, SWT.NONE);
		typeViewerColumn.setLabelProvider(new ClassElementColumnLabelProvider(ColumnKey.TYPE_COLUMN));
		TreeColumn typeColumn = typeViewerColumn.getColumn();
		tcl_elementViewerComposite.setColumnData(typeColumn, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		typeColumn.setText("Type");
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
		if (labelProvider != null) {
			labelProvider.dispose();
			labelProvider = null;
		}
		
		if (contentProvider != null) {
			contentProvider.dispose();
			contentProvider = null;
		}
		
		super.dispose();
	}
}