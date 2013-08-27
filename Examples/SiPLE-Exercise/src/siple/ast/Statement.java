/* This file was generated with JastAdd2 (http://jastadd.org) version R20121112 (r872) */
package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;
/**
 * @production Statement : {@link ASTNode};
 * @ast node
 * @declaredat ./../../specifications/semantics/ast.ast:16
 */
public abstract class Statement extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @apilevel low-level
   */
  public void flushCache() {
    super.flushCache();
    IsCorrect_visited = -1;
    IsCorrectLocal_visited = -1;
    LookUpLocal_String_visited = null;
    ProcedureInContext_visited = -1;
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
  public Statement clone() throws CloneNotSupportedException {
    Statement node = (Statement)super.clone();
    node.IsCorrect_visited = -1;
    node.IsCorrectLocal_visited = -1;
    node.LookUpLocal_String_visited = null;
    node.ProcedureInContext_visited = -1;
    node.in$Circle(false);
    node.is$Final(false);
    return node;
  }
  /**
   * @ast method 
   * @aspect Interpretation
   * @declaredat ./../../specifications/semantics/Core.jrag:123
   */
  public abstract void Interpret(State vm)
		throws InterpretationException;
  /**
   * @ast method 
   * 
   */
  public Statement() {
    super();


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
   * @apilevel internal
   */
  protected int IsCorrect_visited = -1;
  /**
   * @attribute syn
   * @aspect ConstraintChecking
   * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:25
   */
  @SuppressWarnings({"unchecked", "cast"})
  public boolean IsCorrect() {
      ASTNode$State state = state();
    if(IsCorrect_visited == state().boundariesCrossed)
      throw new RuntimeException("Circular definition of attr: IsCorrect in class: ");
    IsCorrect_visited = state().boundariesCrossed;
    boolean IsCorrect_value = IsCorrect_compute();
    IsCorrect_visited = -1;
    return IsCorrect_value;
  }
  /**
   * @apilevel internal
   */
  private boolean IsCorrect_compute() {  return super.IsCorrect();  }
  /**
   * @apilevel internal
   */
  protected int IsCorrectLocal_visited = -1;
  /**
   * @attribute syn
   * @aspect ConstraintChecking
   * @declaredat ./../../specifications/semantics/ConstraintChecking.jrag:26
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
  private boolean IsCorrectLocal_compute() {  return super.IsCorrectLocal();  }
  /**
   * @apilevel internal
   */
  protected java.util.Map LookUpLocal_String_visited;
  /**
   * @attribute syn
   * @aspect NameAnalysis
   * @declaredat ./../../specifications/semantics/NameAnalysis.jrag:35
   */
  @SuppressWarnings({"unchecked", "cast"})
  public LinkedList<Declaration> LookUpLocal(String name) {
    Object _parameters = name;
    if(LookUpLocal_String_visited == null) LookUpLocal_String_visited = new java.util.HashMap(4);
      ASTNode$State state = state();
    if(Integer.valueOf(state().boundariesCrossed).equals(LookUpLocal_String_visited.get(_parameters)))
      throw new RuntimeException("Circular definition of attr: LookUpLocal in class: ");
    LookUpLocal_String_visited.put(_parameters, Integer.valueOf(state().boundariesCrossed));
    LinkedList<Declaration> LookUpLocal_String_value = LookUpLocal_compute(name);
    LookUpLocal_String_visited.remove(_parameters);
    return LookUpLocal_String_value;
  }
  /**
   * @apilevel internal
   */
  private LinkedList<Declaration> LookUpLocal_compute(String name) {  return new LinkedList<Declaration>();  }
  /**
   * @apilevel internal
   */
  protected int ProcedureInContext_visited = -1;
  /**
   * @attribute inh
   * @aspect AccessSupport
   * @declaredat ./../../specifications/semantics/Core.jrag:41
   */
  @SuppressWarnings({"unchecked", "cast"})
  public ProcedureDeclaration ProcedureInContext() {
      ASTNode$State state = state();
    if(ProcedureInContext_visited == state().boundariesCrossed)
      throw new RuntimeException("Circular definition of attr: ProcedureInContext in class: ");
    ProcedureInContext_visited = state().boundariesCrossed;
    ProcedureDeclaration ProcedureInContext_value = getParent().Define_ProcedureDeclaration_ProcedureInContext(this, null);
    ProcedureInContext_visited = -1;
    return ProcedureInContext_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {
    return super.rewriteTo();
  }
}
