/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production ProcedureDeclaration : {@link Declaration} ::= <span
 *             class="component">Parameter:{@link VariableDeclaration}*</span>
 *             <span class="component">&lt;ReturnType:Type&gt;</span> <span
 *             class="component">Body:{@link Block}</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:28
 */
public class ProcedureDeclaration extends Declaration implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		IsCorrectLocal_visited = -1;
		LookUpLocal_String_visited = null;
		Type_visited = -1;
		ProcedureDeclaration_Returns_visited = -1;
		ProcedureDeclaration_Returns_computed = false;
		ProcedureDeclaration_Returns_value = null;
		ProcedureDeclaration_Returns_contributors = null;
		ProcedureDeclaration_Decls_visited = -1;
		ProcedureDeclaration_Decls_computed = false;
		ProcedureDeclaration_Decls_value = null;
		ProcedureDeclaration_Decls_contributors = null;
	}

	/**
	 * @apilevel internal
	 */
	public void flushCollectionCache() {
		super.flushCollectionCache();
		ProcedureDeclaration_Returns_visited = -1;
		ProcedureDeclaration_Returns_computed = false;
		ProcedureDeclaration_Returns_value = null;
		ProcedureDeclaration_Returns_contributors = null;
		ProcedureDeclaration_Decls_visited = -1;
		ProcedureDeclaration_Decls_computed = false;
		ProcedureDeclaration_Decls_value = null;
		ProcedureDeclaration_Decls_contributors = null;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public ProcedureDeclaration clone() throws CloneNotSupportedException {
		ProcedureDeclaration node = (ProcedureDeclaration) super.clone();
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
	public ProcedureDeclaration copy() {
		try {
			ProcedureDeclaration node = (ProcedureDeclaration) clone();
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
	public ProcedureDeclaration fullCopy() {
		try {
			ProcedureDeclaration tree = (ProcedureDeclaration) clone();
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
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:37
	 */
	public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		Frame envPrototype = new Frame();
		envPrototype.implementation = this;
		envPrototype.closure = vm.currentFrame;
		vm.allocate(this, envPrototype);
	}

	/**
	 * @ast method
	 * 
	 */
	public ProcedureDeclaration() {
		super();

		setChild(new List(), 0);

	}

	/**
	 * @ast method
	 * 
	 */
	public ProcedureDeclaration(String p0, List<VariableDeclaration> p1,
			Type p2, Block p3) {
		setName(p0);
		setChild(p1, 0);
		setReturnType(p2);
		setChild(p3, 1);
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
	 * Replaces the Parameter list.
	 * 
	 * @param list
	 *            The new list node to be used as the Parameter list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setParameterList(List<VariableDeclaration> list) {
		setChild(list, 0);
	}

	/**
	 * Retrieves the number of children in the Parameter list.
	 * 
	 * @return Number of children in the Parameter list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public int getNumParameter() {
		return getParameterList().getNumChild();
	}

	/**
	 * Retrieves the number of children in the Parameter list. Calling this
	 * method will not trigger rewrites..
	 * 
	 * @return Number of children in the Parameter list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public int getNumParameterNoTransform() {
		return getParameterListNoTransform().getNumChildNoTransform();
	}

	/**
	 * Retrieves the element at index {@code i} in the Parameter list..
	 * 
	 * @param i
	 *            Index of the element to return.
	 * @return The element at position {@code i} in the Parameter list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public VariableDeclaration getParameter(int i) {
		return (VariableDeclaration) getParameterList().getChild(i);
	}

	/**
	 * Append an element to the Parameter list.
	 * 
	 * @param node
	 *            The element to append to the Parameter list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void addParameter(VariableDeclaration node) {
		List<VariableDeclaration> list = (parent == null || state == null) ? getParameterListNoTransform()
				: getParameterList();
		list.addChild(node);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void addParameterNoTransform(VariableDeclaration node) {
		List<VariableDeclaration> list = getParameterListNoTransform();
		list.addChild(node);
	}

	/**
	 * Replaces the Parameter list element at index {@code i} with the new node
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
	public void setParameter(VariableDeclaration node, int i) {
		List<VariableDeclaration> list = getParameterList();
		list.setChild(node, i);
	}

	/**
	 * Retrieves the Parameter list.
	 * 
	 * @return The node representing the Parameter list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public List<VariableDeclaration> getParameters() {
		return getParameterList();
	}

	/**
	 * Retrieves the Parameter list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Parameter list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public List<VariableDeclaration> getParametersNoTransform() {
		return getParameterListNoTransform();
	}

	/**
	 * Retrieves the Parameter list.
	 * 
	 * @return The node representing the Parameter list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<VariableDeclaration> getParameterList() {
		List<VariableDeclaration> list = (List<VariableDeclaration>) getChild(0);
		list.getNumChild();
		return list;
	}

	/**
	 * Retrieves the Parameter list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Parameter list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<VariableDeclaration> getParameterListNoTransform() {
		return (List<VariableDeclaration>) getChildNoTransform(0);
	}

	/**
	 * Replaces the lexeme ReturnType.
	 * 
	 * @param value
	 *            The new value for the lexeme ReturnType.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setReturnType(Type value) {
		tokenType_ReturnType = value;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	protected Type tokenType_ReturnType;

	/**
	 * Retrieves the value for the lexeme ReturnType.
	 * 
	 * @return The value for the lexeme ReturnType.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public Type getReturnType() {
		return tokenType_ReturnType;
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
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:35
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
		// Procedures with a return type must finish with a return statement:
		if (getReturnType() != Type.Undefined) {
			if (getBody().getNumStatement() == 0)
				return false;
			Statement lastStatement = getBody().getStatement(
					getBody().getNumStatement() - 1);
			if (!(lastStatement instanceof ProcedureReturn))
				return false;
		}
		// The procedure declaration must not be a redeclaration:
		return LookUp(getName()).size() < 2;
	}

	/**
	 * @apilevel internal
	 */
	protected java.util.Map LookUpLocal_String_visited;

	/**
	 * @attribute syn
	 * @aspect NameAnalysis
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:37
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
		if (getName().equals(name))
			result.add(this);
		return result;
	}

	/**
	 * @apilevel internal
	 */
	protected int Type_visited = -1;

	/**
	 * Statements with Type Constraints ** @attribute syn
	 * 
	 * @aspect TypeAnalysis
	 * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:16
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
		Type[] paras = new Type[getNumParameter()];
		for (int i = 0; i < getNumParameter(); i++)
			paras[i] = getParameter(i).Type();
		return Type.newProcedure(paras, getReturnType());
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:23
	 * @apilevel internal
	 */
	public boolean Define_boolean_IsParameterDeclaration(ASTNode caller,
			ASTNode child) {
		if (caller == getParameterListNoTransform()) {
			int index = caller.getIndexOfChild(child);
			return true;
		} else {
			return getParent().Define_boolean_IsParameterDeclaration(this,
					caller);
		}
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:25
	 * @apilevel internal
	 */
	public ProcedureDeclaration Define_ProcedureDeclaration_IsProcedureBody(
			ASTNode caller, ASTNode child) {
		if (caller == getBodyNoTransform()) {
			return this;
		} else {
			return getParent().Define_ProcedureDeclaration_IsProcedureBody(
					this, caller);
		}
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:29
	 * @apilevel internal
	 */
	public ProcedureDeclaration Define_ProcedureDeclaration_ProcedureInContext(
			ASTNode caller, ASTNode child) {
		if (caller == getBodyNoTransform()) {
			return this;
		} else {
			return getParent().Define_ProcedureDeclaration_ProcedureInContext(
					this, caller);
		}
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		return super.rewriteTo();
	}

	/**
	 * @apilevel internal
	 */
	protected int ProcedureDeclaration_Returns_visited = -1;
	/**
	 * @apilevel internal
	 */
	protected boolean ProcedureDeclaration_Returns_computed = false;
	/**
	 * @apilevel internal
	 */
	protected java.util.List<ProcedureReturn> ProcedureDeclaration_Returns_value;

	/**
	 * @attribute coll
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/Core.jrag:44
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public java.util.List<ProcedureReturn> Returns() {
		if (ProcedureDeclaration_Returns_computed) {
			return ProcedureDeclaration_Returns_value;
		}
		ASTNode$State state = state();
		if (ProcedureDeclaration_Returns_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: Returns in class: ");
		ProcedureDeclaration_Returns_visited = state().boundariesCrossed;
		int num = state.boundariesCrossed;
		boolean isFinal = this.is$Final();
		ProcedureDeclaration_Returns_value = Returns_compute();
		if (isFinal && num == state().boundariesCrossed)
			ProcedureDeclaration_Returns_computed = true;
		ProcedureDeclaration_Returns_visited = -1;
		return ProcedureDeclaration_Returns_value;
	}

	java.util.Set ProcedureDeclaration_Returns_contributors;

	public java.util.Set ProcedureDeclaration_Returns_contributors() {
		if (ProcedureDeclaration_Returns_contributors == null)
			ProcedureDeclaration_Returns_contributors = new ASTNode$State.IdentityHashSet(
					4);
		return ProcedureDeclaration_Returns_contributors;
	}

	/**
	 * @apilevel internal
	 */
	private java.util.List<ProcedureReturn> Returns_compute() {
		ASTNode node = this;
		while (node.getParent() != null && !(node instanceof CompilationUnit))
			node = node.getParent();
		CompilationUnit root = (CompilationUnit) node;
		root.collect_contributors_ProcedureDeclaration_Returns();
		ProcedureDeclaration_Returns_value = new ArrayList<ProcedureReturn>();
		if (ProcedureDeclaration_Returns_contributors != null)
			for (java.util.Iterator iter = ProcedureDeclaration_Returns_contributors
					.iterator(); iter.hasNext();) {
				ASTNode contributor = (ASTNode) iter.next();
				contributor
						.contributeTo_ProcedureDeclaration_ProcedureDeclaration_Returns(ProcedureDeclaration_Returns_value);
			}
		return ProcedureDeclaration_Returns_value;
	}

	/**
	 * @apilevel internal
	 */
	protected int ProcedureDeclaration_Decls_visited = -1;
	/**
	 * @apilevel internal
	 */
	protected boolean ProcedureDeclaration_Decls_computed = false;
	/**
	 * @apilevel internal
	 */
	protected java.util.List<Declaration> ProcedureDeclaration_Decls_value;

	/**
	 * @attribute coll
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/Core.jrag:48
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public java.util.List<Declaration> Decls() {
		if (ProcedureDeclaration_Decls_computed) {
			return ProcedureDeclaration_Decls_value;
		}
		ASTNode$State state = state();
		if (ProcedureDeclaration_Decls_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: Decls in class: ");
		ProcedureDeclaration_Decls_visited = state().boundariesCrossed;
		int num = state.boundariesCrossed;
		boolean isFinal = this.is$Final();
		ProcedureDeclaration_Decls_value = Decls_compute();
		if (isFinal && num == state().boundariesCrossed)
			ProcedureDeclaration_Decls_computed = true;
		ProcedureDeclaration_Decls_visited = -1;
		return ProcedureDeclaration_Decls_value;
	}

	java.util.Set ProcedureDeclaration_Decls_contributors;

	public java.util.Set ProcedureDeclaration_Decls_contributors() {
		if (ProcedureDeclaration_Decls_contributors == null)
			ProcedureDeclaration_Decls_contributors = new ASTNode$State.IdentityHashSet(
					4);
		return ProcedureDeclaration_Decls_contributors;
	}

	/**
	 * @apilevel internal
	 */
	private java.util.List<Declaration> Decls_compute() {
		ASTNode node = this;
		while (node.getParent() != null && !(node instanceof CompilationUnit))
			node = node.getParent();
		CompilationUnit root = (CompilationUnit) node;
		root.collect_contributors_ProcedureDeclaration_Decls();
		ProcedureDeclaration_Decls_value = new ArrayList<Declaration>();
		if (ProcedureDeclaration_Decls_contributors != null)
			for (java.util.Iterator iter = ProcedureDeclaration_Decls_contributors
					.iterator(); iter.hasNext();) {
				ASTNode contributor = (ASTNode) iter.next();
				contributor
						.contributeTo_ProcedureDeclaration_ProcedureDeclaration_Decls(ProcedureDeclaration_Decls_value);
			}
		return ProcedureDeclaration_Decls_value;
	}

}
