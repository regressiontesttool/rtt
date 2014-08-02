package rtt.core.testing.compare;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Value;
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
		
		public static CompareResult create(DifferenceType type, Element expected, Element actual) {
			String message = "";
			switch (type) {
			case CLASSES:
				message = type.getDescription(expected.getClass(), actual.getClass());
				break;
			case GEN_NAME:
				message = type.getDescription(expected.getName(), actual.getName());
				break;
			case GEN_TYPE:
				message = type.getDescription(expected.getType(), actual.getType());
				break;
			case ISINFORMATIONAL:
				message = type.getDescription(expected.isInformational(), actual.isInformational());
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
	
	private static final String VALUE_UNEQUAL =	
			"The node values are different.";
	private static final String NODE_NULL = 
			"One or both given nodes were null.";	
	private static final String SIZE_UNEQUAL = 
			"The amount of reference and actual nodes are not equal.";
	private static final String SIMPLENAME_UNEQUAL =
			"The simple name attributes are different.";
	private static final String FULLNAME_UNEQUAL = 
			"The full name attributes are different.";
	private static final String CHILDREN_UNEQUAL =
			"The sizes of children nodes are different.";
	
	private boolean testInformational;
	
	private OutputCompare(boolean testInformational) {
		this.testInformational = testInformational;
	}

	public static List<TestFailure> compareOutput(
			Output referenceOutput, Output actualOutput, boolean testInformational) {
		
		if (referenceOutput == null || actualOutput == null) {
			throw new IllegalArgumentException("Reference or actual output was null.");
		}
		
		OutputCompare comparer = new OutputCompare(testInformational);
		CompareResult result = comparer.compareValue(
				referenceOutput.getAST(), actualOutput.getAST());
		
		List<TestFailure> failures = new ArrayList<>();
		if (result != null && result.hasDifferences()) {			
			failures.add(new TestFailure(result.getDifference()));
		}
		
		return failures;
	}
	
	private CompareResult compareValue(Element referenceElement, Element actualElement) {
		
		if (referenceElement == null || actualElement == null) {
			throw new IllegalStateException(NODE_NULL);
		}
		
		if (referenceElement.isInformational() != actualElement.isInformational()) {
			return CompareResult.create(DifferenceType.ISINFORMATIONAL, referenceElement, actualElement);
		}
		
		if (!referenceElement.isInformational() || testInformational) {
			
			if (referenceElement.getClass() != actualElement.getClass()) {
				return CompareResult.create(DifferenceType.CLASSES, referenceElement, actualElement);
			}
			
			if (!referenceElement.getName().equals(actualElement.getName())) {
				return CompareResult.create(DifferenceType.GEN_NAME, referenceElement, actualElement);
			}
			
			if (!referenceElement.getType().equals(actualElement.getType())) {
				return CompareResult.create(DifferenceType.GEN_TYPE, referenceElement, actualElement);
			}
			
			if (referenceElement instanceof Value) {
				String refValue = String.valueOf(((Value) referenceElement).getValue());
				String actualValue = String.valueOf(((Value) actualElement).getValue());			
				
				if (!refValue.equals(actualValue)) {
					return CompareResult.create(VALUE_UNEQUAL);
				}
			}
			
			if (referenceElement instanceof Node) {
				Node refNode = (Node) referenceElement;
				Node actualNode = (Node) actualElement;
				
				return compareNode(refNode, actualNode);
			}
		}
		
		return null;
	}

	private CompareResult compareNode(Node referenceNode, Node actualNode) {
		if (!referenceNode.getSimpleName().equals(actualNode.getSimpleName())) {
			return CompareResult.create(SIMPLENAME_UNEQUAL);
		}
		
		if (!referenceNode.getFullName().equals(actualNode.getFullName())) {
			return CompareResult.create(FULLNAME_UNEQUAL);
		}
		
		return compareChildren(referenceNode.getElement(),
				actualNode.getElement());
	}
	
	private CompareResult compareChildren(
			List<Element> refValues, List<Element> actualValues) {
		
		if (refValues.size() != actualValues.size()) {
			return CompareResult.create(CHILDREN_UNEQUAL);
		}
		
		int childCount = refValues.size();
		for (int index = 0; index < childCount; index++) {
			CompareResult result = compareValue(
					refValues.get(index), actualValues.get(index));
			
			if (result != null && result.hasDifferences()) {
				return result;
			}			
		}
		
		return null;
	}
}
