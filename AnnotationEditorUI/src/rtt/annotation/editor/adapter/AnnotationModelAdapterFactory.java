package rtt.annotation.editor.adapter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.model.IWorkbenchAdapter3;
import org.eclipse.ui.model.WorkbenchAdapter;
import org.objectweb.asm.tree.ClassNode;

import rtt.annotation.editor.model.ClassModel;

public class AnnotationModelAdapterFactory implements IAdapterFactory {
	
	private Map<Class<?>, WorkbenchAdapter> providers;
	
	public AnnotationModelAdapterFactory() {
		providers = new HashMap<Class<?>, WorkbenchAdapter>();
		
		providers.put(ClassModel.class, new ClassModelWorkbenchAdapter());
		providers.put(ClassNode.class, new ClassNodeWorkbenchAdapter());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (providers.containsKey(adaptableObject.getClass())) {
			WorkbenchAdapter adapter = providers.get(adaptableObject.getClass());
			if (adapterType.isInstance(adapter)) {
				return adapter;
			}
		}
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class<?>[] { 
				IWorkbenchAdapter.class, 
				IWorkbenchAdapter2.class,
				IWorkbenchAdapter3.class
		};
	}

}
