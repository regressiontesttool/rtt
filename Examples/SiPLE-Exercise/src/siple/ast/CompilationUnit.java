/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.*;

import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @author C. B\u221a\u00barger
 * @production CompilationUnit : {@link ASTNode} ::= <span class="component">
 *             {@link Declaration}*</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:14
 */
public class CompilationUnit extends ASTNode<ASTNode> implements Cloneable {
	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		super.flushCache();
		ASTRoot_visited = -1;
		NodeAddress_visited = -1;
		IsCorrect_visited = -1;
		IsCorrectLocal_visited = -1;
		Interpret_visited = -1;
		LookUp_String_visited = null;
		MainProcedure_visited = -1;
		collect_contributors_ProcedureDeclaration_Returns = false;
		collect_contributors_ProcedureDeclaration_Decls = false;
	}

	/**
	 * @apilevel internal
	 */
	public void flushCollectionCache() {
		super.flushCollectionCache();
		collect_contributors_ProcedureDeclaration_Returns = false;
		collect_contributors_ProcedureDeclaration_Decls = false;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public CompilationUnit clone() throws CloneNotSupportedException {
		CompilationUnit node = (CompilationUnit) super.clone();
		node.ASTRoot_visited = -1;
		node.NodeAddress_visited = -1;
		node.IsCorrect_visited = -1;
		node.IsCorrectLocal_visited = -1;
		node.Interpret_visited = -1;
		node.LookUp_String_visited = null;
		node.MainProcedure_visited = -1;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public CompilationUnit copy() {
		try {
			CompilationUnit node = (CompilationUnit) clone();
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
	public CompilationUnit fullCopy() {
		try {
			CompilationUnit tree = (CompilationUnit) clone();
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
	 * 
	 */
	public CompilationUnit() {
		super();

		setChild(new List(), 0);
		is$Final(true);

	}

	/**
	 * @ast method
	 * 
	 */
	public CompilationUnit(List<Declaration> p0) {
		setChild(p0, 0);
		is$Final(true);
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
	 * Replaces the Declaration list.
	 * 
	 * @param list
	 *            The new list node to be used as the Declaration list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setDeclarationList(List<Declaration> list) {
		setChild(list, 0);
	}

	/**
	 * Retrieves the number of children in the Declaration list.
	 * 
	 * @return Number of children in the Declaration list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public int getNumDeclaration() {
		return getDeclarationList().getNumChild();
	}

	/**
	 * Retrieves the number of children in the Declaration list. Calling this
	 * method will not trigger rewrites..
	 * 
	 * @return Number of children in the Declaration list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public int getNumDeclarationNoTransform() {
		return getDeclarationListNoTransform().getNumChildNoTransform();
	}

	/**
	 * Retrieves the element at index {@code i} in the Declaration list..
	 * 
	 * @param i
	 *            Index of the element to return.
	 * @return The element at position {@code i} in the Declaration list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public Declaration getDeclaration(int i) {
		return (Declaration) getDeclarationList().getChild(i);
	}

	/**
	 * Append an element to the Declaration list.
	 * 
	 * @param node
	 *            The element to append to the Declaration list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void addDeclaration(Declaration node) {
		List<Declaration> list = (parent == null || state == null) ? getDeclarationListNoTransform()
				: getDeclarationList();
		list.addChild(node);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void addDeclarationNoTransform(Declaration node) {
		List<Declaration> list = getDeclarationListNoTransform();
		list.addChild(node);
	}

	/**
	 * Replaces the Declaration list element at index {@code i} with the new
	 * node {@code node}.
	 * 
	 * @param node
	 *            The new node to replace the old list element.
	 * @param i
	 *            The list index of the node to be replaced.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public void setDeclaration(Declaration node, int i) {
		List<Declaration> list = getDeclarationList();
		list.setChild(node, i);
	}

	/**
	 * Retrieves the Declaration list.
	 * 
	 * @return The node representing the Declaration list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	public List<Declaration> getDeclarations() {
		return getDeclarationList();
	}

	/**
	 * Retrieves the Declaration list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Declaration list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public List<Declaration> getDeclarationsNoTransform() {
		return getDeclarationListNoTransform();
	}

	/**
	 * Retrieves the Declaration list.
	 * 
	 * @return The node representing the Declaration list.
	 * @apilevel high-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<Declaration> getDeclarationList() {
		List<Declaration> list = (List<Declaration>) getChild(0);
		list.getNumChild();
		return list;
	}

	/**
	 * Retrieves the Declaration list.
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @return The node representing the Declaration list.
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<Declaration> getDeclarationListNoTransform() {
		return (List<Declaration>) getChildNoTransform(0);
	}

	/**
	 * @ast method
	 * @aspect <NoAspect>
	 * @declaredat ./../../specifications/semantics/Core.jrag:44
	 */
	private boolean collect_contributors_ProcedureDeclaration_Returns = false;

	protected void collect_contributors_ProcedureDeclaration_Returns() {
		if (collect_contributors_ProcedureDeclaration_Returns)
			return;
		super.collect_contributors_ProcedureDeclaration_Returns();
		collect_contributors_ProcedureDeclaration_Returns = true;
	}

	/**
	 * @ast method
	 * @aspect <NoAspect>
	 * @declaredat ./../../specifications/semantics/Core.jrag:48
	 */
	private boolean collect_contributors_ProcedureDeclaration_Decls = false;

	protected void collect_contributors_ProcedureDeclaration_Decls() {
		if (collect_contributors_ProcedureDeclaration_Decls)
			return;
		super.collect_contributors_ProcedureDeclaration_Decls();
		collect_contributors_ProcedureDeclaration_Decls = true;
	}

	/**
	 * @apilevel internal
	 */
	protected int ASTRoot_visited = -1;

	/**
	 * @attribute syn
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:15
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public CompilationUnit ASTRoot() {
		ASTNode$State state = state();
		if (ASTRoot_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: ASTRoot in class: ");
		ASTRoot_visited = state().boundariesCrossed;
		CompilationUnit ASTRoot_value = ASTRoot_compute();
		ASTRoot_visited = -1;
		return ASTRoot_value;
	}

	/**
	 * @apilevel internal
	 */
	private CompilationUnit ASTRoot_compute() {
		return this;
	}

	/**
	 * @apilevel internal
	 */
	protected int NodeAddress_visited = -1;

	/**
	 * @attribute syn
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:17
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public DeweyAddress NodeAddress() {
		ASTNode$State state = state();
		if (NodeAddress_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: NodeAddress in class: ");
		NodeAddress_visited = state().boundariesCrossed;
		DeweyAddress NodeAddress_value = NodeAddress_compute();
		NodeAddress_visited = -1;
		return NodeAddress_value;
	}

	/**
	 * @apilevel internal
	 */
	private DeweyAddress NodeAddress_compute() {
		return new DeweyAddress().addAddressPart(1);
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrect_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:24
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public boolean IsCorrect() {
		ASTNode$State state = state();
		if (IsCorrect_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: IsCorrect in class: ");
		IsCorrect_visited = state().boundariesCrossed;
		boolean IsCorrect_value = IsCorrect_compute();
		IsCorrect_visited = -1;
		return IsCorrect_value;
	}

	/**
	 * @apilevel internal
	 */
	private boolean IsCorrect_compute() {
		return super.IsCorrect();
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:28
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
		if (MainProcedure() == null)
			return false;
		ProcedureDeclaration main = MainProcedure();
		return main.getNumParameter() == 0
				&& main.getReturnType() == Type.Undefined;
	}

	/**
	 * @apilevel internal
	 */
	protected int Interpret_visited = -1;

	/**
	 * Program Interpretation * @attribute syn
	 * 
	 * @aspect Interpretation
	 * @declaredat ./../../specifications/semantics/Interpretation.jrag:16
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public State Interpret() {
		ASTNode$State state = state();
		if (Interpret_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: Interpret in class: ");
		Interpret_visited = state().boundariesCrossed;
		State Interpret_value = Interpret_compute();
		Interpret_visited = -1;
		return Interpret_value;
	}
	
	/**
	 * @apilevel internal
	 */
	private State Interpret_compute() {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		State vm = new State();
		// Allocate all global variables:
		for (int i = 0; i < getNumDeclaration(); i++)
			getDeclaration(i).Interpret(vm);
		// Prepare the main procedure's execution environment:
		Frame envPrototype = (Frame) vm.access(MainProcedure()).value;
		Frame newEnv = new Frame();
		newEnv.implementation = envPrototype.implementation;
		newEnv.closure = envPrototype.closure;
		Frame oldEnv = vm.currentFrame;
		vm.currentFrame = newEnv;
		// Execute the main procedure:
		newEnv.implementation.getBody().Interpret(vm);
		// Restore the old execution environment:
		vm.currentFrame = oldEnv;
		return vm;
	}

	/**
	 * @apilevel internal
	 */
	protected java.util.Map LookUp_String_visited;

	/**
	 * Ordinary Name Analysis * @attribute syn
	 * 
	 * @aspect NameAnalysis
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:16
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public LinkedList<Declaration> LookUp(String name) {
		Object _parameters = name;
		if (LookUp_String_visited == null)
			LookUp_String_visited = new java.util.HashMap(4);
		ASTNode$State state = state();
		if (Integer.valueOf(state().boundariesCrossed).equals(
				LookUp_String_visited.get(_parameters)))
			throw new RuntimeException(
					"Circular definition of attr: LookUp in class: ");
		LookUp_String_visited.put(_parameters,
				Integer.valueOf(state().boundariesCrossed));
		LinkedList<Declaration> LookUp_String_value = LookUp_compute(name);
		LookUp_String_visited.remove(_parameters);
		return LookUp_String_value;
	}

	/**
	 * @apilevel internal
	 */
	private LinkedList<Declaration> LookUp_compute(String name) {
		LinkedList<Declaration> result = new LinkedList<Declaration>();
		for (int i = 0; i < getNumDeclaration(); i++)
			// Declared within compilation unit
			result.addAll(getDeclaration(i).LookUpLocal(name));
		return result;
	}

	/**
	 * @apilevel internal
	 */
	protected int MainProcedure_visited = -1;

	/**
	 * @attribute syn
	 * @aspect NameAnalysis
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:50
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare
	public ProcedureDeclaration MainProcedure() {
		ASTNode$State state = state();
		if (MainProcedure_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: MainProcedure in class: ");
		MainProcedure_visited = state().boundariesCrossed;
		ProcedureDeclaration MainProcedure_value = MainProcedure_compute();
		MainProcedure_visited = -1;
		return MainProcedure_value;
	}

	/**
	 * @apilevel internal
	 */
	private ProcedureDeclaration MainProcedure_compute() {
		LinkedList<Declaration> scope = LookUp("main");
		if (scope.size() == 1 && scope.get(0) instanceof ProcedureDeclaration)
			return (ProcedureDeclaration) scope.get(0);
		return null;
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:22
	 * @apilevel internal
	 */
	public boolean Define_boolean_IsParameterDeclaration(ASTNode caller,
			ASTNode child) {
		if (caller == getDeclarationListNoTransform()) {
			int index = caller.getIndexOfChild(child);
			return false;
		} else {
			return getParent().Define_boolean_IsParameterDeclaration(this,
					caller);
		}
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:28
	 * @apilevel internal
	 */
	public ProcedureDeclaration Define_ProcedureDeclaration_ProcedureInContext(
			ASTNode caller, ASTNode child) {
		if (caller == getDeclarationListNoTransform()) {
			int index = caller.getIndexOfChild(child);
			return null;
		} else {
			return getParent().Define_ProcedureDeclaration_ProcedureInContext(
					this, caller);
		}
	}

	/**
	 * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:22
	 * @apilevel internal
	 */
	public LinkedList<Declaration> Define_LinkedList_Declaration__LookUp(
			ASTNode caller, ASTNode child, String name) {
		if (caller == getDeclarationListNoTransform()) {
			int index = caller.getIndexOfChild(child);
			return LookUp(name);
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
