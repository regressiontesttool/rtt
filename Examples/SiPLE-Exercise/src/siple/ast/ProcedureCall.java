/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production ProcedureCall : {@link Expression} ::= <span
 *             class="component">Procedure:{@link Expression}</span> <span
 *             class="component">Argument:{@link Expression}*</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:39
 */
public class ProcedureCall extends Expression implements Cloneable {
	@Parser.Node.Compare("Is Valid Procedure Application")
	public boolean isCorrectLocal() {
		return super.IsCorrectLocal();
	}
	
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
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
	public ProcedureCall clone() throws CloneNotSupportedException {
		ProcedureCall node = (ProcedureCall) super.clone();
		node.Type_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public ProcedureCall copy() {
		try {
			ProcedureCall node = (ProcedureCall) clone();
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
	public ProcedureCall fullCopy() {
		try {
			ProcedureCall tree = (ProcedureCall) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:120
	 */
	public Object Value(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		// Evaluate the Procedure operand:
		Frame envPrototype = (Frame) getProcedure().Value(vm);
		// Evaluate the arguments:
		Object[] args = new Object[getNumArgument()];
		for (int i = 0; i < args.length; i++)
			args[i] = getArgument(i).Value(vm);
		// Prepare the execution environment:
		Frame newEnv = new Frame();
		newEnv.implementation = envPrototype.implementation;
		newEnv.closure = envPrototype.closure;
		Frame oldEnv = vm.currentFrame;
		vm.currentFrame = newEnv;
		// Execute the procedure:
		for (int i = 0; i < args.length; i++)
			vm.allocate(newEnv.implementation.getParameter(i), args[i]);
		newEnv.implementation.getBody().Interpret(vm);
		Object result = null;
		if (Type() != Type.Undefined)
			result = newEnv.returnValue;
		// Restore the old execution environment:
		vm.currentFrame = oldEnv;
		return result;
	}

	/**
	 * @ast method
	 * 
	 */
	public ProcedureCall() {
		super();

		setChild(new List(), 1);

	}

	/**
	 * @ast method
	 * 
	 */
	public ProcedureCall(Expression p0, List<Expression> p1) {
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
		return false;
	}

	/**
	 * Replaces the Procedure child.
	 * 
	 * @param node
	 *            The new node to replace the Procedure child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setProcedure(Expression node) {
		setChild(node, 0);
	}

	/**
	 * Retrieves the Procedure child.
	 * 
	 * @return The current node used as the Procedure child.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Expression getProcedure() {
		return (Expression) getChild(0);
	}

	/**
	 * Retrieves the Procedure child.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The current node used as the Procedure child.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public Expression getProcedureNoTransform() {
		return (Expression) getChildNoTransform(0);
	}

	/**
	 * Replaces the Argument list.
	 * 
	 * @param list
	 *            The new list node to be used as the Argument list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setArgumentList(List<Expression> list) {
		setChild(list, 1);
	}

	/**
	 * Retrieves the number of children in the Argument list.
	 * 
	 * @return Number of children in the Argument list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public int getNumArgument() {
		return getArgumentList().getNumChild();
	}

	/**
	 * Retrieves the number of children in the Argument list. Calling this
	 * method will not trigger rewrites..
	 * 
	 * @return Number of children in the Argument list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public int getNumArgumentNoTransform() {
		return getArgumentListNoTransform().getNumChildNoTransform();
	}

	/**
	 * Retrieves the element at index {@code i} in the Argument list..
	 * 
	 * @param i
	 *            Index of the element to return.
	 * @return The element at position {@code i} in the Argument list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Expression getArgument(int i) {
		return (Expression) getArgumentList().getChild(i);
	}

	/**
	 * Append an element to the Argument list.
	 * 
	 * @param node
	 *            The element to append to the Argument list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void addArgument(Expression node) {
		List<Expression> list = (parent == null || state == null) ? getArgumentListNoTransform()
				: getArgumentList();
		list.addChild(node);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void addArgumentNoTransform(Expression node) {
		List<Expression> list = getArgumentListNoTransform();
		list.addChild(node);
	}

	/**
	 * Replaces the Argument list element at index {@code i} with the new node
	 * {@code node}.
	 * 
	 * @param node
	 *            The new node to replace the old list element.
	 * @param i
	 *            The list index of the node to be replaced.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setArgument(Expression node, int i) {
		List<Expression> list = getArgumentList();
		list.setChild(node, i);
	}

	/**
	 * Retrieves the Argument list.
	 * 
	 * @return The node representing the Argument list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public List<Expression> getArguments() {
		return getArgumentList();
	}

	/**
	 * Retrieves the Argument list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Argument list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public List<Expression> getArgumentsNoTransform() {
		return getArgumentListNoTransform();
	}

	/**
	 * Retrieves the Argument list.
	 * 
	 * @return The node representing the Argument list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<Expression> getArgumentList() {
		List<Expression> list = (List<Expression>) getChild(1);
		list.getNumChild();
		return list;
	}

	/**
	 * Retrieves the Argument list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Argument list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<Expression> getArgumentListNoTransform() {
		return (List<Expression>) getChildNoTransform(1);
	}

	/**
	 * @apilevel internal
	 */
	protected int Type_visited = -1;

	/**
	 * @attribute syn
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:65
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare
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
		Type type = getProcedure().Type();
		if (!type.isProcedure())
			return Type.ERROR_TYPE;
		if (getNumArgument() != type.paras.length)
			return Type.ERROR_TYPE;
		for (int i = 0; i < getNumArgument(); i++)
			if (!Type.bequals(type.paras[i], getArgument(i).Type()))
				return Type.ERROR_TYPE;
		return type.rtype;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
