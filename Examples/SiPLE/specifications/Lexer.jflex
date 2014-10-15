/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */

/**
 * @author C. Bürger
 */
package siple;

import beaver.Symbol;
import beaver.Scanner;

import siple.Parser.Terminals;

%%

%class Lexer
%extends Scanner
%public
%function nextToken
%type Symbol
%yylexthrow Scanner.Exception
%eofval{
	return new Symbol(Terminals.EOF, "end-of-file");
%eofval}
%line
%column

LineTerminator = \r|\n|\r\n
Comment        = "%" [^\r\n]*
WhiteSpace     = {LineTerminator} | [ \t\f] | {Comment}
Identifier	   = [a-zA-Z] [a-zA-Z0-9]*
RealNumber     = [0-9]+[.][0-9]+
IntegerNumber  = [0-9]+

%{
	private Symbol createPrimitiveToken(short id) {
		return new Symbol(id, yyline, yycolumn, yylength(), yytext());
	}
%}

%%

<YYINITIAL> {
	"Procedure"		{ return createPrimitiveToken(Terminals.kPROCEDURE); }
	"Var"			{ return createPrimitiveToken(Terminals.kVAR); }
	"Begin"			{ return createPrimitiveToken(Terminals.kBEGIN); }
	"End"			{ return createPrimitiveToken(Terminals.kEND); }
	"If"			{ return createPrimitiveToken(Terminals.kIF); }
	"Then"			{ return createPrimitiveToken(Terminals.kTHEN); }
	"Else"			{ return createPrimitiveToken(Terminals.kELSE); }
	"Fi"			{ return createPrimitiveToken(Terminals.kFI); }
	"While"			{ return createPrimitiveToken(Terminals.kWHILE); }
	"Do"			{ return createPrimitiveToken(Terminals.kDO); }
	"Od"			{ return createPrimitiveToken(Terminals.kOD); }
	"Call"			{ return createPrimitiveToken(Terminals.kCALL); }
	"Return"		{ return createPrimitiveToken(Terminals.kRETURN); }
	"Write"			{ return createPrimitiveToken(Terminals.kWRITE); }
	"Read"			{ return createPrimitiveToken(Terminals.kREAD); }
	"Boolean"		{ return createPrimitiveToken(Terminals.kBOOLEAN); }
	"Integer"		{ return createPrimitiveToken(Terminals.kINTEGER); }
	"Real"			{ return createPrimitiveToken(Terminals.kREAL); }
	"Pointer"		{ return createPrimitiveToken(Terminals.kPOINTER); }
	"Address"		{ return createPrimitiveToken(Terminals.kADDRESS); }
	"Deref"			{ return createPrimitiveToken(Terminals.kDEREF); }
	"Not"			{ return createPrimitiveToken(Terminals.kNOT); }
	"And"			{ return createPrimitiveToken(Terminals.kAND); }
	"Or"			{ return createPrimitiveToken(Terminals.kOR); }
	":="			{ return createPrimitiveToken(Terminals.pCOLON_EQUALS); }
	"+"				{ return createPrimitiveToken(Terminals.pPLUS); }
	"-"				{ return createPrimitiveToken(Terminals.pMINUS); }
	"*"				{ return createPrimitiveToken(Terminals.pSTAR); }
	"/"				{ return createPrimitiveToken(Terminals.pSLASH); }
	"<="			{ return createPrimitiveToken(Terminals.pANGLEBRACKETLEFT_EQUALS); }
	">="			{ return createPrimitiveToken(Terminals.pANGLEBRACKETRIGHT_EQUALS); }
	"<"				{ return createPrimitiveToken(Terminals.pANGLEBRACKETLEFT); }
	">"				{ return createPrimitiveToken(Terminals.pANGLEBRACKETRIGHT); }
	"="				{ return createPrimitiveToken(Terminals.pEQUALS); }
	"#"				{ return createPrimitiveToken(Terminals.pSHARP); }
	"("				{ return createPrimitiveToken(Terminals.pBRACKETOPENROUND); }
	")"				{ return createPrimitiveToken(Terminals.pBRACKETCLOSEROUND); }
	":"				{ return createPrimitiveToken(Terminals.pCOLON); }
	";"				{ return createPrimitiveToken(Terminals.pSEMICOLON); }
	","				{ return createPrimitiveToken(Terminals.pCOMMA); }
	"true"			{ return createPrimitiveToken(Terminals.CONSTANT); }
	"false"			{ return createPrimitiveToken(Terminals.CONSTANT); }
	{RealNumber}	{ return createPrimitiveToken(Terminals.CONSTANT); }
	{IntegerNumber}	{ return createPrimitiveToken(Terminals.CONSTANT); }
	{Identifier}	{ return createPrimitiveToken(Terminals.IDENTIFIER); }
	{WhiteSpace}	{ /* ignore */ }
}
.					{ throw new Scanner.Exception("Unknown symbol."); }
