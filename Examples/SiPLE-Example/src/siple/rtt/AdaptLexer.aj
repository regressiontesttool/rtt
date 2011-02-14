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

import siple.symbols.*;

/**
 * AspectJ specification to adapt the lexer and its tokens for RTT.
 * @author C. Bürger
 */
public aspect AdaptLexer {
	// Lexer annotations:
	declare @type: SIPLELexer : @Lexer;
	
	// Specify method returning next token:
	declare @method:
		public * SIPLELexer.nextToken() : @Lexer.NextToken;
	
	// Token annotations:
	declare parents:
		beaver.Symbol implements RTTToken;
}
