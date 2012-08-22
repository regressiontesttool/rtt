package rtt.ui.views.utils;

import java.util.ArrayList;
import java.util.List;

import rtt.ui.content.IContent;

public class RttListenerManager<T extends IContent> {
	
	private List<IRttListener<T>> listeners;
	private T currentContent;
	
	public RttListenerManager() {
		listeners = new ArrayList<IRttListener<T>>();
	}
	
	public final void addListener(IRttListener<T> listener) {
		listeners.add(listener);
	}
	
	public final void removeListener(IRttListener<T> listener) {
		listeners.remove(listener);
	}
	
	public void refreshListener()  {
		for (IRttListener<T> listener : listeners) {
			listener.refresh();
		}
	}
	
	public boolean setCurrentContent(T newContent) {
		return setCurrentContent(newContent, false);
	}
	
	public boolean setCurrentContent(T newContent, boolean force) {
		if (force || currentContent != newContent) {
			currentContent = newContent;
			for (IRttListener<T> listener : listeners) {
				listener.update(newContent);
			}
			
			additionalOperations(currentContent);
			
			return true;
		}
		
		return false;
	}
	
	protected void additionalOperations(T content) {}

	public T getCurrentContent() {
		return currentContent;
	}

}
