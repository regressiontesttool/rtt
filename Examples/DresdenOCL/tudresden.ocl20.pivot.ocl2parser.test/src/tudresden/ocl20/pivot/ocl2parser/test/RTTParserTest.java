/*
Copyright (C) 2008-2009 by Claas Wilke (claaswilke@gmx.net)

This file is part of the OCL2 Parser Test Suite of Dresden OCL2 for Eclipse.

Dresden OCL2 for Eclipse is free software: you can redistribute it and/or modify 
it under the terms of the GNU Lesser General Public License as published by the 
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

Dresden OCL2 for Eclipse is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
for more details.

You should have received a copy of the GNU Lesser General Public License along 
with Dresden OCL2 for Eclipse. If not, see <http://www.gnu.org/licenses/>.
 */

package tudresden.ocl20.pivot.ocl2parser.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.dresdenocl.testsuite._abstract.AbstractDresdenOclTest;

import rtt.annotations.Parser;
import rtt.annotations.Parser.Node;
import tudresden.ocl20.pivot.ocl2parser.test.constrainttypes.AllConstraintTypeTests;
import tudresden.ocl20.pivot.pivotmodel.Constraint;

@Parser(withParams=true)
public class RTTParserTest extends AbstractDresdenOclTest {
	
	@Node
	public static class Ast {

		private List<Constraint> nodeList;

		public Ast(List<Constraint> parsedList) {
			this.nodeList = new ArrayList<Constraint>(parsedList);
		}
		
		@Node.Child
		public List<Constraint> getNodeList() {
			return nodeList;
		}
		
	}

	private Ast ast;
	
	@Parser.Initialize
	public RTTParserTest(InputStream input, String[] params) throws Exception {
		TestPerformer testPerformer;

		/* Try to get the TestPerformer. */
		testPerformer = TestPerformer.getInstance(
				AllConstraintTypeTests.META_MODEL_ID,
				AllConstraintTypeTests.MODEL_BUNDLE,
				AllConstraintTypeTests.MODEL_DIRECTORY);
		testPerformer.setModel(params[0]);

		/* Try to parse the constraint file. */
		this.ast = new Ast(testPerformer.parseString(convertStreamToString(input)));		
	}
	
	private String convertStreamToString(InputStream input) {
		Scanner scanner = new Scanner(input).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";				
	}

	@Parser.AST
	public Ast getAst() {
		return ast;
	}
}