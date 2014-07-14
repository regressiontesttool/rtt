package rtt.annotation.editor.ui.viewer.util;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;

public final class ClassModelItemProvider extends ViewerItemProvider {
	
	List<ViewerItem> packages = new ArrayList<>(0);

	@Override
	List<ViewerItem> setInput(Object input) {
		ClassModel model = (ClassModel) input;
		
		packages.clear();
		for (PackageElement packageElement : model.getPackages()) {
			String packageName = packageElement.getName();
			ViewerTree packageTree = new ViewerTree(this, packageName);
			packageTree.setModelElement(packageElement);
			
			packageTree.items = createClassItems(model.getClasses(packageName), packageTree);
			
			packages.add(packageTree);
		}
		
		return packages;
	}

	public List<ViewerItem> createClassItems(List<ClassElement> classes, ViewerTree parent) {
		List<ViewerItem> classItems = new ArrayList<>(classes.size());
		for (ClassElement classElement : classes) {
			ViewerItem item = createItem(parent, classElement.getName());
			item.setModelElement(classElement);
			
			if (classElement.hasMemberAnnotation()) {
				item.setForeground(ItemColor.EXTEND_MEMBER);
				item.setFont(ItemFont.ITALIC_FONT);
			}
			
			if (classElement.hasExtendedAnnotation()) {
				item.setForeground(ItemColor.EXTEND_NODE);
				item.setFont(ItemFont.ITALIC_FONT);
			}
			
			if (classElement.hasAnnotation()) {
				item.setForeground(ItemColor.NODE);
				item.setFont(ItemFont.BOLD_FONT);
			}
			
			classItems.add(item);
		}
		
		return classItems;
	}

	@Override
	boolean hasRoot(Object parentElement) {
		return parentElement.getClass().equals(ClassModel.class);
	}
}