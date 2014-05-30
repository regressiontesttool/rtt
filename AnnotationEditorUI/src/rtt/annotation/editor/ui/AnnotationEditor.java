package rtt.annotation.editor.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.TreeColumnLayout;
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
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import rtt.annotation.ClassModel;
import rtt.annotation.ClassModelFactory;
import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.util.StatusFactory;

public class AnnotationEditor extends EditorPart {

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
			
			ClassModel model = ClassModelFactory.createModel(inputFile);
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
		
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		
		Composite leftSashComposite = new Composite(sashForm, SWT.NONE);
		leftSashComposite.setLayout(new GridLayout(1, false));
		
		Group nodesGroup = new Group(leftSashComposite, SWT.NONE);
		nodesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		nodesGroup.setText("Nodes");
		nodesGroup.setLayout(new GridLayout(2, false));
		
		Label classLabel = new Label(nodesGroup, SWT.WRAP);
		classLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		classLabel.setText("Displaying classes (and nodes)");
		
		Composite addClassesComposite = new Composite(nodesGroup, SWT.NONE);
		addClassesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		addClassesComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Button addClassButton = new Button(addClassesComposite, SWT.NONE);
		addClassButton.setText("Add Class");
		
		Composite nodeViewerComposite = new Composite(nodesGroup, SWT.NONE);
		nodeViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		nodeViewerComposite.setLayout(new TreeColumnLayout());
		
		TreeViewer treeViewer = new TreeViewer(nodeViewerComposite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		Composite nodeButtonsComposite = new Composite(nodesGroup, SWT.NONE);
		FillLayout fl_nodeButtonsComposite = new FillLayout(SWT.HORIZONTAL);
		fl_nodeButtonsComposite.spacing = 2;
		nodeButtonsComposite.setLayout(fl_nodeButtonsComposite);
		nodeButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Button setNodeButton = new Button(nodeButtonsComposite, SWT.NONE);
		setNodeButton.setText("Set Node");
		
		Button removeNodeButton = new Button(nodeButtonsComposite, SWT.NONE);
		removeNodeButton.setText("Remove Node");
		
		Composite rightSashComposite = new Composite(sashForm, SWT.NONE);
		rightSashComposite.setLayout(new GridLayout(1, false));
		
		Group elementsGroup = new Group(rightSashComposite, SWT.NONE);
		elementsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		elementsGroup.setText("Elements");
		elementsGroup.setLayout(new GridLayout(1, false));
		
		Composite elementViewerComposite = new Composite(elementsGroup, SWT.NONE);
		elementViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		elementViewerComposite.setLayout(new TreeColumnLayout());
		
		TreeViewer treeViewer_1 = new TreeViewer(elementViewerComposite, SWT.BORDER);
		Tree tree_1 = treeViewer_1.getTree();
		tree_1.setHeaderVisible(true);
		tree_1.setLinesVisible(true);
		
		Composite annotationComposite = new Composite(elementsGroup, SWT.NONE);
		annotationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		annotationComposite.setBounds(0, 0, 64, 64);
		FillLayout fl_annotationComposite = new FillLayout(SWT.HORIZONTAL);
		fl_annotationComposite.spacing = 2;
		annotationComposite.setLayout(fl_annotationComposite);
		
		Button compareAnnotationButton = new Button(annotationComposite, SWT.NONE);
		compareAnnotationButton.setText("Compare");
		
		Button informationalAnnotationButton = new Button(annotationComposite, SWT.NONE);
		informationalAnnotationButton.setText("Informational");
		
		Button removeAnnotationButton = new Button(annotationComposite, SWT.NONE);
		removeAnnotationButton.setBounds(0, 0, 75, 25);
		removeAnnotationButton.setText("Remove");
		
		Group detailsGroup = new Group(rightSashComposite, SWT.NONE);
		detailsGroup.setLayout(new GridLayout(1, false));
		detailsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		detailsGroup.setText("Details");
		
		Composite composite = new Composite(detailsGroup, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new TreeColumnLayout());
		
		TreeViewer treeViewer_2 = new TreeViewer(composite, SWT.BORDER);
		Tree tree_2 = treeViewer_2.getTree();
		tree_2.setHeaderVisible(true);
		tree_2.setLinesVisible(true);
		sashForm.setWeights(new int[] {1, 1});
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}