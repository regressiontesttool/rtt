/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package siple.rtt;

import rtt.annotations.*;

import siple.semantics.ast.*;

/**
 * AspectJ specification to adapt the syntax and semantics for RTT.
 * @author C. Bürger
 */
public aspect AdaptAST {
	/** AST Structure */
	
	declare @type: ASTNode+ : @Parser.Node;
	
	// Support meaningful toString() results for AST nodes:
	String around(ASTNode node):
	execution(String ASTNode+.toString()) && target(node) {
		String type = node.getClass().toString();
		type = type.substring(type.lastIndexOf('.') + 1);
		return "(Node-Type: " + type + " | Node-Address: " + node.NodeAddress() + ")";
	}
	public String ASTNode.toString() {return null;}
	
	// Compare nodes' type and address:
	declare @method:
		public * ASTNode+.toString() : @Parser.Node.Compare;
	
	// Compare nodes' children:
	declare @method:
		public * ASTNode.getChildren() : @Parser.Node.Child;
	@SuppressWarnings("unchecked")
	public java.util.List<ASTNode> ASTNode.getChildren() {
		java.util.List<ASTNode> result = new java.util.LinkedList<ASTNode>();
		for (int i=0; i < getNumChild(); i++)
			result.add(getChild(i));
		return result;
	}
	
	// Compare nodes' terminals:
	declare @method:
		public * VariableAssignment+.getLValue() : @Parser.Node.Compare;
	declare @method:
		public * Declaration+.getName() : @Parser.Node.Compare;
	declare @method:
		public * ProcedureDeclaration+.getReturnType() : @Parser.Node.Compare;
	declare @method:
		public * Reference+.getName() : @Parser.Node.Compare;
	declare @method:
		public * ProcedureCall+.getName() : @Parser.Node.Compare;
	
	/** Name Analysis */
	
	// Compare procedure calls' associated procedure:
	declare @method:
		public * ProcedureCall+.Declaration() : @Parser.Node.Compare;
	// Compare the program's main procedure:
	declare @method:
		public * CompilationUnit+.MainProcedure() : @Parser.Node.Compare;
	// Compare references' associated declaration:
	declare @method:
		public * Reference+.Declaration() : @Parser.Node.Compare;
	// Compare the declaration associated with assignments' left hand side:
	declare @method:
		public * VariableAssignment+.Declaration() : @Parser.Node.Compare;
	
	/** Type Analysis */
	
	// Compare variable declarations' type:
	declare @method:
		public * VariableDeclaration+.Type() : @Parser.Node.Compare;
	// Compare assignments' type:
	declare @method:
		public * VariableAssignment+.Type() : @Parser.Node.Compare;
	// Compare procedure returns' type:
	declare @method:
		public * ProcedureReturn+.Type() : @Parser.Node.Compare;
	// Compare expressions' type:
	declare @method:
		public * Expression+.Type() : @Parser.Node.Compare;
}
