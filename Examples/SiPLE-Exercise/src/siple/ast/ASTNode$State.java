package siple.ast;

import java.util.*;
import siple.*;
import siple.semantics.*;
import siple.semantics.State.*;
/**
 * @apilevel internal
  * @ast class
 * 
 */
public class ASTNode$State extends java.lang.Object {

/**
 * @apilevel internal
 */
  public boolean IN_CIRCLE = false;


/**
 * @apilevel internal
 */
  public int CIRCLE_INDEX = 1;


/**
 * @apilevel internal
 */
  public boolean CHANGE = false;


/**
 * @apilevel internal
 */
  public boolean LAST_CYCLE = false;


/**
 * @apilevel internal
 */
  public boolean RESET_CYCLE = false;


  /**
   * @apilevel internal
   */
  static public class CircularValue {
    Object value;
    int visited = -1;
  }


  public static final int REWRITE_CHANGE = 1;


  public static final int REWRITE_NOCHANGE = 2;


  public static final int REWRITE_INTERRUPT = 3;


  public int boundariesCrossed = 0;



  private int[] stack;


  private int pos;


  public ASTNode$State() {
      stack = new int[64];
      pos = 0;
  }


  private void ensureSize(int size) {
      if(size < stack.length)
        return;
      int[] newStack = new int[stack.length * 2];
      System.arraycopy(stack, 0, newStack, 0, stack.length);
      stack = newStack;
  }


  public void push(int i) {
    ensureSize(pos+1);
    stack[pos++] = i;
  }


  public int pop() {
    return stack[--pos];
  }


  public int peek() {
    return stack[pos-1];
  }


  /**
   * @apilevel internal
   */
  static class IdentityHashSet extends java.util.AbstractSet implements java.util.Set {
    public IdentityHashSet(int initialCapacity) {
      map = new java.util.IdentityHashMap(initialCapacity);
      }
    private java.util.IdentityHashMap map;
    private static final Object PRESENT = new Object();
    public java.util.Iterator iterator() { return map.keySet().iterator(); }
    public int size() { return map.size(); }
    public boolean isEmpty() { return map.isEmpty(); }
    public boolean contains(Object o) { return map.containsKey(o); }
    public boolean add(Object o) { return map.put(o, PRESENT)==null; }
    public boolean remove(Object o) { return map.remove(o)==PRESENT; }
    public void clear() { map.clear(); }
  }


  protected int duringTypeCoercions = 0;

public void reset() {
    IN_CIRCLE = false;
    CIRCLE_INDEX = 1;
    CHANGE = false;
    LAST_CYCLE = false;
    boundariesCrossed = 0;
    if(duringTypeCoercions != 0) {
      System.out.println("Warning: resetting duringTypeCoercions");
      duringTypeCoercions = 0;
    }
  }


}
