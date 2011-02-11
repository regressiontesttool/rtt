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

import siple.syntax.*;

/**
 * Support class woven by an aspect into Beaver's <tt>beaver.Symbol</tt> class
 * to adapt it for RTT.
 * @author C. Bürger
 */
@Lexer.Token
public abstract class RTTToken {
	public abstract short getId();
	public abstract int getStart();
	public abstract int getEnd();
	
	@Lexer.Token.EOF
	public boolean isEof() {
		return getId() == 0;
	}
	
	@Lexer.Token.Compare
	public String getType() {
		return SIPLEParser.Terminals.NAMES[getId()];
	}
	
	@Lexer.Token.Informational
	public int getStartLine() {
		return getStart();
	}
	
	@Lexer.Token.Informational
	public int getEndLine() {
		return getEnd();
	}
}
