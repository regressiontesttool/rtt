package org.dresdenocl.ocl2parser.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.dresdenocl.ocl2parser.test.constrainttypes.AllConstraintTypeTests;
import org.dresdenocl.pivotmodel.Constraint;
import org.dresdenocl.testsuite._abstract.AbstractDresdenOclTest;

import rtt.annotations.Parser;
import rtt.annotations.Parser.Node;

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