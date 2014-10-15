/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package siple;

import java.io.*;

import siple.ast.*;

/**
 * Class for command line {@link #main(String[]) execution} of
 * <i>SiPLE</i> programs.
 * @author C. Bürger
 *
 */
public class Interpreter {
	/**
	 * {@link #Interpreter() Interpret} the source file <tt>args[0]</tt> and
	 * print the result on the standard output.
	 * @param args The <i>SiPLE</i> program to interpret.
	 * @throws IOException Thrown, iff the program file cannot be read.
	 * @throws beaver.Parser.Exception Thrown, iff the program contains
	 * syntax errors.
	 */
	public static void main(String[] args)
	throws IOException, beaver.Parser.Exception {
		final long start = new java.util.Date().getTime();
		try {
			State vm = interpret(new File(args[0]));
			System.out.println(vm.stdOut);
		} catch (InterpretationException rte) {
			System.out.println(rte);
		}
		final long end = new java.util.Date().getTime();
		System.out.println("\nExecution Time: "+ (end - start) +"ms");
	}
	
	/**
	 * Interpret the given program, i.e. do a lexic, syntactic and semantic
	 * analysis and finally call the root node's
	 * {@link siple.semantics.ast.ASTNode.Interpret()} method. Iff the
	 * interpretation has been successful return its {@link State final state}.
	 * Otherwise abort with an {@link InterpretationException interpretation
	 * exception}.
	 * @param args The <i>SiPLE</i> program to interpret.
	 * @throws InterpretationException Thrown, iff the given program is
	 * erroneous.
	 * @throws IOException Thrown, iff the program file cannot be read.
	 * @throws beaver.Parser.Exception Thrown, iff the program contains
	 * syntax errors.
	 */
	public static State interpret(File program)
	throws InterpretationException, IOException, beaver.Parser.Exception {
		Lexer scanner = new Lexer(new FileReader(program));
		Parser parser = new Parser();
		CompilationUnit ast = (CompilationUnit)parser.parse(scanner);
		return ast.Interpret();
	}
}
