package rtt.annotation.editor.ui;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.importer.ASMImporter;
import rtt.annotation.editor.model.importer.Importer;
import rtt.annotation.editor.ui.viewer.util.ClassViewerContentProvider;
import rtt.annotation.editor.ui.viewer.util.ModelElementLabelProvider;
import rtt.annotation.editor.util.StatusFactory;

public class AnnotationEditor extends EditorPart {

	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	private TreeViewer detailViewer;
	private TreeViewer elementViewer;
	private TreeViewer nodeViewer;
	
	private ClassModel model;

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
				model = importer.importModel(inputFile);
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
		
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		
		createLeftEditorPanel(sashForm);
		createRightEditorPanel(sashForm);	
		
		sashForm.setWeights(new int[] {1, 1});
	}

	private void createLeftEditorPanel(Composite parentComposite) {
		Composite mainComposite = new Composite(parentComposite, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		
		Group nodesGroup = new Group(mainComposite, SWT.NONE);
		nodesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		nodesGroup.setText("Nodes");
		nodesGroup.setLayout(new GridLayout(2, false));
		
		Label descriptionLabel = new Label(nodesGroup, SWT.WRAP);
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		descriptionLabel.setText("Displaying classes (and nodes)");
		
		Composite addClassesComposite = new Composite(nodesGroup, SWT.NONE);
		addClassesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		addClassesComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Button addClassButton = new Button(addClassesComposite, SWT.NONE);
		addClassButton.setText("Add Class");
		
		Composite nodeViewerComposite = new Composite(nodesGroup, SWT.NONE);
		nodeViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		nodeViewerComposite.setLayout(new TreeColumnLayout());
		
		createNodeViewer(nodeViewerComposite);
		
		Composite nodeButtonsComposite = new Composite(nodesGroup, SWT.NONE);
		FillLayout fl_nodeButtonsComposite = new FillLayout(SWT.HORIZONTAL);
		fl_nodeButtonsComposite.spacing = 2;
		nodeButtonsComposite.setLayout(fl_nodeButtonsComposite);
		nodeButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Button setNodeButton = new Button(nodeButtonsComposite, SWT.NONE);
		setNodeButton.setEnabled(false);
		setNodeButton.setText("Set Node");
		
		Button removeNodeButton = new Button(nodeButtonsComposite, SWT.NONE);
		removeNodeButton.setEnabled(false);
		removeNodeButton.setText("Remove Node");
	}
	
	private void createNodeViewer(Composite viewerComposite) {
		nodeViewer = new TreeViewer(viewerComposite, SWT.BORDER);
		nodeViewer.setContentProvider(new ClassViewerContentProvider());
		nodeViewer.setLabelProvider(new ModelElementLabelProvider());
		
		nodeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				elementViewer.setInput(event.getSelection());
				elementViewer.getControl().setEnabled(true);
			}
		});
		
		Tree nodeTree = nodeViewer.getTree();
		nodeTree.setHeaderVisible(true);
		nodeTree.setLinesVisible(true);		
		
		if (model != null) {
			nodeViewer.setInput(model);
		}
	}

	private void createRightEditorPanel(Composite parentComposite) {
		Composite rightMainComposite = new Composite(parentComposite, SWT.NONE);
		rightMainComposite.setLayout(new GridLayout(1, false));
		
		Group elementsGroup = new Group(rightMainComposite, SWT.NONE);
		elementsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		elementsGroup.setText("Elements");
		elementsGroup.setLayout(new GridLayout(1, false));
		
		Composite elementViewerComposite = new Composite(elementsGroup, SWT.NONE);
		elementViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		elementViewerComposite.setLayout(new TreeColumnLayout());
		
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
		
		Group detailsGroup = new Group(rightMainComposite, SWT.NONE);
		detailsGroup.setLayout(new GridLayout(1, false));
		detailsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		detailsGroup.setText("Details");
		
		Composite detailViewerComposite = new Composite(detailsGroup, SWT.NONE);
		detailViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		detailViewerComposite.setLayout(new TreeColumnLayout());
		
		createDetailViewer(detailViewerComposite);
	}

	private void createElementViewer(Composite viewerComposite) {
		elementViewer = new TreeViewer(viewerComposite, SWT.BORDER);
		elementViewer.setContentProvider(contentProvider);
		elementViewer.setLabelProvider(labelProvider);
		
		elementViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				detailViewer.setInput(event.getSelection());
				detailViewer.getControl().setEnabled(true);
			}
		});
		
		Tree elementTree = elementViewer.getTree();
		elementTree.setHeaderVisible(true);
		elementTree.setLinesVisible(true);		
		elementTree.setEnabled(false);		
	}
	
	private void createDetailViewer(Composite detailViewerComposite) {
		detailViewer = new TreeViewer(detailViewerComposite, SWT.BORDER);
		detailViewer.setContentProvider(contentProvider);
		detailViewer.setLabelProvider(labelProvider);
		
		Tree detailTree = detailViewer.getTree();
		detailTree.setHeaderVisible(true);
		detailTree.setLinesVisible(true);		
		detailTree.setEnabled(false);		
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