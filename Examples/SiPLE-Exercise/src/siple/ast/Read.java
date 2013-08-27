/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production Read : {@link Statement} ::= <span class="component">
 *             {@link Expression}</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:24
 */
public class Read extends Statement implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		IsCorrectLocal_visited = -1;
		Type_visited = -1;
	}

	/**
	 * @apilevel internal
	 */
	public void flushCollectionCache() {
		super.flushCollectionCache();
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Read clone() throws CloneNotSupportedException {
		Read node = (Read) super.clone();
		node.IsCorrectLocal_visited = -1;
		node.Type_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Read copy() {
		try {
			Read node = (Read) clone();
			if (children != null)
				node.children = (ASTNode[]) children.clone();
			return node;
		} catch (CloneNotSupportedException e) {
		}
		System.err.println("Error: Could not clone node of type "
				+ getClass().getName() + "!");
		return null;
	}

	/**
	 * Create a deep copy of the AST subtree at this node. The copy is dangling,
	 * i.e. has no parent.
	 * 
	 * @return dangling copy of the subtree at this node
	 * @apilevel low-level
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Read fullCopy() {
		try {
			Read tree = (Read) clone();
			tree.setParent(null);// make dangling
			if (children != null) {
				tree.children = new ASTNode[children.length];
				for (int i = 0; i < children.length; ++i) {
					if (children[i] == null) {
						tree.children[i] = null;
					} else {
						tree.children[i] = ((ASTNode) children[i]).fullCopy();
						((ASTNode) tree.children[i]).setParent(tree);
					}
				}
			}
			return tree;
		} catch (CloneNotSupportedException e) {
			throw new Error("Error: clone not supported for "
					+ getClass().getName());
		}
	}

	/**
	 * @ast method
	 * @aspect Interpretation
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:93
	 */
	public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		MemoryLocation loc = (MemoryLocation) getExpression().Value(vm);
		loc.value = ReadDialog.execute(getExpression().Type(),
				vm.stdOut.toString());
	}

	/**
	 * @ast method
	 * 
	 */
	public Read() {
		super();

	}

	/**
	 * @ast method
	 * 
	 */
	public Read(Expression p0) {
		setChild(p0, 0);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	protected int numChildren() {
		return 1;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public boolean mayHaveRewrite() {
		return false;
	}

	/**
	 * Replaces the Expression child.
	 * 
	 * @param node
	 *            The new node to replace the Expression child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setExpression(Expression node) {
		setChild(node, 0);
	}

	/**
	 * Retrieves the Expression child.
	 * 
	 * @return The current node used as the Expression child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Expression getExpression() {
		return (Expression) getChild(0);
	}

	/**
	 * Retrieves the Expression child.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The current node used as the Expression child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public Expression getExpressionNoTransform() {
		return (Expression) getChildNoTransform(0);
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:55
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare("Is Writeable Memory Location")
	public boolean IsCorrectLocal() {
		ASTNode$State state = state();
		if (IsCorrectLocal_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: IsCorrectLocal in class: ");
		IsCorrectLocal_visited = state().boundariesCrossed;
		boolean IsCorrectLocal_value = IsCorrectLocal_compute();
		IsCorrectLocal_visited = -1;
		return IsCorrectLocal_value;
	}

	/**
	 * @apilevel internal
	 */
	private boolean IsCorrectLocal_compute() {
		return Type() != Type.ERROR_TYPE;
	}

	/**
	 * @apilevel internal
	 */
	protected int Type_visited = -1;

	/**
	 * @attribute syn
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:42
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Type Type() {
		ASTNode$State state = state();
		if (Type_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: Type in class: ");
		Type_visited = state().boundariesCrossed;
		Type Type_value = Type_compute();
		Type_visited = -1;
		return Type_value;
	}

	/**
	 * @apilevel internal
	 */
	private Type Type_compute() {
		Type type = getExpression().Type();
		if (!type.isPointer())
			return Type.ERROR_TYPE;
		if (type.rtype.isPointer() || type.rtype.isProcedure()
				|| type.rtype.isUndefined())
			return Type.ERROR_TYPE;
		return type;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
