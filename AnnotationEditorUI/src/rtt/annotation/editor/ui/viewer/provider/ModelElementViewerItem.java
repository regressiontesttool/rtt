package rtt.annotation.editor.ui.viewer.provider;

import rtt.annotation.editor.model.ModelElement;

public class ModelElementViewerItem<T extends ModelElement> extends ViewerItem {

	private T element;

	public ModelElementViewerItem(ViewerItem parent, T element) {
		super(parent);
		this.element = element;
	}
	
	public T getModelElement() {
		return element;
	}
	
	@SuppressWarnings("unchecked")
	public <E extends ModelElement> E getModelElement(Class<E> elementType) {
		if (elementType.isInstance(element)) {
			return (E) element;
		}
		
		return null;
	}
	
	@Override
	public final String getColumnText(int columnIndex) {
		return getColumnText(element, columnIndex);
	}
	
	protected String getColumnText(T element, int columnIndex) {
		return element.getName();
	}

	@Override
	protected final ItemColor getItemColor() {
		return getItemColor(element);
	}

	protected ItemColor getItemColor(T element) {
		return null;
	}
	
	@Override
	protected final ItemFont getItemFont() {
		return getItemFont(element);
	}

	protected ItemFont getItemFont(T element) {
		return null;
	}

}
