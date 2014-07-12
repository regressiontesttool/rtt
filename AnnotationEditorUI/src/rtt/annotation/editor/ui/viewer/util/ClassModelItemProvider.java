package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;
import rtt.annotation.editor.ui.viewer.util.ModelElementContentProvider.ItemProvider;

final class ClassModelItemProvider extends ItemProvider {
	
	List<ModelElementViewerItem> packages = new ArrayList<>(0);
	
	public ClassModelItemProvider(Object parent) {
		super(parent);
	}

	@Override
	public List<ModelElementViewerItem> setInput(Object input) {
		ClassModel model = (ClassModel) input;
		
		packages.clear();
		for (PackageElement packageElement : model.getPackages()) {
			String packageName = packageElement.getName();
			ModelElementViewerTree packageTree = createTree(packageName);
			packageTree.setModelElement(packageElement);
			
			packageTree.items = createClassItems(model.getClasses(packageName), packageTree);
			
			packages.add(packageTree);
		}
		
		return packages;
	}

	public List<ModelElementViewerItem> createClassItems(List<ClassElement> classes, ModelElementViewerTree parent) {
		List<ModelElementViewerItem> classItems = new ArrayList<>(classes.size());
		for (ClassElement classElement : classes) {
			ModelElementViewerItem item = createItem(parent, classElement.getName());
			item.setModelElement(classElement);
			
			classItems.add(item);
		}
		
		return classItems;
	}
}