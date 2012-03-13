package rtt.ui.content;


public interface IContentObserver<T extends IContent> {
	
	String getObserverID();
	void update(T content);
}
