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
	
	public void addListener(IRttListener<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IRttListener<T> listener) {
		listeners.remove(listener);
	}
	
	public void refreshListener()  {
		for (IRttListener<T> listener : listeners) {
			listener.refresh();
		}
	}
	
	public boolean setCurrentContent(T newContent) {
		if (currentContent != newContent) {
			currentContent = newContent;
			for (IRttListener<T> listener : listeners) {
				System.out.println("***  Content changend: " + newContent + " - " + listener.getClass());
				
				listener.update(newContent);
			}
			
			return true;
		}
		
		return false;
	}
	
	
	public T getCurrentContent() {
		return currentContent;
	}

}
