package rtt.ui.content;


public interface IContentObserver {
	
	String getObserverID();
	void update(IContent content);
}
