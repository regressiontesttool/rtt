/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production Multiplication : {@link ArithmeticExpression};
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:70
 */
public class Multiplication extends ArithmeticExpression implements Cloneable {
  /**
   * @apilevel low-level
   */
  public void flushCache() {
    super.flushCache();
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
  public Multiplication clone() throws CloneNotSupportedException {
    Multiplication node = (Multiplication)super.clone();
    node.in$Circle(false);
    node.is$Final(false);
    return node;
  }
  /**
   * @apilevel internal
   */
  @SuppressWarnings({"unchecked", "cast"})
  public Multiplication copy() {
      try {
        Multiplication node = (Multiplication)clone();
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
  public Multiplication fullCopy() {
    try {
      Multiplication tree = (Multiplication) clone();
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
   * @declaredat ./../../specifications/semantics/Interpretation.jrag:259
   */
  public Object Value(State vm) {
		if (!IsCorrectLocal())
			throw new InterpretationException();
		switch (Type().domain) {
		case Integer: return (Integer)getOperand1().Value(vm) *
			(Integer)getOperand2().Value(vm);
		default: return (Float)getOperand1().Value(vm) *
			(Float)getOperand2().Value(vm);}
	}
  /**
   * @ast method 
   * 
   */
  public Multiplication() {
    super();


  }
  /**
   * @ast method 
   * 
   */
  public Multiplication(Expression p0, Expression p1) {
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
    return true;
  }
  /**
   * Replaces the Operand1 child.
   * @param node The new node to replace the Operand1 child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public void setOperand1(Expression node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Operand1 child.
   * @return The current node used as the Operand1 child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public Expression getOperand1() {
    return (Expression)getChild(0);
  }
  /**
   * Retrieves the Operand1 child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Operand1 child.
   * @apilevel low-level
   * @ast method 
   * 
   */
  public Expression getOperand1NoTransform() {
    return (Expression)getChildNoTransform(0);
  }
  /**
   * Replaces the Operand2 child.
   * @param node The new node to replace the Operand2 child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public void setOperand2(Expression node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Operand2 child.
   * @return The current node used as the Operand2 child.
   * @apilevel high-level
   * @ast method 
   * 
   */
  public Expression getOperand2() {
    return (Expression)getChild(1);
  }
  /**
   * Retrieves the Operand2 child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Operand2 child.
   * @apilevel low-level
   * @ast method 
   * 
   */
  public Expression getOperand2NoTransform() {
    return (Expression)getChildNoTransform(1);
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {
    return super.rewriteTo();
  }
}
