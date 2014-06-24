package rtt.annotation.editor.ui;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
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
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.importer.Importer;
import rtt.annotation.editor.importer.asm.ASMImporter;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.rules.Annotation;
import rtt.annotation.editor.rules.RuleEngine;
import rtt.annotation.editor.ui.viewer.util.ClassElementColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.ClassElementContentProvider;
import rtt.annotation.editor.ui.viewer.util.ClassModelColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.ClassModelContentProvider;
import rtt.annotation.editor.ui.viewer.util.ModelElementSelectionAdapter;
import rtt.annotation.editor.ui.viewer.util.ModelElementSelectionChangedListener;
import rtt.annotation.editor.ui.viewer.util.PropertyColumnLabelProvider;
import rtt.annotation.editor.ui.viewer.util.PropertyContentProvider;
import rtt.annotation.editor.util.StatusFactory;

public class AnnotationEditor extends EditorPart {

	private static final int MIN_COLUMN_WIDTH = 200;

	private static final int SECOND_LEVEL = 2;
	
	private TreeViewer propertyViewer;
	private TreeViewer elementViewer;
	private TreeViewer nodeViewer;
	
	private ClassModel model;
	private Button setNodeButton;

	private Button compareAnnotationButton;

	private Button informationalAnnotationButton;

	private Button removeAnnotationButton;

	private Button removeNodeButton;

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
		
		createNodeButtons(nodeButtonsComposite);		
	}
	
	private void createNodeViewer(Composite viewerComposite) {		
		nodeViewer = new TreeViewer(viewerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		nodeViewer.setContentProvider(new ClassModelContentProvider());
		
		nodeViewer.addSelectionChangedListener(new ModelElementSelectionChangedListener() {
			
			@Override
			protected void handleSelection(Object selectedObject) {
				setNodeButton.setEnabled(false);
				removeNodeButton.setEnabled(false);
				if (selectedObject instanceof ClassElement) {
					ClassElement element = (ClassElement) selectedObject;
					setNodeButton.setEnabled(RuleEngine.canApply(Annotation.NODE, element));
					removeNodeButton.setEnabled(RuleEngine.canApply(Annotation.NONE, element));
					
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
	
	private void createNodeButtons(Composite composite) {
		setNodeButton = new Button(composite, SWT.NONE);
		setNodeButton.setEnabled(false);
		setNodeButton.setText("Set Node");
		setNodeButton.addSelectionListener(new ModelElementSelectionAdapter(nodeViewer) {
			
			@Override
			protected void handleElement(ModelElement<?> selectedObject) {
				if (selectedObject instanceof Annotatable<?>) {
					ControllerRegistry.apply(Annotation.NODE, (Annotatable<?>) selectedObject);
					nodeViewer.refresh();
				}
			}
		});
		
		removeNodeButton = new Button(composite, SWT.NONE);
		removeNodeButton.setEnabled(false);
		removeNodeButton.setText("Remove Node");
		removeNodeButton.addSelectionListener(new ModelElementSelectionAdapter(nodeViewer) {
			
			@Override
			protected void handleElement(ModelElement<?> selectedObject) {
				if (selectedObject instanceof Annotatable<?>) {
					ControllerRegistry.apply(Annotation.NONE, (Annotatable<?>) selectedObject);
					nodeViewer.refresh();
				}
			}
		});
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
		
		elementViewer.addSelectionChangedListener(new ModelElementSelectionChangedListener() {
			
			@Override
			protected void handleSelection(Object selectedObject) {
				compareAnnotationButton.setEnabled(false);
				informationalAnnotationButton.setEnabled(false);
				removeAnnotationButton.setEnabled(false);
				
				if (selectedObject instanceof Annotatable<?>) {
					Annotatable<?> annotatable = (Annotatable<?>) selectedObject;
					
					compareAnnotationButton.setEnabled(RuleEngine.canApply(Annotation.COMPARE, annotatable));
					informationalAnnotationButton.setEnabled(RuleEngine.canApply(Annotation.INFORMATIONAL, annotatable));
					removeAnnotationButton.setEnabled(RuleEngine.canApply(Annotation.NONE, annotatable));
				}
				
				propertyViewer.setInput(selectedObject);
				propertyViewer.getControl().setEnabled(true);
				propertyViewer.expandToLevel(SECOND_LEVEL);
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
		compareAnnotationButton = new Button(composite, SWT.NONE);
		compareAnnotationButton.setEnabled(false);
		compareAnnotationButton.setText("Compare");
		compareAnnotationButton.addSelectionListener(new ModelElementSelectionAdapter(elementViewer) {
			
			@Override
			protected void handleElement(ModelElement<?> selectedObject) {
				if (selectedObject instanceof Annotatable<?>) {
					ControllerRegistry.apply(Annotation.COMPARE, (Annotatable<?>) selectedObject);
					elementViewer.refresh();
				}
			}
		});
		
		informationalAnnotationButton = new Button(composite, SWT.NONE);
		informationalAnnotationButton.setEnabled(false);
		informationalAnnotationButton.setText("Informational");
		informationalAnnotationButton.addSelectionListener(new ModelElementSelectionAdapter(elementViewer) {
			
			@Override
			protected void handleElement(ModelElement<?> selectedObject) {
				if (selectedObject instanceof Annotatable<?>) {
					ControllerRegistry.apply(Annotation.INFORMATIONAL, (Annotatable<?>) selectedObject);
					elementViewer.refresh();
				}
			}
		});
		
		removeAnnotationButton = new Button(composite, SWT.NONE);
		removeAnnotationButton.setEnabled(false);
		removeAnnotationButton.setText("Remove");
		removeAnnotationButton.addSelectionListener(new ModelElementSelectionAdapter(elementViewer) {
			
			@Override
			protected void handleElement(ModelElement<?> selectedObject) {
				if (selectedObject instanceof Annotatable<?>) {
					ControllerRegistry.apply(Annotation.NONE, (Annotatable<?>) selectedObject);
					elementViewer.refresh();
				}
			}
		});
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