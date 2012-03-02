package rtt.ui.dialogs;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.LexerClass;
import rtt.core.archive.configuration.ParserClass;
import rtt.core.archive.configuration.Path;
import rtt.ui.content.IContent;
import rtt.ui.content.configuration.ClasspathContent;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.EmptyContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.viewer.BaseContentLabelProvider;
import rtt.ui.viewer.BaseContentProvider;

public class ConfigurationDialog extends TitleAreaDialog {
	
	static final IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
	static final IRunnableContext context = PlatformUI.getWorkbench().getProgressService();
	
	private class ClassSelectionAdapter extends SelectionAdapter {		
		
		private Text text;
		private Shell shell;
		
		public ClassSelectionAdapter(Text text) {
			super();
			this.text = text;
			shell = getParentShell();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				SelectionDialog selectionDialog = JavaUI.createTypeDialog(shell,context,scope,
								IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES,
								false);
				
				if (selectionDialog.open() == Dialog.OK) {
					for (Object o : selectionDialog.getResult()) {
						if (o instanceof IType) {
							IType type = (IType) o;
							text.setText(type.getFullyQualifiedName());
						}						
					}
				}
			} catch (JavaModelException exception) {
				exception.printStackTrace();
			}		
		}
	}
	
	private class ResourceSelectionAdapter extends SelectionAdapter {
		private SelectionDialog dialog;
		
		public ResourceSelectionAdapter(SelectionDialog dialog) {
			this.dialog = dialog;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (dialog.open() == Dialog.OK) {
				for (Object o : dialog.getResult()) {
					Path path = new Path();
					
					if (o instanceof IResource) {
						IResource file = (IResource) o;
						
						path.setValue(file.getLocation().toPortableString());
						tempClasspath.getPath().add(path);
					}
					
					if (o instanceof IPath) {
						IPath folderPath = (IPath) o;
						path.setValue("." + folderPath.toPortableString());
						tempClasspath.getPath().add(path);
					}
				}			
				listViewer.setInput(new ClasspathContent(null, tempClasspath));
			}
		}
	}
	
	private Text nameText;
	private Text lexerText;
	private Text parserText;
	private Button defaultButton;
	private ListViewer listViewer;
	private Color red;
	
	private Configuration config;
	private Classpath tempClasspath;

	private String title;
	private String message;
	private boolean isDefault;	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfigurationDialog(Shell parentShell, ConfigurationContent content) {
		super(parentShell);
		initDialog(parentShell);
		
		this.title = "Edit configuration";
		this.message = "Modify an existing configuration ...";		
		
		config = content.getConfiguration();
		initTempClasspath(config);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public ConfigurationDialog(Shell parentShell, ProjectContent content) {
		super(parentShell);
		initDialog(parentShell);
		
		this.title = "New configuration";
		this.message = "Create a new configuration for testing ...";
		
		this.config = new Configuration();
		config.setName("myConfig");
		config.setClasspath(new Classpath());
		
		LexerClass lexer = new LexerClass();
		lexer.setValue("");
		config.setLexerClass(lexer);
		
		ParserClass parser = new ParserClass();
		parser.setValue("");
		config.setParserClass(parser);
		
		Classpath classpath = new Classpath();
		Path path = new Path();
		System.out.println(content.getProject().find("bin").toPortableString());
		path.setValue(content.getProject().find("bin").toPortableString());
		classpath.getPath().add(path);
		config.setClasspath(classpath);
		
		initTempClasspath(config);
	}
	
	private void initTempClasspath(Configuration config) {
		tempClasspath = new Classpath();
		if (config.getClasspath() != null) {
			for (Path path : config.getClasspath().getPath()) {
				if (path.getValue() != null) {
					Path newPath = new Path();
					newPath.setValue(path.getValue());
					tempClasspath.getPath().add(newPath);
				}
			}
		}
	}
	
	private void initDialog(Shell parentShell) {
		setShellStyle(SWT.DIALOG_TRIM);
		setHelpAvailable(false);
		red = new Color(null, 255, 150, 150);
	}
	
	@Override
	protected void finalize() throws Throwable {
		red.dispose();
		super.finalize();
	}
	
	private void addTextListener(final Text text) {
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (text.getText().trim().equals("")) {
					setErrorMessage("Please check marked text box(es)");
					text.setBackground(red);
					getButton(OK).setEnabled(false);
				} else {
					setErrorMessage(null);
					text.setBackground(null);
					getButton(OK).setEnabled(true);
				}
			}
		});
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(message);
		setTitle(title);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		nameLabel.setText("Name:");
		
		nameText = new Text(container, SWT.BORDER);
		nameText.setText(config.getName());
		GridData gd_nameText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_nameText.widthHint = 326;
		nameText.setLayoutData(gd_nameText);
		addTextListener(nameText);
		
		defaultButton = new Button(container, SWT.CHECK);
		defaultButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		defaultButton.setAlignment(SWT.CENTER);
		defaultButton.setText("Default");
		
		Label lexerLabel = new Label(container, SWT.NONE);
		lexerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lexerLabel.setText("Lexer class:");
		
		lexerText = new Text(container, SWT.BORDER);
		lexerText.setText(config.getLexerClass().getValue());
		lexerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addTextListener(lexerText);
		
		Button lexerButton = new Button(container, SWT.NONE);
		lexerButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lexerButton.setText("Find ...");
		lexerButton.addSelectionListener(new ClassSelectionAdapter(lexerText));
		
		Label parserLabel = new Label(container, SWT.NONE);
		parserLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		parserLabel.setText("Parser class:");
		
		parserText = new Text(container, SWT.BORDER);
		parserText.setText(config.getParserClass().getValue());		
		parserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addTextListener(parserText);
		
		Button parserButton = new Button(container, SWT.NONE);
		parserButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 0, 1));
		parserButton.setText("Find ...");
		parserButton.addSelectionListener(new ClassSelectionAdapter(parserText));
		
		Label classpathLabel = new Label(container, SWT.NONE);
		classpathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		classpathLabel.setText("Classpath:");
		
		listViewer = new ListViewer(container);
		listViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listViewer.setContentProvider(new BaseContentProvider());
		listViewer.setLabelProvider(new BaseContentLabelProvider());
		
		listViewer.setInput(new ClasspathContent(null, tempClasspath));		
		
		Composite classpathComposite = new Composite(container, SWT.NONE);
		classpathComposite.setLayout(new GridLayout(1, true));
		
		Button btnAddFile = new Button(classpathComposite, SWT.NONE);
		btnAddFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddFile.setText("Add file ...");
		btnAddFile.addSelectionListener(new ResourceSelectionAdapter(new ResourceSelectionDialog(
						getParentShell(), ResourcesPlugin.getWorkspace().getRoot(), "Test")));
		
		Button btnAddFolder = new Button(classpathComposite, SWT.NONE);
		btnAddFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddFolder.setText("Add folder ...");
		btnAddFolder.addSelectionListener(new ResourceSelectionAdapter(new ContainerSelectionDialog(
				getParentShell(), ResourcesPlugin.getWorkspace().getRoot(), false, "Test")));
		
		Composite composite = new Composite(classpathComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		final Button btnRemove = new Button(classpathComposite, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemove.setText("Remove");
		btnRemove.setEnabled(false);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				if (!selection.isEmpty()) {
					Object object = selection.getFirstElement();
					if (object instanceof IContent) {
						IContent content = (IContent) object;
						Path foundPath = null;
						for (Path path : tempClasspath.getPath()) {
							if (path.getValue().equals(content.getText())) {
								foundPath = path;
							}
						}
						
						if (foundPath != null) {						
							tempClasspath.getPath().remove(foundPath);
							listViewer.setInput(new ClasspathContent(null, tempClasspath));
						}
					}
					
				}
				btnRemove.setEnabled(false);
			}
		});
		
        listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.isEmpty() || (selection.getFirstElement() instanceof EmptyContent)) {
					btnRemove.setEnabled(false);
				} else {
					btnRemove.setEnabled(true);					
				}
			}
		});
		

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void okPressed() {
		config.setName(nameText.getText().trim());
		config.getLexerClass().setValue(lexerText.getText().trim());
		config.getParserClass().setValue(parserText.getText().trim());
		config.setClasspath(tempClasspath);
		
		isDefault = defaultButton.getSelection();
		
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 430);
	}

	public Configuration getConfiguration() {
		return config;
	}
	
	public boolean isDefault() {
		return isDefault;
	}
}
