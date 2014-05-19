package rtt.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import rtt.core.archive.testsuite.Testcase;
import rtt.ui.content.testsuite.TestcaseContent;
import rtt.ui.viewer.ViewerUtils;

public class ParametersDialog extends TitleAreaDialog {
	
	private ListViewer listViewer;
	
	private String title;
	private String message;
	
	private List<String> parameterList;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ParametersDialog(Shell parentShell, TestcaseContent testcaseContent) {
		super(parentShell);
		setHelpAvailable(false);
		
		setShellStyle(SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE);
		
		parameterList = new ArrayList<String>();
		Testcase testcase = testcaseContent.getTestcase();
		
		if (testcase != null && testcase.getParameter() != null) {
			parameterList.addAll(testcase.getParameter());
		}		
	}
	
	@Override
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	@Override
	public void setMessage(String newMessage) {
		this.message = newMessage;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		super.setMessage(message, IMessageProvider.INFORMATION);
		super.setTitle(title);
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label parameterLabel = new Label(container, SWT.NONE);
		parameterLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		parameterLabel.setText("Parameters:");
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		listViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new LabelProvider());
		
		listViewer.setInput(parameterList);		
		
		Composite classpathComposite = new Composite(container, SWT.NONE);
		classpathComposite.setLayout(new GridLayout(1, true));
		
		Button addParameterButton = new Button(classpathComposite, SWT.NONE);
		addParameterButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		addParameterButton.setText("Add Parameter ...");
		addParameterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog(getParentShell(), "New parameter", "Enter new parameter ...", "", null);
				if (inputDialog.open() == Dialog.OK) {
					String value = inputDialog.getValue().trim();
					if (value != null && !value.equals("")) {
						parameterList.add(value);
						listViewer.setInput(parameterList);
						
						setOkButtonEnabled(true);
					}
				}
			}
		});
		
		Composite composite = new Composite(classpathComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		final Button removeButton = new Button(classpathComposite, SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		removeButton.setText("Remove");
		removeButton.setEnabled(false);
		
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedParameter = ViewerUtils.getSelection(
						listViewer.getSelection(), String.class);
				
				if (selectedParameter != null && !selectedParameter.isEmpty()) {
					parameterList.remove(selectedParameter);
					listViewer.setInput(parameterList);
					
					setOkButtonEnabled(true);
				}
				
				removeButton.setEnabled(false);
			}
		});
		
		Composite spacer = new Composite(container, SWT.NONE);
		GridData gd_spacer = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_spacer.heightHint = 30;
		spacer.setLayoutData(gd_spacer);	
		
        listViewer.addSelectionChangedListener(new ISelectionChangedListener() {			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				
				boolean enable = false;
				if (!selection.isEmpty() && selection.getFirstElement() instanceof String) {
					String selectionText = (String) selection.getFirstElement();
					if (!selectionText.equals("")) {
						enable = true;
					}
				}
				
				removeButton.setEnabled(enable);
			}
		});

		return area;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	public void setOkButtonEnabled(boolean enable) {
		Button okButton = this.getButton(OK);
		if (okButton != null) {
			// if name is empty, disable ok button. When not empty, disable only if lexer AND parser is empty
			getButton(OK).setEnabled(enable);
		}		
	}	

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 450);
	}

	public ListViewer getViewer() {
		return listViewer;
	}

	public List<String> getParameters() {
		return parameterList;
	}

}
