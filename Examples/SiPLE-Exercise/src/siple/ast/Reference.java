/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.*;

import siple.semantics.*;

/**
 * @production Reference : {@link Expression} ::= <span
 *             class="component">&lt;Name:String&gt;</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:38
 */
public class Reference extends Expression implements Cloneable {
	@Parser.Node.Compare("Is Declared")
	public boolean isCorrectLocal() {
		return super.IsCorrectLocal();
	}
	
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		Declaration_visited = -1;
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
	public Reference clone() throws CloneNotSupportedException {
		Reference node = (Reference) super.clone();
		node.Declaration_visited = -1;
		node.Type_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Reference copy() {
		try {
			Reference node = (Reference) clone();
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
	public Reference fullCopy() {
		try {
			Reference tree = (Reference) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:115
	 */
	public Object Value(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		return vm.access(Declaration());
	}

	/**
	 * @ast method
	 * 
	 */
	public Reference() {
		super();

	}

	/**
	 * @ast method
	 * 
	 */
	public Reference(String p0) {
		setName(p0);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	protected int numChildren() {
		return 0;
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
	 * Replaces the lexeme Name.
	 * 
	 * @param value
	 *            The new value for the lexeme Name.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setName(String value) {
		tokenString_Name = value;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	protected String tokenString_Name;

	/**
	 * Retrieves the value for the lexeme Name.
	 * 
	 * @return The value for the lexeme Name.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@Parser.Node.Compare
	public String getName() {
		return tokenString_Name != null ? tokenString_Name : "";
	}

	/**
	 * @apilevel internal
	 */
	protected int Declaration_visited = -1;

	/**
	 * @attribute syn
	 * @aspect NameAnalysis
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:57
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare
	public Declaration Declaration() {
		ASTNode$State state = state();
		if (Declaration_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: Declaration in class: ");
		Declaration_visited = state().boundariesCrossed;
		Declaration Declaration_value = Declaration_compute();
		Declaration_visited = -1;
		return Declaration_value;
	}

	/**
	 * @apilevel internal
	 */
	private Declaration Declaration_compute() {
		LinkedList<Declaration> scope = LookUp(getName());
		return scope.size() == 1 ? scope.get(0) : null;
	}

	/**
	 * @apilevel internal
	 */
	protected int Type_visited = -1;

	/**
	 * @attribute syn
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:62
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
		return Declaration() != null ? Type.newPointer(Declaration().Type())
				: Type.ERROR_TYPE;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
