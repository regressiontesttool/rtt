/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import rtt.annotations.*;

import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production VariableAssignment : {@link Statement} ::= <span
 *             class="component">LValue:{@link Expression}</span> <span
 *             class="component">RValue:{@link Expression}</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:21
 */
public class VariableAssignment extends Statement implements Cloneable {
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
	public VariableAssignment clone() throws CloneNotSupportedException {
		VariableAssignment node = (VariableAssignment) super.clone();
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
	public VariableAssignment copy() {
		try {
			VariableAssignment node = (VariableAssignment) clone();
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
	public VariableAssignment fullCopy() {
		try {
			VariableAssignment tree = (VariableAssignment) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:74
	 */
	public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		MemoryLocation loc = (MemoryLocation) getLValue().Value(vm);
		loc.value = getRValue().Value(vm);
	}

	/**
	 * @ast method
	 * @aspect TypeCoercions
	 * @declaredat ./../../specifications/semantics/TypeCoercions.jrag:19
	 */

	private boolean coercionTested = false;

	/**
	 * @ast method
	 * 
	 */
	public VariableAssignment() {
		super();

	}

	/**
	 * @ast method
	 * 
	 */
	public VariableAssignment(Expression p0, Expression p1) {
		setChild(p0, 0);
		setChild(p1, 1);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	protected int numChildren() {
		return 2;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public boolean mayHaveRewrite() {
		return true;
	}

	/**
	 * Replaces the LValue child.
	 * 
	 * @param node
	 *            The new node to replace the LValue child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setLValue(Expression node) {
		setChild(node, 0);
	}

	/**
	 * Retrieves the LValue child.
	 * 
	 * @return The current node used as the LValue child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Expression getLValue() {
		return (Expression) getChild(0);
	}

	/**
	 * Retrieves the LValue child.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The current node used as the LValue child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public Expression getLValueNoTransform() {
		return (Expression) getChildNoTransform(0);
	}

	/**
	 * Replaces the RValue child.
	 * 
	 * @param node
	 *            The new node to replace the RValue child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setRValue(Expression node) {
		setChild(node, 1);
	}

	/**
	 * Retrieves the RValue child.
	 * 
	 * @return The current node used as the RValue child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Expression getRValue() {
		return (Expression) getChild(1);
	}

	/**
	 * Retrieves the RValue child.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The current node used as the RValue child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public Expression getRValueNoTransform() {
		return (Expression) getChildNoTransform(1);
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:52
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare("Is Assignable")
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
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:23
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
		return getLValue().Type().isPointer() ? Type.equals(
				getLValue().Type().rtype, getRValue().Type()) : Type.ERROR_TYPE;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		// Declared in ./../../specifications/semantics/TypeCoercions.jrag at
		// line 36
		if (!coercionTested) {
			state().duringTypeCoercions++;
			ASTNode result = rewriteRule0();
			state().duringTypeCoercions--;
			return result;
		}

		return super.rewriteTo();
	}

	/**
	 * @declaredat ./../../specifications/semantics/TypeCoercions.jrag:36
	 * @apilevel internal
	 */
	private VariableAssignment rewriteRule0() {
		{
			coercionTested = true;
			if (getRValue().Type().isInteger()
					&& getLValue().Type().isPointer()
					&& getLValue().Type().rtype.isReal()) {
				setRValue(new RealCoercion(getRValue()));
			}
			return this;
		}
	}
}
