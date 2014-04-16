package rtt.ui.views.utils;

import java.util.ArrayList;
import java.util.List;

import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo.Content;

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
	
	/**
	 * Updates all listeners to handle the (new) current content.
	 * This method is for example used to update all views. Without
	 * the {@code force} parameter listeners will only be updated 
	 * if the *new* current content doesn't equal the *old* current content.
	 * 
	 * @param newContent the *new* current {@link Content}
	 * @param force force an update
	 * @return {@code true}, if update has occurred.
	 */
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
