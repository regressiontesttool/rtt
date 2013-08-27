/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production Block : {@link Statement} ::= <span class="component">
 *             {@link Statement}*</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:18
 */
public class Block extends Statement implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		IsProcedureBody_visited = -1;
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
	public Block clone() throws CloneNotSupportedException {
		Block node = (Block) super.clone();
		node.IsProcedureBody_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Block copy() {
		try {
			Block node = (Block) clone();
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
	public Block fullCopy() {
		try {
			Block tree = (Block) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:50
	 */
	public void Interpret(State vm) {
		for (int i = 0; i < getNumStatement(); i++) {
			getStatement(i).Interpret(vm);
			if (vm.currentFrame.returnValue != null)
				return;
		}
	}

	/**
	 * @ast method
	 * 
	 */
	public Block() {
		super();

		setChild(new List(), 0);

	}

	/**
	 * @ast method
	 * 
	 */
	public Block(List<Statement> p0) {
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
	 * Replaces the Statement list.
	 * 
	 * @param list
	 *            The new list node to be used as the Statement list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setStatementList(List<Statement> list) {
		setChild(list, 0);
	}

	/**
	 * Retrieves the number of children in the Statement list.
	 * 
	 * @return Number of children in the Statement list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public int getNumStatement() {
		return getStatementList().getNumChild();
	}

	/**
	 * Retrieves the number of children in the Statement list. Calling this
	 * method will not trigger rewrites..
	 * 
	 * @return Number of children in the Statement list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public int getNumStatementNoTransform() {
		return getStatementListNoTransform().getNumChildNoTransform();
	}

	/**
	 * Retrieves the element at index {@code i} in the Statement list..
	 * 
	 * @param i
	 *            Index of the element to return.
	 * @return The element at position {@code i} in the Statement list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Statement getStatement(int i) {
		return (Statement) getStatementList().getChild(i);
	}

	/**
	 * Append an element to the Statement list.
	 * 
	 * @param node
	 *            The element to append to the Statement list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void addStatement(Statement node) {
		List<Statement> list = (parent == null || state == null) ? getStatementListNoTransform()
				: getStatementList();
		list.addChild(node);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void addStatementNoTransform(Statement node) {
		List<Statement> list = getStatementListNoTransform();
		list.addChild(node);
	}

	/**
	 * Replaces the Statement list element at index {@code i} with the new node
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
	public void setStatement(Statement node, int i) {
		List<Statement> list = getStatementList();
		list.setChild(node, i);
	}

	/**
	 * Retrieves the Statement list.
	 * 
	 * @return The node representing the Statement list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public List<Statement> getStatements() {
		return getStatementList();
	}

	/**
	 * Retrieves the Statement list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Statement list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public List<Statement> getStatementsNoTransform() {
		return getStatementListNoTransform();
	}

	/**
	 * Retrieves the Statement list.
	 * 
	 * @return The node representing the Statement list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<Statement> getStatementList() {
		List<Statement> list = (List<Statement>) getChild(0);
		list.getNumChild();
		return list;
	}

	/**
	 * Retrieves the Statement list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Statement list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<Statement> getStatementListNoTransform() {
		return (List<Statement>) getChildNoTransform(0);
	}

	/**
	 * @apilevel internal
	 */
	protected int IsProcedureBody_visited = -1;

	/**
	 * @attribute inh
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/Core.jrag:39
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public ProcedureDeclaration IsProcedureBody() {
		ASTNode$State state = state();
		if (IsProcedureBody_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: IsProcedureBody in class: ");
		IsProcedureBody_visited = state().boundariesCrossed;
		ProcedureDeclaration IsProcedureBody_value = getParent()
				.Define_ProcedureDeclaration_IsProcedureBody(this, null);
		IsProcedureBody_visited = -1;
		return IsProcedureBody_value;
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:26
	 * @apilevel internal
	 */
	public ProcedureDeclaration Define_ProcedureDeclaration_IsProcedureBody(
			ASTNode caller, ASTNode child) {
		if (caller == getStatementListNoTransform()) {
			int index = caller.getIndexOfChild(child);
			return null;
		} else {
			return getParent().Define_ProcedureDeclaration_IsProcedureBody(
					this, caller);
		}
	}

	/**
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:24
	 * @apilevel internal
	 */
	public LinkedList<Declaration> Define_LinkedList_Declaration__LookUp(
			ASTNode caller, ASTNode child, String name) {
		if (caller == getStatementListNoTransform()) {
			int index = caller.getIndexOfChild(child);
			{
				LinkedList<Declaration> result = new LinkedList<Declaration>();
				if (IsProcedureBody() != null)
					for (VariableDeclaration para : IsProcedureBody()
							.getParameterList())
						if (para.getName().equals(name))
							result.add(para);
				for (int i = 0; i <= index; i++)
					// Declare before use
					result.addAll(getStatement(i).LookUpLocal(name));
				return result;
			}
		} else {
			return getParent().Define_LinkedList_Declaration__LookUp(this,
					caller, name);
		}
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
