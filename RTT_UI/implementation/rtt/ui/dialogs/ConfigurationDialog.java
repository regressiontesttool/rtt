package rtt.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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

import rtt.core.archive.configuration.Configuration;
import rtt.ui.dialogs.utils.ClassSelectionAdapter;
import rtt.ui.dialogs.utils.ResourceSelectionAdapter;
import rtt.ui.dialogs.utils.ResourceSelectionAdapter.DialogType;
import rtt.ui.model.RttProject;
import rtt.ui.viewer.ViewerUtils;

public class ConfigurationDialog extends TitleAreaDialog {
	
	private String title;
	private String message;
	
	private Text nameText;
	private Text lexerText;
	private Text parserText;
	private Button defaultButton;
	private ListViewer listViewer;
	private boolean nameEditable = true;
	
	private String configName = "";
	private String lexerName  = "";
	private String parserName = "";
	private List<String> cpEntries;
	private boolean isDefault = false;
	
	private RttProject project;
	private Color red;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfigurationDialog(Shell parentShell, RttProject project, Configuration config) {
		super(parentShell);
		setHelpAvailable(false);
		
		setShellStyle(SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE);
		red = new Color(getParentShell().getDisplay(), 255, 150, 150);
		
		this.project = project;
		
		configName = config.getName();
		lexerName = config.getLexerClass();
		parserName = config.getParserClass();
		cpEntries = new ArrayList<String>();
		
		if (config.getClasspath() != null && config.getClasspath().getPath()  != null) {
			cpEntries.addAll(config.getClasspath().getPath());
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
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		nameLabel.setText("Name:");
		
		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		nameText.setText(configName);
		nameText.addModifyListener(getModifyListener());
		nameText.setEnabled(nameEditable);
		
		defaultButton = new Button(container, SWT.CHECK);
		defaultButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		defaultButton.setAlignment(SWT.CENTER);
		defaultButton.setText("Default");
		defaultButton.setSelection(isDefault);
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setOkButtonEnabled(true);
			}
		});
		
		Label lexerLabel = new Label(container, SWT.NONE);
		lexerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lexerLabel.setText("Lexer class:");
		
		lexerText = new Text(container, SWT.BORDER);
		lexerText.setText(lexerName);
		lexerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lexerText.addModifyListener(getModifyListener());
		
		Button lexerButton = new Button(container, SWT.NONE);
		lexerButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lexerButton.setText("Find ...");
		lexerButton.addSelectionListener(new ClassSelectionAdapter(getParentShell(), lexerText, project.getSearchScope()));
		
		Label parserLabel = new Label(container, SWT.NONE);
		parserLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		parserLabel.setText("Parser class:");
		
		parserText = new Text(container, SWT.BORDER);
		parserText.setText(parserName);		
		parserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		parserText.addModifyListener(getModifyListener());
		
		Button parserButton = new Button(container, SWT.NONE);
		parserButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 0, 1));
		parserButton.setText("Find ...");
		parserButton.addSelectionListener(new ClassSelectionAdapter(getParentShell(), parserText, project.getSearchScope()));
		
		Label classpathLabel = new Label(container, SWT.NONE);
		classpathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		classpathLabel.setText("Classpath:");
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		listViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new LabelProvider());
		
		listViewer.setInput(cpEntries);
		
		Composite classpathComposite = new Composite(container, SWT.NONE);
		classpathComposite.setLayout(new GridLayout(1, true));
		
		Button btnAddFile = new Button(classpathComposite, SWT.NONE);
		btnAddFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddFile.setText("Add Files ...");
		btnAddFile.addSelectionListener(new ResourceSelectionAdapter(DialogType.RESOURCE, this));
		
		Button btnAddFolder = new Button(classpathComposite, SWT.NONE);
		btnAddFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddFolder.setText("Add Binary Folder ...");
		btnAddFolder.addSelectionListener(new ResourceSelectionAdapter(DialogType.CONTAINER, this));
		
		Composite composite = new Composite(classpathComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		final Button btnRemove = new Button(classpathComposite, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemove.setText("Remove");
		btnRemove.setEnabled(false);
		
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				String selectedString = ViewerUtils.getSelection(listViewer.getSelection(), String.class);
				if (selectedString != null && !selectedString.equals("")) {
					int oldIndex = cpEntries.indexOf(selectedString);
					
					cpEntries.remove(selectedString);
					listViewer.refresh();
					
					int size = cpEntries.size();					
					if (size == 0) {
						btnRemove.setEnabled(false);
					} else{
						int select = Math.min(oldIndex, size - 1);
						listViewer.setSelection(new StructuredSelection(cpEntries.get(select)));
					}	
					
					setOkButtonEnabled(true);
				}
			}
		});
		
		new Label(container, SWT.NONE);
		
		Label lblNoteToAdd = new Label(container, SWT.NONE);
		lblNoteToAdd.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNoteToAdd.setText("Note: To add Java archives (*.jar) use 'Add Files ...' ");
		new Label(container, SWT.NONE);
		
		Composite spacer = new Composite(container, SWT.NONE);
		GridData gd_spacer = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_spacer.heightHint = 30;
		spacer.setLayoutData(gd_spacer);	
		
        listViewer.addSelectionChangedListener(new ISelectionChangedListener() {			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				boolean enable = false;
				String selectedString = ViewerUtils.getSelection(event.getSelection(), String.class);
				if (selectedString != null && !selectedString.equals("")) {
					enable = true;
				}
				
				btnRemove.setEnabled(enable);
			}
		});

		return area;
	}
	
	private ModifyListener getModifyListener() {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {				
				setOkButtonEnabled(true);
			}
		};
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
			getButton(OK).setEnabled(enable && hasContent());
		}		
	}
	
	private boolean hasContent() {
		boolean nameHasContent = hasContent(nameText);
		boolean lexerHasContent = hasContent(lexerText);
		boolean parserHasContent = hasContent(parserText);
		
		if (nameHasContent && lexerHasContent && parserHasContent) {
			setErrorMessage(null);
		} else {
			setErrorMessage("Please check marked text box(es)");
		}
		
		return nameHasContent && (lexerHasContent || parserHasContent);			
	}
	
	private boolean hasContent(Text text) {
		String content = text.getText().trim();
		boolean hasContent = !content.equals("");
		
		if (hasContent) {
			text.setBackground(null);
		} else {
			text.setBackground(red);
		}
		
		return hasContent;
	}
	
	@Override
	protected void okPressed() {
		configName = nameText.getText().trim();
		lexerName = lexerText.getText().trim();
		parserName = parserText.getText().trim();
		isDefault = defaultButton.getSelection();	
		
		super.okPressed();
	}
	
	@Override
	protected void finalize() throws Throwable {
		red.dispose();
		super.finalize();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 450);
	}
	
	public String getConfigName() {
		return configName;
	}
	
	public String getLexerName() {
		return lexerName;
	}
	
	public String getParserName() {
		return parserName;
	}
	
	public List<String> getClasspathEntries() {
		return cpEntries;
	}
	
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public boolean isDefault() {
		return isDefault;
	}

	public RttProject getProject() {
		return project;
	}

	public ListViewer getViewer() {
		return listViewer;
	}

	public void setNameEditable(boolean nameEditable) {
		this.nameEditable = nameEditable;
	}
}
