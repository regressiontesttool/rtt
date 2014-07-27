package rtt.core.testing.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare.CompareResult.DifferenceType;
import rtt.core.testing.compare.results.TestFailure;

public class OutputCompare {
	
	protected static abstract class ExtendedComparator<T extends Node> {
		
		protected OutputCompare outputCompare;
		
		protected ExtendedComparator(OutputCompare outputCompare) {
			this.outputCompare = outputCompare;
		}

		@SuppressWarnings("unchecked")
		public CompareResult compareNodes(Node referenceNode, Node actualNode) {				
			return compare((T) referenceNode, (T) actualNode);
		}
		
		protected abstract CompareResult compare(T referenceNode, T actualNode);
	}
	
	public static class CompareResult {
		
		public enum DifferenceType {
			CLASSES("Node classes"),
			ISINFORMATIONAL("IsInformational"),
			ISNULL("IsNull"),
			GEN_NAME("Generator name"),
			GEN_TYPE("Generator type");
			
			private String description;
			
			private DifferenceType(String description) {
				this.description = description;
			}
			
			public String getDescription(Object expected, Object actual) {
				StringBuilder builder = new StringBuilder(description);
				builder.append(" expected = '");
				builder.append(expected.toString());
				builder.append("', but was '");
				builder.append(actual.toString());
				builder.append("'.");
				
				return builder.toString();
			}
		}		
		
		private String difference;
		
		public void setDifference(String difference) {
			this.difference = difference;
		}

		public String getDifference() {
			return difference;
		}
		
		public boolean hasDifferences() {
			return difference != null && !difference.equals("");
		}
		
		public static CompareResult create(DifferenceType type, Node expected, Node actual) {
			String message = "";
			switch (type) {
			case CLASSES:
				message = type.getDescription(expected.getClass(), actual.getClass());
				break;
			case GEN_NAME:
				message = type.getDescription(expected.getGeneratorName(), actual.getGeneratorName());
				break;
			case GEN_TYPE:
				message = type.getDescription(expected.getGeneratorType().name(), actual.getGeneratorType().name());
				break;
			case ISINFORMATIONAL:
				message = type.getDescription(expected.isInformational(), actual.isInformational());
				break;
			case ISNULL:
				message = type.getDescription(expected.isIsNull(), actual.isIsNull());
				break;
			}

			return create(message);	
		}
		
		public static CompareResult create(String message) {
			CompareResult result = new CompareResult();
			result.difference = message;
			return result;
		}
	}
	
	
	private static final String NODE_NULL = "One or both given nodes were null.";	
	private static final String SIZE_UNEQUAL = "The amount of reference and actual nodes are not equal.";
	
	private Map<Class<? extends Node>, ExtendedComparator<?>> comparerMap;
	private boolean testInformational;
	
	public OutputCompare(boolean testInformational) {
		this.testInformational = testInformational;
		
		comparerMap = new HashMap<>();			
		comparerMap.put(ClassNode.class, new ClassNodeComparer(this));
		comparerMap.put(ValueNode.class, new ValueNodeComparer(this));
	}

	public static List<TestFailure> compareOutput(
			Output referenceOutput, Output actualOutput, boolean testInformational) {
		
		if (referenceOutput == null || actualOutput == null) {
			throw new IllegalArgumentException("Reference or actual output was null.");
		}
		
		OutputCompare compare = new OutputCompare(testInformational);
		
		List<TestFailure> failures = new ArrayList<>();
		
		failures.addAll(compare.compareNodeLists(
				referenceOutput.getNodes(), actualOutput.getNodes()));
		
		return failures;
	}
	
	private List<TestFailure> compareNodeLists(List<Node> referenceNodes, 
			List<Node> actualNodes) {
		
		List<TestFailure> failures = new ArrayList<>();
		
		if (referenceNodes.size() != actualNodes.size()) {
			failures.add(new TestFailure(SIZE_UNEQUAL));
			return failures;
		}
		
		for(int index = 0; index < referenceNodes.size(); index++) {
			Node referenceNode = referenceNodes.get(index);
			Node actualNode = actualNodes.get(index);
			
			CompareResult result = compareNodes(referenceNode, actualNode);
			if (result != null && result.hasDifferences()) {
				failures.add(new TestFailure(result.getDifference()));
			}
		}
		
		return failures;
	}
	
	public CompareResult compareNodes(Node referenceNode, Node actualNode) {
		
		if (referenceNode == null || actualNode == null) {
			throw new IllegalStateException(NODE_NULL);
		}
		
		if (referenceNode.isInformational() != actualNode.isInformational()) {
			return CompareResult.create(DifferenceType.ISINFORMATIONAL, referenceNode, actualNode);
		}
		
		if (!referenceNode.isInformational() || testInformational) {
			
			if (referenceNode.getClass() != actualNode.getClass()) {
				return CompareResult.create(DifferenceType.CLASSES, referenceNode, actualNode);
			}
			
			if (!referenceNode.getGeneratorType().equals(actualNode.getGeneratorType())) {
				return CompareResult.create(DifferenceType.GEN_TYPE, referenceNode, actualNode);
			}

			if (!referenceNode.getGeneratorName().equals(actualNode.getGeneratorName())) {
				return CompareResult.create(DifferenceType.GEN_NAME, referenceNode, actualNode);
			}			

			if (referenceNode.isIsNull() != actualNode.isIsNull()) {
				return CompareResult.create(DifferenceType.ISNULL, referenceNode, actualNode);
			}
			
			ExtendedComparator<?> comparer = getComparer(referenceNode.getClass());
			if (comparer != null) {
				return comparer.compareNodes(referenceNode, actualNode);
			}			
		}
		
		return null;
	}

	private ExtendedComparator<? extends Node> getComparer(Class<?> nodeClass) {
		if (comparerMap.containsKey(nodeClass)) {
			return comparerMap.get(nodeClass);
		}
		
		return null;
	}
}
