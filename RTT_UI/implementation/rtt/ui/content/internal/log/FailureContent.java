package rtt.ui.content.internal.log;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;

import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.FailureType;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.internal.ContentIcon;

public class FailureContent extends AbstractContent implements IContent, IClickableContent {

	private String text;
	private String path;
	private String message;
	
	public FailureContent(IContent parent, Failure failure) {
		super(parent);
		
		if 	(failure.getType() == FailureType.LEXER) {
			this.text = "Lexer results";
		} else if (failure.getType() == FailureType.PARSER){
			this.text = "Parser results";
		} else {
			this.text = "Unknown error";
		}
		
		this.message = failure.getMsg();
		this.path = failure.getPath();
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.FAILED;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		MessageDialog.openInformation(currentPage.getWorkbenchWindow().getShell(), 
				"Failure", message + " - Path:" + path);
	}

}
