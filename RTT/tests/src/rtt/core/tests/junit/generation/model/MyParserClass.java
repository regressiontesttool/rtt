package rtt.core.tests.junit.generation.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.Parser;

@Parser
public class MyParserClass {
	
	@rtt.annotations.Node
	public static class MyNode {
		
		@Compare
		private List<MyNode> childrenNodes;
		
		@Informational
		private String name;
		
		public MyNode(int i) {
			this.name = "MyNode" + i;
			childrenNodes = new ArrayList<>();
			
			for (int j = 0; j < i; j++) {
				childrenNodes.add(new MyNode(i - 1));
			}
		}		
	}
	
	private List<MyNode> myNodes;

	@Parser.Initialize
	public MyParserClass(InputStream in) {
		myNodes = new ArrayList<>();
		
		int maxNodes = 5;
		for (int i = 0; i < maxNodes; i++) {
			myNodes.add(new MyNode(i - 1));
		}
	}
	
	@Parser.AST
	public List<?> getMyNodes() {
		return myNodes;
	}
}