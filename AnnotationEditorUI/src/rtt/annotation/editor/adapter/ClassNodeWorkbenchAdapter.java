package rtt.annotation.editor.adapter;

import org.eclipse.ui.model.WorkbenchAdapter;
import org.objectweb.asm.tree.ClassNode;

public class ClassNodeWorkbenchAdapter extends WorkbenchAdapter {
	
	@Override
	public String getLabel(Object object) {
		if (object instanceof ClassNode) {
			return ((ClassNode) object).name;
		}
		
		return super.getLabel(object);
	}
	
	public Object[] getChildren(Object object) { 
		if (object instanceof ClassNode) {
			return ((ClassNode) object).interfaces.toArray();
		}
		
		return NO_CHILDREN;
	}
}
