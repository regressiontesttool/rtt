/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import rtt.annotations.*;

import siple.semantics.*;

/**
 * @production Declaration : {@link Statement} ::= <span
 *             class="component">&lt;Name:String&gt;</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:26
 */
public abstract class Declaration extends Statement implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		IsParameterDeclaration_visited = -1;
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
	public Declaration clone() throws CloneNotSupportedException {
		Declaration node = (Declaration) super.clone();
		node.IsParameterDeclaration_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @ast method
	 * 
	 */
	public Declaration() {
		super();

	}

	/**
	 * @ast method
	 * 
	 */
	public Declaration(String p0) {
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
	 * @attribute syn
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/Core.jrag:88
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare
	public abstract Type Type();

	/**
	 * @apilevel internal
	 */
	protected int IsParameterDeclaration_visited = -1;

	/**
	 * @attribute inh
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/Core.jrag:36
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public boolean IsParameterDeclaration() {
		ASTNode$State state = state();
		if (IsParameterDeclaration_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: IsParameterDeclaration in class: ");
		IsParameterDeclaration_visited = state().boundariesCrossed;
		boolean IsParameterDeclaration_value = getParent()
				.Define_boolean_IsParameterDeclaration(this, null);
		IsParameterDeclaration_visited = -1;
		return IsParameterDeclaration_value;
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}

	protected void collect_contributors_ProcedureDeclaration_Decls() {
		/**
		 * @attribute coll
		 * @aspect AccessSupport
		 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:34
		 */
		if (ProcedureInContext() != null) {
			{
				ProcedureDeclaration ref = (ProcedureDeclaration) (ProcedureInContext());
				if (ref != null)
					ref.ProcedureDeclaration_Decls_contributors().add(this);
			}
		}
		super.collect_contributors_ProcedureDeclaration_Decls();
	}

	protected void contributeTo_ProcedureDeclaration_ProcedureDeclaration_Decls(
			java.util.List<Declaration> collection) {
		super.contributeTo_ProcedureDeclaration_ProcedureDeclaration_Decls(collection);
		if (ProcedureInContext() != null)
			collection.add(this);
	}

}
