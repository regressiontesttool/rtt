/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production If : {@link Statement} ::= <span class="component">Condition:
 *             {@link Expression}</span> <span class="component">Body:
 *             {@link Block}</span> <span class="component">[Alternative:
 *             {@link Block}]</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:19
 */
public class If extends Statement implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		IsCorrectLocal_visited = -1;
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
	public If clone() throws CloneNotSupportedException {
		If node = (If) super.clone();
		node.IsCorrectLocal_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public If copy() {
		try {
			If node = (If) clone();
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
	public If fullCopy() {
		try {
			If tree = (If) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:57
	 */
	public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		if ((Boolean) getCondition().Value(vm))
			getBody().Interpret(vm);
		else if (hasAlternative())
			getAlternative().Interpret(vm);
	}

	/**
	 * @ast method
	 * 
	 */
	public If() {
		super();

		setChild(new Opt(), 2);

	}

	/**
	 * @ast method
	 * 
	 */
	public If(Expression p0, Block p1, Opt<Block> p2) {
		setChild(p0, 0);
		setChild(p1, 1);
		setChild(p2, 2);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	protected int numChildren() {
		return 3;
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
	 * Replaces the Condition child.
	 * 
	 * @param node
	 *            The new node to replace the Condition child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setCondition(Expression node) {
		setChild(node, 0);
	}

	/**
	 * Retrieves the Condition child.
	 * 
	 * @return The current node used as the Condition child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Expression getCondition() {
		return (Expression) getChild(0);
	}

	/**
	 * Retrieves the Condition child.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The current node used as the Condition child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public Expression getConditionNoTransform() {
		return (Expression) getChildNoTransform(0);
	}

	/**
	 * Replaces the Body child.
	 * 
	 * @param node
	 *            The new node to replace the Body child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setBody(Block node) {
		setChild(node, 1);
	}

	/**
	 * Retrieves the Body child.
	 * 
	 * @return The current node used as the Body child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Block getBody() {
		return (Block) getChild(1);
	}

	/**
	 * Retrieves the Body child.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The current node used as the Body child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public Block getBodyNoTransform() {
		return (Block) getChildNoTransform(1);
	}

	/**
	 * Replaces the optional node for the Alternative child. This is the
	 * {@code Opt} node containing the child Alternative, not the actual child!
	 * 
	 * @param opt
	 *            The new node to be used as the optional node for the
	 *            Alternative child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void setAlternativeOpt(Opt<Block> opt) {
		setChild(opt, 2);
	}

	/**
	 * Check whether the optional Alternative child exists.
	 * 
	 * @return {@code true} if the optional Alternative child exists,
	 *         {@code false} if it does not.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public boolean hasAlternative() {
		return getAlternativeOpt().getNumChild() != 0;
	}

	/**
	 * Retrieves the (optional) Alternative child.
	 * 
	 * @return The Alternative child, if it exists. Returns {@code null}
	 *         otherwise.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Block getAlternative() {
		return (Block) getAlternativeOpt().getChild(0);
	}

	/**
	 * Replaces the (optional) Alternative child.
	 * 
	 * @param node
	 *            The new node to be used as the Alternative child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setAlternative(Block node) {
		getAlternativeOpt().setChild(node, 0);
	}

	/**
	 * Retrieves the optional node for the Alternative child. This is the
	 * {@code Opt} node containing the child Alternative, not the actual child!
	 * 
	 * @return The optional node for child the Alternative child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Opt<Block> getAlternativeOpt() {
		return (Opt<Block>) getChild(2);
	}

	/**
	 * Retrieves the optional node for child Alternative. This is the
	 * {@code Opt} node containing the child Alternative, not the actual child!
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The optional node for child Alternative.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Opt<Block> getAlternativeOptNoTransform() {
		return (Opt<Block>) getChildNoTransform(2);
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:50
	 */
	@SuppressWarnings({ "unchecked", "cast" })
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
		return getCondition().Type() == Type.Boolean;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
