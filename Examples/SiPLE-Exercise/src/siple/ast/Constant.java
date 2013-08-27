/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import rtt.annotations.*;

import siple.semantics.*;

/**
 * @production Constant : {@link Expression} ::= <span
 *             class="component">&lt;Lexem:String&gt;</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:37
 */
public class Constant extends Expression implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		AsBoolean_visited = -1;
		AsInteger_visited = -1;
		AsReal_visited = -1;
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
	public Constant clone() throws CloneNotSupportedException {
		Constant node = (Constant) super.clone();
		node.AsBoolean_visited = -1;
		node.AsInteger_visited = -1;
		node.AsReal_visited = -1;
		node.Type_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Constant copy() {
		try {
			Constant node = (Constant) clone();
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
	public Constant fullCopy() {
		try {
			Constant tree = (Constant) clone();
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
	 * Expression Evaluation * @ast method
	 * 
	 * @aspect Interpretation
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:107
	 */
	public Object Value(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		switch (Type().domain) {
		case Boolean:
			return AsBoolean();
		case Integer:
			return AsInteger();
		default:
			return AsReal();
		}
	}

	/**
	 * @ast method
	 * 
	 */
	public Constant() {
		super();

	}

	/**
	 * @ast method
	 * 
	 */
	public Constant(String p0) {
		setLexem(p0);
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
	 * Replaces the lexeme Lexem.
	 * 
	 * @param value
	 *            The new value for the lexeme Lexem.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setLexem(String value) {
		tokenString_Lexem = value;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	protected String tokenString_Lexem;

	/**
	 * Retrieves the value for the lexeme Lexem.
	 * 
	 * @return The value for the lexeme Lexem.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@Parser.Node.Compare
	public String getLexem() {
		return tokenString_Lexem != null ? tokenString_Lexem : "";
	}

	/**
	 * @apilevel internal
	 */
	protected int AsBoolean_visited = -1;

	/**
	 * @attribute syn
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:37
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Boolean AsBoolean() {
		ASTNode$State state = state();
		if (AsBoolean_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: AsBoolean in class: ");
		AsBoolean_visited = state().boundariesCrossed;
		Boolean AsBoolean_value = AsBoolean_compute();
		AsBoolean_visited = -1;
		return AsBoolean_value;
	}

	/**
	 * @apilevel internal
	 */
	private Boolean AsBoolean_compute() {
		if ("true".equals(getLexem()))
			return true;
		if ("false".equals(getLexem()))
			return false;
		return null;
	}

	/**
	 * @apilevel internal
	 */
	protected int AsInteger_visited = -1;

	/**
	 * @attribute syn
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:44
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Integer AsInteger() {
		ASTNode$State state = state();
		if (AsInteger_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: AsInteger in class: ");
		AsInteger_visited = state().boundariesCrossed;
		Integer AsInteger_value = AsInteger_compute();
		AsInteger_visited = -1;
		return AsInteger_value;
	}

	/**
	 * @apilevel internal
	 */
	private Integer AsInteger_compute() {
		try {
			return Integer.parseInt(getLexem());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @apilevel internal
	 */
	protected int AsReal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:49
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Float AsReal() {
		ASTNode$State state = state();
		if (AsReal_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: AsReal in class: ");
		AsReal_visited = state().boundariesCrossed;
		Float AsReal_value = AsReal_compute();
		AsReal_visited = -1;
		return AsReal_value;
	}

	/**
	 * @apilevel internal
	 */
	private Float AsReal_compute() {
		if (!getLexem().contains("."))
			return null;
		try {
			return Float.parseFloat(getLexem());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @apilevel internal
	 */
	protected int Type_visited = -1;

	/**
	 * Expressions' Type ** @attribute syn
	 * 
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:53
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
		if (AsBoolean() != null)
			return Type.Boolean;
		if (AsReal() != null)
			return Type.Real;
		if (AsInteger() != null)
			return Type.Integer;
		return Type.ERROR_TYPE;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}
}
