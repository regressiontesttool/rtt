package rtt.ui.content;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.ui.content.internal.ContentIcon;
import rtt.ui.model.RttProject;

public abstract class AbstractContent implements IContent {
	
	protected static final Object[] EMPTY_ARRAY = new Object[0];
	protected IContent parent = null;	
	protected List<IContent> childs;	
	
	public AbstractContent(IContent parent) {
		this.parent = parent;
		
		childs = new ArrayList<IContent>();
	}
	
	@Override
	public RttProject getProject() {
		if (parent != null) {
			return parent.getProject();
		}
		
		return null;
	}
	
	@Override
	public void load() {
		if (parent != null) {
			parent.load();
		}
	}
	
	@Override
	public IContent getParent() {
		return parent;
	}
	
	@Override
	public boolean hasChildren() {
		if (childs == null) {
			return false;
		}
		
		return !childs.isEmpty();
	}
	
	@Override
	public Object[] getChildren() {
		if (childs == null) {
			return EMPTY_ARRAY;
		}
		
		return childs.toArray();
	}

	@Override
	public Image getImage(ResourceManager manager) {
		return manager.createImage(getIcon().getDescriptor(!hasChildren()));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getContent(Class<T> clazz) {
		if (clazz == this.getClass()) {
			return (T) this;
		}
		
		if (parent != null && parent instanceof IContent) {
			return ((IContent) parent).getContent(clazz);
		}
		
		throw new RuntimeException("No '" + clazz + "' content found in " + this.getClass());
	}
	
	protected abstract ContentIcon getIcon();
}
