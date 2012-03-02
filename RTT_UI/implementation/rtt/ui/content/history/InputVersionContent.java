package rtt.ui.content.history;

import java.util.Calendar;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import rtt.core.archive.history.Version;
import rtt.core.manager.data.IHistoryManager;
import rtt.core.manager.data.InputManager;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IDecoratableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.editors.input.InputEditorInput;

public class InputVersionContent extends AbstractContent implements IClickableContent, IDecoratableContent {

	Integer versionNr;
	InputManager manager;
	Calendar calendar;
	
	public InputVersionContent(IContent parent, Version version, IHistoryManager manager) {
		super(parent);
		
		this.versionNr = version.getNr();
		this.calendar = version.getDate();
		this.manager = (InputManager) manager;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		try {
			currentPage.openEditor(new InputEditorInput(manager, versionNr),
					"org.eclipse.ui.DefaultTextEditor", true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getText() {
		return "Version";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.VERSION;
	}

	@Override
	public Image decorateImage(ResourceManager manager, Image image,
			IContent content) {
		return image;
	}

	@Override
	public String decorateText(String text, IContent content) {
		return text + " " + versionNr + " (" + String.format("%1$te.%1$tm.%1$tY", calendar) + ")";
	}

}
