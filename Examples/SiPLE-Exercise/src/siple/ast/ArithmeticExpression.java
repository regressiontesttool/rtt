/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production ArithmeticExpression : {@link BinaryExpression};
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:66
 */
public abstract class ArithmeticExpression extends BinaryExpression implements Cloneable {
  /**
   * @apilevel low-level
   */
  public void flushCache() {
    super.flushCache();
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
  @SuppressWarnings({"unchecked", "cast"})
  public ArithmeticExpression clone() throws CloneNotSupportedException {
    ArithmeticExpression node = (ArithmeticExpression)super.clone();
    node.Type_visited = -1;
    node.in$Circle(false);
    node.is$Final(false);
    return node;
  }
  /**
   * @ast method 
   * 
   */
  public ArithmeticExpression() {
    super();


  }
  /**
   * @ast method 
   * 
   */
  public ArithmeticExpression(Expression p0, Expression p1) {
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
  protected int Type_visited = -1;
  /**
   * @attribute syn
   * @aspect TypeAnalysis
   * @declaredat ./../../specifications/semantics/TypeAnalysis.jrag:106
   */
  @SuppressWarnings({"unchecked", "cast"})
  public Type Type() {
      ASTNode$State state = state();
    if(Type_visited == state().boundariesCrossed)
      throw new RuntimeException("Circular definition of attr: Type in class: ");
    Type_visited = state().boundariesCrossed;
    Type Type_value = Type_compute();
    Type_visited = -1;
    return Type_value;
  }
  /**
   * @apilevel internal
   */
  private Type Type_compute() {  return Type.bequals(getOperand1().Type(), getOperand2().Type()) &&
		(getOperand1().Type().isReal() ||
		getOperand1().Type().isInteger()) ?
			getOperand1().Type() :
			Type.ERROR_TYPE;  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {
    return super.rewriteTo();
  }
}
