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
		configName = "myConfig";
		parserClass = "test.MyParser";
		lexerClass = "test.MyLexer";
		makeDefault = false;
	}
	
	@Override
	protected void addContent(Composite parent) {
		configText = createText(parent, "Name of configuration", configName);	
		lexerText = createText(parent, "Lexerclass", lexerClass);
		parserText = createText(parent, "Parserclass", parserClass);
		
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
	
	public void setContent(String configName, String lexerName, String parserName, boolean makeDefault) {
		this.configName = configName;
		this.lexerClass = lexerName;
		this.parserClass = parserName;
		this.makeDefault = makeDefault;
		
		if (configText != null) {
			configText.setText(configName);
		}

		if (lexerText != null) {
			lexerText.setText(lexerName);
		}
		
		if (parserText != null) {
			parserText.setText(parserName);
		}
		
		if (makeDefaultButton != null) {
			makeDefaultButton.setSelection(makeDefault);
		}
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
