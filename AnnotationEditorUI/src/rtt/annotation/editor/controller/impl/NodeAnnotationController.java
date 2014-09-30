package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.AbstractAnnotationController;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.NodeAnnotation;

public class NodeAnnotationController extends AbstractAnnotationController<NodeAnnotation> {

	public NodeAnnotationController() {
		super(NodeAnnotation.class);
	}

	@Override
	protected boolean canSetAnnotation(Annotatable<?> element) {
		return element instanceof ClassElement && !element.hasAnnotation();
	}
}
