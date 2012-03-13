package rtt.ui;

import rtt.ui.content.IContent;

public interface IRttListener<T extends IContent> {
	
	void refresh();
	void update(T content);

}
