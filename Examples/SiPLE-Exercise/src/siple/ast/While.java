/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;
/**
 * @production While : {@link Statement} ::= <span class="component">Condition:{@link Expression}</span> <span class="component">Body:{@link Block}</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:20
 */
public class While extends Statement implements Cloneable {
  /**
   * @apilevel low-level
   */
  public void flushCache() {
    super.flushCache();
    IsCorrectLocal_visited = -1;
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
  @SuppressWarnings({"unchecked", "cast"})
  public While clone() throws CloneNotSupportedException {
    While node = (While)super.clone();
    node.IsCorrectLocal_visited = -1;
    node.in$Circle(false);
    node.is$Final(false);
    return node;
  }
  /**
   * @apilevel internal
   */
  @SuppressWarnings({"unchecked", "cast"})
  public While copy() {
      try {
        While node = (While)clone();
        if(children != null) node.children = (ASTNode[])children.clone();
        return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   */
  @SuppressWarnings({"unchecked", "cast"})
  public While fullCopy() {
    try {
      While tree = (While) clone();
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
      throw new Error("Error: clone not supported for " +
        getClass().getName());
    }
  }
  /**
   * @ast method 
   * @aspect Interpretation
   * @declaredat ./../../specifications/semantics/Interpretation.jrag:65
   */
  public void Interpret(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		while ((Boolean)getCondition().Value(vm)) {
			getBody().Interpret(vm);
			if (vm.currentFrame.returnValue != null)
				return;
		}
	}
  /**
   * @ast method 
   * 
   */
  public While() {
    super();


  }
  /**
   * @ast method 
   * 
   */
  public While(Expression p0, Block p1) {
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
   * Replaces the Condition child.
   * @param node The new node to replace the Condition child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public void setCondition(Expression node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Condition child.
   * @return The current node used as the Condition child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public Expression getCondition() {
    return (Expression)getChild(0);
  }
  /**
   * Retrieves the Condition child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Condition child.
   * @apilevel low-level
   * @ast method 
   * 
   */
  public Expression getConditionNoTransform() {
    return (Expression)getChildNoTransform(0);
  }
  /**
   * Replaces the Body child.
   * @param node The new node to replace the Body child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public void setBody(Block node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Body child.
   * @return The current node used as the Body child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public Block getBody() {
    return (Block)getChild(1);
  }
  /**
   * Retrieves the Body child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Body child.
   * @apilevel low-level
   * @ast method 
   * 
   */
  public Block getBodyNoTransform() {
    return (Block)getChildNoTransform(1);
  }
  /**
   * @apilevel internal
   */
  protected int IsCorrectLocal_visited = -1;
  /**
   * @attribute syn
   * @aspect ConstraintChecking
   * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:51
   */
  @SuppressWarnings({"unchecked", "cast"})
  public boolean IsCorrectLocal() {
      ASTNode$State state = state();
    if(IsCorrectLocal_visited == state().boundariesCrossed)
      throw new RuntimeException("Circular definition of attr: IsCorrectLocal in class: ");
    IsCorrectLocal_visited = state().boundariesCrossed;
    boolean IsCorrectLocal_value = IsCorrectLocal_compute();
    IsCorrectLocal_visited = -1;
    return IsCorrectLocal_value;
  }
  /**
   * @apilevel internal
   */
  private boolean IsCorrectLocal_compute() {  return getCondition().Type() == Type.Boolean;  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {
    return super.rewriteTo();
  }
}
