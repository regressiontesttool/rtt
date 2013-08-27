/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;

/**
 * @production BinaryExpression : {@link Expression} ::= <span class="component">Operand1:{@link Expression}</span> <span class="component">Operand2:{@link Expression}</span>;
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:49
 */
public abstract class BinaryExpression extends Expression implements Cloneable {
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
  public BinaryExpression clone() throws CloneNotSupportedException {
    BinaryExpression node = (BinaryExpression)super.clone();
    node.in$Circle(false);
    node.is$Final(false);
    return node;
  }
  /**
   * @ast method 
   * @aspect TypeCoercions
   * @declaredat ./../../specifications/semantics/TypeCoercions.jrag:18
   */
  
	// Coercion can only happen once! The following hack --- to use a flag to
	// test for rewrite applicability --- avoids horrible execution times and
	// memory requirements because of some JastAdd issues.
	
	private boolean coercionTested = false;
  /**
   * @ast method 
   * 
   */
  public BinaryExpression() {
    super();


  }
  /**
   * @ast method 
   * 
   */
  public BinaryExpression(Expression p0, Expression p1) {
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
    // Declared in ./../../specifications/semantics/TypeCoercions.jrag at line 22
    if(!coercionTested) {
      state().duringTypeCoercions++;
      ASTNode result = rewriteRule0();
      state().duringTypeCoercions--;
      return result;
    }

    return super.rewriteTo();
  }
  /**
   * @declaredat ./../../specifications/semantics/TypeCoercions.jrag:22
   * @apilevel internal
   */  private BinaryExpression rewriteRule0() {
{
			coercionTested = true;
			if (getOperand1().Type().isReal() &&
					getOperand2().Type().isInteger())
				setOperand2(new RealCoercion(getOperand2()));
			else if (getOperand1().Type().isInteger() &&
					getOperand2().Type().isReal())
				setOperand1(new RealCoercion(getOperand1()));
			return this;
		}  }
}
