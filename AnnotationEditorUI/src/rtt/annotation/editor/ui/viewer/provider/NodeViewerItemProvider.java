package rtt.annotation.editor.ui.viewer.provider;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModel.PackageElement;

public final class NodeViewerItemProvider extends ViewerItemProvider {

	private final class ClassElementItem extends
			ModelElementViewerItem<ClassElement> {
		private ClassElementItem(ViewerItem parent, ClassElement element) {
			super(parent, element);
		}

		@Override
		protected ItemColor getItemColor(ClassElement element) {
			if (element.hasAnnotation()) {
				return ItemColor.ANNOTATED;
			}
			
			if (element.hasExtendedAnnotation()) {
				return ItemColor.ANNOTATED_EXTEND;
			}
			
			if (element.hasValues()) {
				return ItemColor.ANNOTATED_MEMBER;
			}					
			
			return super.getItemColor(element);
		}

		@Override
		protected ItemFont getItemFont(ClassElement element) {
			if (element.hasAnnotation()) {
				return ItemFont.BOLD_FONT;
			}
			
			if (element.hasExtendedAnnotation() || element.hasValues()) {
				return ItemFont.ITALIC_FONT;
			}
			
			return super.getItemFont(element);
		}
	}

	List<ViewerItem> packages = new ArrayList<>(0);

	@Override
	List<ViewerItem> setInput(Object input, ViewerItem parent) {
		ClassModel model = (ClassModel) input;
		
		packages.clear();
		for (PackageElement packageElement : model.getPackages()) {
			ViewerItem packageTree = new ModelElementViewerItem<PackageElement>(parent, packageElement);
			createClassItems(model.getClasses(packageElement.getName()), packageTree);
			
			packages.add(packageTree);
		}
		
		return packages;
	}

	public void createClassItems(List<ClassElement> classes, ViewerItem parent) {
		for (ClassElement classElement : classes) {
			ViewerItem item = new ClassElementItem(parent, classElement);
			
			parent.add(item);
		}
	}

	@Override
	boolean hasElements(Object parentElement) {
		return parentElement instanceof ClassModel;
	}
}