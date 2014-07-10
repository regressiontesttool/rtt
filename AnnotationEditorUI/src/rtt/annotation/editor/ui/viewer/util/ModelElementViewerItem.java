package rtt.annotation.editor.ui.viewer.util;

import org.eclipse.swt.graphics.Color;

import rtt.annotation.editor.model.ModelElement;

public class ModelElementViewerItem {
	
	private Object parent;
	private String[] columns;
	
	private ModelElement<?> modelElement;
	
	public ModelElementViewerItem(Object parent, String... columns) {
		this.parent = parent;
		this.columns = columns;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public String getColumnText(int index) {
		if (index < columns.length) {
			return columns[index];
		}
		
		return "";
	}
	
	public Color getForeground() {
		return null;
	}

	public void setModelElement(ModelElement<?> modelElement) {
		this.modelElement = modelElement;
	}
	
	public ModelElement<?> getModelElement() {
		return modelElement;
	}
	
}