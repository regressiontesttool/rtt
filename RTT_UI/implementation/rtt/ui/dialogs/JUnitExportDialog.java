package rtt.ui.dialogs;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import rtt.ui.content.main.ProjectContent;
import rtt.ui.model.RttProject;

/**
 * @author C. BŸrger
 */
public class JUnitExportDialog extends TitleAreaDialog {
	private Text adapterClassText;
	private Button overwriteButton;
	private Color red;
	
	private RttProject project;
	
	private IPackageFragment adapterPackage;
	private String adapterClass;
	
	public JUnitExportDialog(Shell parentShell, ProjectContent projectContent) {
		super(parentShell);
		setHelpAvailable(false);
		setShellStyle(SWT.DIALOG_TRIM);
		red = new Color(null, 255, 150, 150);
		this.project = projectContent.getProject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		red.dispose();
		super.finalize();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		super.setMessage("Generate a JUnit test suite that executes the selected RTT test suite.",
				IMessageProvider.INFORMATION);
		super.setTitle("Generate JUnit Adapter ...");
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label packageLabel = new Label(container, SWT.NONE);
		packageLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		packageLabel.setText("Package:");
		
		final Text adapterPackageText = new Text(container, SWT.BORDER);
		adapterPackageText.setEditable(false);
		adapterPackageText.setText("");
		adapterPackageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Shell parentShell = this.getParentShell();
		Button btnSelectPackage = new Button(container, SWT.NONE);
		btnSelectPackage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelectPackage.setText("Find ...");
		btnSelectPackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SelectionDialog createPackageDialog =
							JavaUI.createPackageDialog(parentShell, project.getJavaProject(), 0);
					if (createPackageDialog.open() == Dialog.OK) {
						adapterPackage = (IPackageFragment)createPackageDialog.getResult()[0];
						adapterPackageText.setText(adapterPackage.getElementName().equals("") ?
								"(default package)" :
								adapterPackage.getElementName());
						setOkButtonEnabled(true);
					}
				} catch (JavaModelException e1) {
					throw new RuntimeException("RTT Plugin (JUnit export): Cannot open package selection dialog.");
				}
			}
		});
		
		Label classLabel = new Label(container, SWT.NONE);
		classLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		classLabel.setText("Class:");
		
		adapterClassText = new Text(container, SWT.BORDER);
		adapterClassText.setBackground(red);
		adapterClassText.setText("");
		adapterClassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		adapterClassText.addModifyListener(
				new ModifyListener() {
					@Override public void modifyText(ModifyEvent e) {setOkButtonEnabled(true);}
				});
		
		overwriteButton = new Button(container, SWT.CHECK);
		overwriteButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		overwriteButton.setAlignment(SWT.CENTER);
		overwriteButton.setText("Overwrite");
		overwriteButton.setSelection(false);
		overwriteButton.addSelectionListener(
				new SelectionAdapter() {
					@Override public void widgetSelected(SelectionEvent e) {setOkButtonEnabled(true);}
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
	
	public void setOkButtonEnabled(boolean enable) { // Handle invalid selections (enable OK button only if valid)
		final String adapterClassName = adapterClassText.getText().trim();
		boolean error = true;
		if (project.getActiveConfiguration() == null) {
			adapterClassText.setBackground(red);
			setErrorMessage("Cannot generate JUnit adapter - no active configuration.");
		} else if (!isJavaClassName(adapterClassName)) {
			adapterClassText.setBackground(red);
			setErrorMessage("Please specify a valid class name for the JUnit adapter to generate.");
		} else {
			if (adapterPackage == null) {
				adapterClassText.setBackground(red);
				setErrorMessage("Please specify a package for the JUnit adapter to generate.");
			} else if (adapterPackage.getCompilationUnit(adapterClassName + ".java").exists() &&
					!overwriteButton.getSelection()) {
				adapterClassText.setBackground(red);
				setErrorMessage("A class with the given name already exists.");
			} else {
				adapterClassText.setBackground(null);
				setErrorMessage(null);
				error = false;
			}
		}
		Button okButton = this.getButton(OK);
		if (okButton != null) {
			getButton(OK).setEnabled(enable && !error);
		}
	}
	
	private boolean isJavaClassName(String name) {
		CharacterIterator iter = new StringCharacterIterator(name);
		char c = iter.first();
		if (c == CharacterIterator.DONE)
			return false;
		if (!Character.isJavaIdentifierStart(c) && !Character.isIdentifierIgnorable(c))
			return false;
		for (c = iter.next(); c != CharacterIterator.DONE; c = iter.next()) {
			if (!Character.isJavaIdentifierPart(c) && !Character.isIdentifierIgnorable(c))
				return false;
		}
		return true;
	}
	
	@Override
	protected void okPressed() {
		adapterClass = adapterClassText.getText().trim();
		super.okPressed();
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 200);
	}
	
	public String getAdapterClass() {
		return adapterClass;
	}
	
	public IPackageFragment getAdaperPackage() {
		return adapterPackage;
	}
}
