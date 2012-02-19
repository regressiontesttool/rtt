package rtt.ui.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractDialog extends TitleAreaDialog {
	
	private String title;
	private String message;
	private boolean shouldBlock;
	
	private Color red;

	public AbstractDialog(Shell parentShell, String title, String message, boolean shouldBlock) {
		super(parentShell);
		this.shouldBlock = shouldBlock;
		this.title = title;
		this.message = message;

		red = new Color(null, 255, 150, 150);
	}
	
	@Override
	public void create() {
		super.create();
		setBlockOnOpen(shouldBlock);
		setTitle(title);
		setMessage(message);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		composite.setLayout(layout);
		
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(data);
		
		addContent(composite);
		
		return composite;
	}
	
	@Override
	protected void finalize() throws Throwable {
		red.dispose();
		super.finalize();
	}
	
	protected Text createText(Composite parent, final String labelText, String defaultValue) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText + ": ");
		
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.grabExcessHorizontalSpace = true;
		
		final Text text = new Text(parent, SWT.BORDER);
		text.setText(defaultValue);
		text.setLayoutData(gridData);
		
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
		
		return text;
	}
	
	protected abstract void addContent(final Composite parent);
}
