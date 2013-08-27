/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;

import rtt.annotations.Parser;

import siple.semantics.*;

/**
 * @production ASTNode;
 * @ast node
 * 
 */
@Parser.Node
public class ASTNode<T extends ASTNode> implements Cloneable,
		Comparable<ASTNode>, Iterable<T> {
	/**
	 * @apilevel low-level
	 */
	public ASTNode[] children;

	@Parser.Node.Child
	public java.util.List<ASTNode> getChildren() {
		java.util.List<ASTNode> result = new java.util.LinkedList<ASTNode>();
		for (int i = 0; i < getNumChild(); i++)
			result.add(getChild(i));
		return result;
	}

	public String toString() {
		String type = getClass().toString();
		if (type.endsWith("Impl"))
			type = type.substring(0, type.length() - 4);
		type = type.substring(type.lastIndexOf('.') + 1);
		if (type.equals("ASTList"))
			type = "List";
		return "(Node-Type: " + type + " | Node-Address: " + NodeAddress()
				+ ")";
	}

	/**
	 * @apilevel low-level
	 */
	public void flushCache() {
		ASTRoot_visited = -1;
		IsCorrect_visited = -1;
		IsCorrectLocal_visited = -1;
		NodeAddress_visited = -1;
		LookUp_String_visited = null;
	}

	/**
	 * @apilevel internal
	 */
	public void flushCollectionCache() {
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public ASTNode<T> clone() throws CloneNotSupportedException {
		ASTNode node = (ASTNode) super.clone();
		node.ASTRoot_visited = -1;
		node.IsCorrect_visited = -1;
		node.IsCorrectLocal_visited = -1;
		node.NodeAddress_visited = -1;
		node.LookUp_String_visited = null;
		node.in$Circle(false);
		node.is$Final(false);
		return node;
	}

	/**
	 * @apilevel internal
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public ASTNode<T> copy() {
		try {
			ASTNode node = (ASTNode) clone();
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
	public ASTNode<T> fullCopy() {
		try {
			ASTNode tree = (ASTNode) clone();
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
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/Core.jrag:26
	 */
	public int compareTo(ASTNode o) {
		return o.NodeAddress().compareTo(NodeAddress());
	}

	/**
	 * @ast method
	 * 
	 */
	public ASTNode() {
		super();

	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	public static final boolean generatedWithCircularEnabled = true;
	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	public static final boolean generatedWithCacheCycle = true;
	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	public static final boolean generatedWithComponentCheck = false;
	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	protected static ASTNode$State state = new ASTNode$State();

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public final ASTNode$State state() {
		return state;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	public boolean in$Circle = false;

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public boolean in$Circle() {
		return in$Circle;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public void in$Circle(boolean b) {
		in$Circle = b;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	public boolean is$Final = false;

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public boolean is$Final() {
		return is$Final;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */
	public void is$Final(boolean b) {
		is$Final = b;
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings("cast")
	public T getChild(int i) {
		return (T) ASTNode.getChild(this, i);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public static ASTNode getChild(ASTNode that, int i) {
		ASTNode node = that.getChildNoTransform(i);
		if (node.is$Final())
			return node;
		if (!node.mayHaveRewrite()) {
			node.is$Final(that.is$Final());
			return node;
		}
		if (!node.in$Circle()) {
			int rewriteState;
			int num = that.state().boundariesCrossed;
			do {
				that.state().push(ASTNode$State.REWRITE_CHANGE);
				ASTNode oldNode = node;
				oldNode.in$Circle(true);
				node = node.rewriteTo();
				if (node != oldNode)
					that.setChild(node, i);
				oldNode.in$Circle(false);
				rewriteState = that.state().pop();
			} while (rewriteState == ASTNode$State.REWRITE_CHANGE);
			if (rewriteState == ASTNode$State.REWRITE_NOCHANGE
					&& that.is$Final()) {
				node.is$Final(true);
				that.state().boundariesCrossed = num;
			}
		} else if (that.is$Final() != node.is$Final())
			that.state().boundariesCrossed++;
		return node;
	}

	/**
	 * @apilevel internal
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel internal
	 */
	private int childIndex;

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public int getIndexOfChild(ASTNode node) {
		if (node != null && node.childIndex < getNumChildNoTransform()
				&& node == getChildNoTransform(node.childIndex))
			return node.childIndex;
		for (int i = 0; i < getNumChildNoTransform(); i++)
			if (getChildNoTransform(i) == node) {
				node.childIndex = i;
				return i;
			}
		return -1;
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void addChild(T node) {
		setChild(node, getNumChildNoTransform());
	}

	/**
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	@SuppressWarnings("cast")
	public final T getChildNoTransform(int i) {
		return (T) (children != null ? children[i] : null);
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel low-level
	 */
	protected int numChildren;

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	protected int numChildren() {
		return numChildren;
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public int getNumChild() {
		return numChildren();
	}

	/**
	 * <p>
	 * <em>This method does not invoke AST transformations.</em>
	 * </p>
	 * 
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public final int getNumChildNoTransform() {
		return numChildren();
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void setChild(ASTNode node, int i) {
		if (children == null) {
			children = new ASTNode[i + 1];
		} else if (i >= children.length) {
			ASTNode c[] = new ASTNode[i << 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = node;
		if (i >= numChildren)
			numChildren = i + 1;
		if (node != null) {
			node.setParent(this);
			node.childIndex = i;
		}
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void insertChild(ASTNode node, int i) {
		if (children == null) {
			children = new ASTNode[i + 1];
			children[i] = node;
		} else {
			ASTNode c[] = new ASTNode[children.length + 1];
			System.arraycopy(children, 0, c, 0, i);
			c[i] = node;
			if (i < children.length) {
				System.arraycopy(children, i, c, i + 1, children.length - i);
				for (int j = i + 1; j < c.length; ++j) {
					if (c[j] != null)
						c[j].childIndex = j;
				}
			}
			children = c;
		}
		numChildren++;
		if (node != null) {
			node.setParent(this);
			node.childIndex = i;
		}
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void removeChild(int i) {
		if (children != null) {
			ASTNode child = (ASTNode) children[i];
			if (child != null) {
				child.setParent(null);
				child.childIndex = -1;
			}
			System.arraycopy(children, i + 1, children, i, children.length - i
					- 1);
			numChildren--;
			for (int j = i; j < numChildren; ++j) {
				if (children[j] != null) {
					child = (ASTNode) children[j];
					child.childIndex = j;
				}
			}
		}
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public ASTNode getParent() {
		if (parent != null && ((ASTNode) parent).is$Final() != is$Final()) {
			state().boundariesCrossed++;
		}
		return (ASTNode) parent;
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public void setParent(ASTNode node) {
		parent = node;
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */

	/**
	 * @apilevel low-level
	 */
	protected ASTNode parent;

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */

	/**
	 * @ast method
	 * 
	 */
	protected boolean duringTypeCoercions() {
		if (state().duringTypeCoercions == 0) {
			return false;
		} else {
			state().pop();
			state().push(ASTNode$State.REWRITE_INTERRUPT);
			return true;
		}
	}

	/**
	 * @apilevel low-level
	 * @ast method
	 * 
	 */
	public java.util.Iterator<T> iterator() {
		return new java.util.Iterator<T>() {
			private int counter = 0;

			public boolean hasNext() {
				return counter < getNumChild();
			}

			@SuppressWarnings("unchecked")
			public T next() {
				if (hasNext())
					return (T) getChild(counter++);
				else
					return null;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
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
	 * @ast method
	 * @aspect <NoAspect>
	 * @declaredat ./../../specifications/semantics/Core.jrag:44
	 */
	protected void collect_contributors_ProcedureDeclaration_Returns() {
		for (int i = 0; i < getNumChild(); i++)
			getChild(i).collect_contributors_ProcedureDeclaration_Returns();
	}

	protected void contributeTo_ProcedureDeclaration_ProcedureDeclaration_Returns(
			java.util.List<ProcedureReturn> collection) {
	}

	/**
	 * @ast method
	 * @aspect <NoAspect>
	 * @declaredat ./../../specifications/semantics/Core.jrag:48
	 */
	protected void collect_contributors_ProcedureDeclaration_Decls() {
		for (int i = 0; i < getNumChild(); i++)
			getChild(i).collect_contributors_ProcedureDeclaration_Decls();
	}

	protected void contributeTo_ProcedureDeclaration_ProcedureDeclaration_Decls(
			java.util.List<Declaration> collection) {
	}

	/**
	 * @apilevel internal
	 */
	protected int ASTRoot_visited = -1;

	/**
	 * @attribute syn
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:14
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
		return getParent().ASTRoot();
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrect_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:14
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
		if (!IsCorrectLocal())
			return false;
		for (int i = 0; i < getNumChild(); i++)
			if (!getChild(i).IsCorrect())
				return false;
		return true;
	}

	/**
	 * @apilevel internal
	 */
	protected int IsCorrectLocal_visited = -1;

	/**
	 * @attribute syn
	 * @aspect ConstraintChecking
	 * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:22
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Parser.Node.Compare
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
		return true;
	}

	/**
	 * @apilevel internal
	 */
	protected int NodeAddress_visited = -1;

	/**
	 * @attribute inh
	 * @aspect AccessSupport
	 * @declaredat ./../../specifications/semantics/Core.jrag:33
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public DeweyAddress NodeAddress() {
		ASTNode$State state = state();
		if (NodeAddress_visited == state().boundariesCrossed)
			throw new RuntimeException(
					"Circular definition of attr: NodeAddress in class: ");
		NodeAddress_visited = state().boundariesCrossed;
		DeweyAddress NodeAddress_value = getParent()
				.Define_DeweyAddress_NodeAddress(this, null);
		NodeAddress_visited = -1;
		return NodeAddress_value;
	}

	/**
	 * @apilevel internal
	 */
	protected java.util.Map LookUp_String_visited;

	/**
	 * @attribute inh
	 * @aspect NameAnalysis
	 * @declaredat ./../../specifications/semantics/Core.jrag:71
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
		LinkedList<Declaration> LookUp_String_value = getParent()
				.Define_LinkedList_Declaration__LookUp(this, null, name);
		LookUp_String_visited.remove(_parameters);
		return LookUp_String_value;
	}

	/**
	 * @declaredat ./../../specifications/semantics/AccessSupport.jrag:19
	 * @apilevel internal
	 */
	public DeweyAddress Define_DeweyAddress_NodeAddress(ASTNode caller,
			ASTNode child) {
		{
			int index = this.getIndexOfChild(caller);
			return NodeAddress().clone().addAddressPart(index + 1);
		}
	}

	/**
	 * @apilevel internal
	 */
	public ASTNode rewriteTo() {
		if (state().peek() == ASTNode$State.REWRITE_CHANGE) {
			state().pop();
			state().push(ASTNode$State.REWRITE_NOCHANGE);
		}
		return this;
	}

	/**
	 * @apilevel internal
	 */
	public boolean Define_boolean_IsParameterDeclaration(ASTNode caller,
			ASTNode child) {
		return getParent().Define_boolean_IsParameterDeclaration(this, caller);
	}

	/**
	 * @apilevel internal
	 */
	public ProcedureDeclaration Define_ProcedureDeclaration_ProcedureInContext(
			ASTNode caller, ASTNode child) {
		return getParent().Define_ProcedureDeclaration_ProcedureInContext(this,
				caller);
	}

	/**
	 * @apilevel internal
	 */
	public LinkedList<Declaration> Define_LinkedList_Declaration__LookUp(
			ASTNode caller, ASTNode child, String name) {
		return getParent().Define_LinkedList_Declaration__LookUp(this, caller,
				name);
	}

	/**
	 * @apilevel internal
	 */
	public ProcedureDeclaration Define_ProcedureDeclaration_IsProcedureBody(
			ASTNode caller, ASTNode child) {
		return getParent().Define_ProcedureDeclaration_IsProcedureBody(this,
				caller);
	}
}
