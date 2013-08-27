/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package siple.semantics;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;


import siple.syntax.SiPLELexer;
import siple.syntax.SiPLEParser;
import siple.syntax.AbstractParser.Exception;
import siple.syntax.SiPLEParser.AltGoals;

/**
 * Instances of this class represent <i>SiPLE</i> types. Constraints:<br>
 * 1) Iff {@link Type#domain} <tt>==</tt> {@link Domains#Procedure} or
 * {@link Domains#Pointer}, {@link #rtype} must not be <tt>null</tt>,
 * otherwise {@link #rtype} must be <tt>null</tt>.<br>
 * 2) Iff {@link Type#domain} <tt>==</tt> {@link Domains#Pointer},
 * {@link #rtype} must not be {@link #Undefined}.
 * @author C. Bürger
 */
public class Type {
	/**
	 * Enumeration of SiPLE's basic type domains.
	 * @author C. Bürger
	 */
	public static enum Domains {
		Boolean,
		Integer,
		Real,
		Pointer,
		Procedure,
		Undefined,
		ERROR_TYPE
	}
	
	/**
	 * The type's base type restricting its domain.
	 */
	public Domains domain = null;
	
	/**
	 * The parameter types, iff the {@link #domain base type} is
	 * {@link Domains#Procedure}.
	 */
	public Type[] paras = {};
	
	/**
	 * The return type, iff the {@link #domain base type} is
	 * {@link Domains#Procedure} or {@link Domains#Pointer}.
	 */
	public Type rtype = null;
	
	private Type(Domains domain) {this.domain = domain;}
	
	/** Prototype representing a boolean type. */
	public static final Type Boolean = new Type(Domains.Boolean);
	/** Prototype representing an integer type. */
	public static final Type Integer = new Type(Domains.Integer);
	/** Prototype representing a real type. */
	public static final Type Real = new Type(Domains.Real);
	/** Prototype representing an undefined type. */
	public static final Type Undefined = new Type(Domains.Undefined);
	/** Prototype representing an error type. */
	public static final Type ERROR_TYPE = new Type(Domains.ERROR_TYPE);
	
	/**
	 * Parses a given value string to a concrete instance of this class. It is called by the 
	 * basic EMF EFactory to create instances through the EDataType Binding. 
	 *
	 * @param value String representation of an instance of Type.
	 * @return A corresponding instance.
	 */
	public static Type valueOf(String value){
	if("Integer".equals(value)){
			return Integer;
		}
		else if("Boolean".equals(value)){
			return Boolean;
		}
		else if("Real".equals(value)){
			return Real;
		}  
		else if(value.startsWith("Pointer") || value.startsWith("Procedure")){
			SiPLELexer scanner = new SiPLELexer(new StringReader(value));
			SiPLEParser parser = new SiPLEParser();	
			try {
				Type type = (Type)parser.parse(scanner,SiPLEParser.AltGoals.Type);
				return type;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		//consider pointers and procedure types too!	
		return  Undefined;
	}
	
	/**
	 * Construct a pointer for the given type.
	 * @param rtype Type the resulting pointer points to.
	 * @return Pointer of type "pointer to <tt>rtype</tt>".
	 */
	public static Type newPointer(Type rtype) {
		Type type = new Type(Domains.Pointer);
		type.rtype = rtype;
		return type;
	}
	
	/**
	 * Construct a procedure for the given parameter and return types.
	 * @param paras Array of parameter types.
	 * @param rtype Return type.
	 * @return Procedure of type "procedure with parameters <tt>paras</tt>
	 * and return type <tt>rtype</tt>".
	 */
	public static Type newProcedure(Type[] paras, Type rtype) {
		Type type = new Type(Domains.Procedure);
		type.paras = paras;
		type.rtype = rtype;
		return type;
	}
	
	/**
	 * Return, if this type represents a boolean.
	 * @return <tt>true</tt>, iff this type represents a boolean.
	 */
	public boolean isBoolean() {return this.domain == Domains.Boolean;}
	/** Similar to {@link #isBoolean()}. */
	public boolean isInteger() {return this.domain == Domains.Integer;}
	/** Similar to  {@link #isBoolean()}. */
	public boolean isReal() {return this.domain == Domains.Real;}	
	/** Similar to  {@link #isBoolean()}. */
	public boolean isPointer() {return this.domain == Domains.Pointer;}
	/** Similar to  {@link #isBoolean()}. */
	public boolean isProcedure() {return this.domain == Domains.Procedure;}
	/** Similar to  {@link #isBoolean()}. */
	public boolean isUndefined() {return this.domain == Domains.Undefined;}
	/** Similar to  {@link #isBoolean()}. */
	public boolean isError() {return this.domain == Domains.ERROR_TYPE;}
	
	/**
	 * Compare two types for equality; For a variant returning a truth value
	 * see {@link #bequals(Type, Type)}.
	 * @param t1 The first type to compare.
	 * @param t2 The second type to compare.
	 * @return t1, iff both types are equal, {@link Domains#ERROR_TYPE} otherwise.
	 */
	public static Type equals(Type t1, Type t2) {
		if (t1 == null || t2 == null)
			if (t1 != t2)
				return ERROR_TYPE;
			else return null;
		if (t1.domain == t2.domain) {
			if (!bequals(t1.rtype, t2.rtype))
				return ERROR_TYPE;
			if (t1.paras.length != t2.paras.length)
				return ERROR_TYPE;
			for (int i = 0; i < t1.paras.length; i++)
				if (!bequals(t1.paras[i], t2.paras[i]))
					return ERROR_TYPE;
			return t1;
		}
		return ERROR_TYPE;
	}
	
	/**
	 * For details see {@link #equals(Type, Type)}.
	 * @return <tt>true</tt>, iff <tt>t1</tt> and <tt>t2</tt> represent the
	 * same type.
	 */
	public static boolean bequals(Type t1, Type t2) {
		return equals(t1, t2) != ERROR_TYPE;
	}
	
	/**
	 * Pretty print the represented type as written in SiPLE.
	 * @return SiPLE code for this type.
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(domain);
		switch (domain) {
		case Procedure:
			result.append(" (");
			for (Type para:paras)
				result.append(para.toString());
			result.append(")");
			if (rtype != null) {
				result.append(":");
				result.append(rtype.toString());
			}
			break;
		case Pointer:
			result.append("(");
			result.append(rtype.toString());
			result.append(")");
		}
		return result.toString();
	}
}
