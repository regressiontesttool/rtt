/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production ProcedureReturn : {@link Statement} ::= <span class="component">[
 *             {@link Expression}]</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:22
 */
public class ProcedureReturn extends Statement implements Cloneable {
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
	public ProcedureReturn clone() throws CloneNotSupportedException {
		ProcedureReturn node = (ProcedureReturn) super.clone();
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
	public ProcedureReturn copy() {
		try {
			ProcedureReturn node = (ProcedureReturn) clone();
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
	public ProcedureReturn fullCopy() {
		try {
			ProcedureReturn tree = (ProcedureReturn) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:80
	 */
	public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		if (hasExpression())
			vm.currentFrame.returnValue = getExpression().Value(vm);
		else
			vm.currentFrame.returnValue = Type.Undefined;
	}

	/**
	 * @ast method
	 * 
	 */
	public ProcedureReturn() {
		super();

		setChild(new Opt(), 0);

	}

	/**
	 * @ast method
	 * 
	 */
	public ProcedureReturn(Opt<Expression> p0) {
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
	 * Replaces the optional node for the Expression child. This is the
	 * {@code Opt} node containing the child Expression, not the actual child!
	 * 
	 * @param opt
	 *            The new node to be used as the optional node for the
	 *            Expression child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void setExpressionOpt(Opt<Expression> opt) {
		setChild(opt, 0);
	}

	/**
	 * Check whether the optional Expression child exists.
	 * 
	 * @return {@code true} if the optional Expression child exists,
	 *         {@code false} if it does not.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public boolean hasExpression() {
		return getExpressionOpt().getNumChild() != 0;
	}

	/**
	 * Retrieves the (optional) Expression child.
	 * 
	 * @return The Expression child, if it exists. Returns {@code null}
	 *         otherwise.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Expression getExpression() {
		return (Expression) getExpressionOpt().getChild(0);
	}

	/**
	 * Replaces the (optional) Expression child.
	 * 
	 * @param node
	 *            The new node to be used as the Expression child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setExpression(Expression node) {
		getExpressionOpt().setChild(node, 0);
	}

	/**
	 * Retrieves the optional node for the Expression child. This is the
	 * {@code Opt} node containing the child Expression, not the actual child!
	 * 
	 * @return The optional node for child the Expression child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Opt<Expression> getExpressionOpt() {
		return (Opt<Expression>) getChild(0);
	}

	/**
	 * Retrieves the optional node for child Expression. This is the {@code Opt}
	 * node containing the child Expression, not the actual child!
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The optional node for child Expression.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Opt<Expression> getExpressionOptNoTransform() {
		return (Opt<Expression>) getChildNoTransform(0);
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:53
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare("Returns Expected Type")
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
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:26
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
		ProcedureDeclaration decl = ProcedureInContext();
		if (decl == null)
			return Type.ERROR_TYPE;
		if (!hasExpression())
			if (!decl.getReturnType().isUndefined())
				return Type.ERROR_TYPE;
			else
				return Type.Undefined;
		return Type.equals(decl.getReturnType(), getExpression().Type());
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}

	protected void collect_contributors_ProcedureDeclaration_Returns() {
		/**
		 * @attribute coll
		 * @aspect AccessSupport
		 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:31
		 */
		if (ProcedureInContext() != null) {
			{
				ProcedureDeclaration ref = (ProcedureDeclaration) (ProcedureInContext());
				if (ref != null)
					ref.ProcedureDeclaration_Returns_contributors().add(this);
			}
		}
		super.collect_contributors_ProcedureDeclaration_Returns();
	}

	protected void contributeTo_ProcedureDeclaration_ProcedureDeclaration_Returns(
			java.util.List<ProcedureReturn> collection) {
		super.contributeTo_ProcedureDeclaration_ProcedureDeclaration_Returns(collection);
		if (ProcedureInContext() != null)
			collection.add(this);
	}

}
