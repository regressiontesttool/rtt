package rtt.ui;

import java.util.ArrayList;
import java.util.List;

import rtt.ui.content.IContent;

public class RttListenerManager<T extends IContent> {
	
	private List<IRttListener<T>> listeners;
	private T currentContent;
	
	public RttListenerManager(T initContent) {
		this();
		currentContent = initContent;
	}
	
	public RttListenerManager() {
		listeners = new ArrayList<IRttListener<T>>();
	}
	
	public void addListener(IRttListener<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IRttListener<T> listener) {
		listeners.remove(listener);
	}
	
	protected void refreshListener()  {
		for (IRttListener<T> listener : listeners) {
			listener.refresh();
		}
	}
	
	public void setCurrentContent(T newContent) {
		if (currentContent != newContent) {
			currentContent = newContent;
			for (IRttListener<T> listener : listeners) {
				listener.update(newContent);
			}
		}
	}
	
	public T getCurrentContent() {
		return currentContent;
	}

}
