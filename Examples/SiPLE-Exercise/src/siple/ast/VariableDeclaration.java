/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production VariableDeclaration : {@link Declaration} ::= <span
 *             class="component">&lt;DeclaredType:Type&gt;</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:32
 */
public class VariableDeclaration extends Declaration implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		IsCorrectLocal_visited = -1;
		LookUpLocal_String_visited = null;
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
	public VariableDeclaration clone() throws CloneNotSupportedException {
		VariableDeclaration node = (VariableDeclaration) super.clone();
		node.IsCorrectLocal_visited = -1;
		node.LookUpLocal_String_visited = null;
		node.Type_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public VariableDeclaration copy() {
		try {
			VariableDeclaration node = (VariableDeclaration) clone();
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
	public VariableDeclaration fullCopy() {
		try {
			VariableDeclaration tree = (VariableDeclaration) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:45
	 */
	public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		vm.allocate(this, null);
	}

	/**
	 * @ast method
	 * 
	 */
	public VariableDeclaration() {
		super();

	}

	/**
	 * @ast method
	 * 
	 */
	public VariableDeclaration(String p0, Type p1) {
		setName(p0);
		setDeclaredType(p1);
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
	 * Retrieves the value for the lexeme Name.
	 * 
	 * @return The value for the lexeme Name.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public String getName() {
		return tokenString_Name != null ? tokenString_Name : "";
	}

	/**
	 * Replaces the lexeme DeclaredType.
	 * 
	 * @param value
	 *            The new value for the lexeme DeclaredType.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setDeclaredType(Type value) {
		tokenType_DeclaredType = value;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	protected Type tokenType_DeclaredType;

	/**
	 * Retrieves the value for the lexeme DeclaredType.
	 * 
	 * @return The value for the lexeme DeclaredType.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@Parser.Node.Compare
	public Type getDeclaredType() {
		return tokenType_DeclaredType;
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:48
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare("Is Unique")
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
		return LookUp(getName()).size() < 2;
	}

	/**
	 * @apilevel internal
	 */
	protected java.util.Map LookUpLocal_String_visited;

	/**
	 * @attribute syn
	 * @aspect NameAnalysis
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:43
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public LinkedList<Declaration> LookUpLocal(String name) {
		Object _parameters = name;
		if (LookUpLocal_String_visited == null)
			LookUpLocal_String_visited = new java.util.HashMap(4);
		ASTNode$State state = state();
		if (Integer.valueOf(state().boundariesCrossed).equals(
				LookUpLocal_String_visited.get(_parameters)))
			throw new RuntimeException(
					"Circular definition of attr: LookUpLocal in class: ");
		LookUpLocal_String_visited.put(_parameters,
				Integer.valueOf(state().boundariesCrossed));
		LinkedList<Declaration> LookUpLocal_String_value = LookUpLocal_compute(name);
		LookUpLocal_String_visited.remove(_parameters);
		return LookUpLocal_String_value;
	}

	/**
	 * @apilevel internal
	 */
	private LinkedList<Declaration> LookUpLocal_compute(String name) {
		LinkedList<Declaration> result = new LinkedList<Declaration>();
		if (!IsParameterDeclaration() && getName().equals(name))
			result.add(this);
		return result;
	}

	/**
	 * @apilevel internal
	 */
	protected int Type_visited = -1;

	/**
	 * @attribute syn
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:22
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
		return getDeclaredType();
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
