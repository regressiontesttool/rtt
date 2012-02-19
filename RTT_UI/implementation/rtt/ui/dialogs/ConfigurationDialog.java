package rtt.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConfigurationDialog extends AbstractDialog {

	private Text configText;
	private Text parserText;
	private Text lexerText;
	private Button makeDefaultButton;
	
	private String configName;
	private String parserClass;
	private String lexerClass;
	private boolean makeDefault;
	
	public ConfigurationDialog(Shell parentShell) {
		super(parentShell, "New configuration", "Input data for a new configuration.", true);
		configName = "";
		parserClass = "";
		lexerClass = "";
		makeDefault = false;
	}
	
	@Override
	protected void addContent(Composite parent) {
		configText = createText(parent, "Name of configuration", "myConfig");	
		lexerText = createText(parent, "Lexerclass", "test.MyLexer");
		parserText = createText(parent, "Parserclass", "test.MyParser");
		
		Label defaultButtonLabel = new Label(parent, SWT.NONE);
		defaultButtonLabel.setText("Make default?");
		
		makeDefaultButton = new Button(parent, SWT.CHECK);
	}
	
	@Override
	protected void okPressed() {
		configName = configText.getText().trim();
		parserClass = parserText.getText().trim();
		lexerClass = lexerText.getText().trim();
		makeDefault = makeDefaultButton.getSelection();	
		
		super.okPressed();		
	}
	
	public String getConfigName() {
		return configName;
	}
	
	public String getParserName() {
		return parserClass;
	}
	
	public String getLexerName() {
		return lexerClass;
	}
	
	public boolean getMakeDefault() {
		return makeDefault;
	}

	
}
