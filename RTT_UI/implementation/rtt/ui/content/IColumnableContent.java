package rtt.ui.content;

import org.eclipse.swt.graphics.Image;

public interface IColumnableContent extends IContent {
	
	public String getText(int columnIndex);
	public Image getImage(int columnIndex);
}
