package rtt.annotation.editor.adapter;

import org.eclipse.ui.model.WorkbenchAdapter;

import rtt.annotation.editor.model.ClassModel;

public class ClassModelWorkbenchAdapter extends WorkbenchAdapter {
	
	@Override
	public Object[] getChildren(Object o) {
		if (o instanceof ClassModel) {
			ClassModel model = (ClassModel) o;
			return model.getNodes().toArray();
		}
		
		return super.getChildren(o);
	}

}
