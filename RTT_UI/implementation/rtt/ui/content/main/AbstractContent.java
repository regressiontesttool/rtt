package rtt.ui.content.main;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import rtt.core.manager.Manager;
import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo;
import rtt.ui.model.RttProject;

public abstract class AbstractContent implements IContent {
	
	protected static final IContent[] EMPTY_ARRAY = new IContent[0];
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
	public void reload(ReloadInfo info, Manager manager) {}
	
	@Override
	public void notifyChanges() {}
	
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
	public IContent[] getChildren() {
		if (childs == null) {
			return EMPTY_ARRAY;
		}
		
		return childs.toArray(new IContent[childs.size()]);
	}
	
	public Image getImage() {
		return getIcon().getImage(!hasChildren());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getContent(Class<T> clazz) {
		if (clazz == this.getClass()) {
			return (T) this;
		}
		
		if (parent instanceof IContent) {
			return ((IContent) parent).getContent(clazz);
		}
		
		return null;
	}
	
	@Override
	public String getToolTip() {
		return "";
	}
	
	protected abstract ContentIcon getIcon();
}
