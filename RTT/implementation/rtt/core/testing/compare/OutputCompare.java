package rtt.core.testing.compare;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Reference;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
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
		
		public enum Difference {
			CLASSES("Object type"),
			INFORMATIONAL("IsInformational"),
			NAME("Name"),
			TYPE("Type"),
			VALUE("Value"),
			REFERENCE("Reference"),
			SIMPLENAME("Simple name"),
			FULLNAME("Full name"),
			CHILD_COUNT("Sizes of children");			
			
			private String description;
			
			private Difference(String description) {
				this.description = description;
			}
		}
		
		private Difference difference = null;
		private Object expected = null;
		private Object actual = null;
		
		protected CompareResult(Difference difference) {
			this.difference = difference;
		}
		
		public Difference getDifference() {
			return difference;
		}

		public boolean hasDifferences() {
			return difference != null;
		}
		
		public String getMessage() {
			if (difference != null) {
				StringBuilder builder = new StringBuilder(difference.description);
				if (expected != null && actual != null) {
					builder.append(" expected '");
					builder.append(expected.toString());
					builder.append("', but was '");
					builder.append(actual.toString());
					builder.append("'.");
				}
				
				return builder.toString();
			}
			
			return "No differences found.";			
		}
		
		public static CompareResult create(Difference difference, Object expected, Object actual) {
			CompareResult result = new CompareResult(difference);
			result.difference = difference;
			result.expected = expected;
			result.actual = actual;
			
			return result;
		}		
	}
	
	private static final String ELEMENT_NULL = 
			"One or both given elements were null.";
	
	private boolean testInformational;
	
	public OutputCompare(boolean testInformational) {
		this.testInformational = testInformational;
	}

	public static List<TestFailure> compareOutput(
			Output referenceOutput, Output actualOutput, boolean testInformational) {
		
		if (referenceOutput == null || actualOutput == null) {
			throw new IllegalArgumentException("Reference or actual output was null.");
		}
		
		OutputCompare comparer = new OutputCompare(testInformational);
		CompareResult result = null; //comparer.compareElement(
				//referenceOutput.getAST(), actualOutput.getAST());
		
		List<TestFailure> failures = new ArrayList<>();
		if (result != null && result.hasDifferences()) {			
			failures.add(new TestFailure(result.getMessage()));
		}
		
		return failures;
	}
	
	public CompareResult compareElements(Element referenceElement, Element actualElement) {
		if (referenceElement == null || actualElement == null) {
			throw new IllegalArgumentException(ELEMENT_NULL);
		}
		
		if (referenceElement.getClass() != actualElement.getClass()) {
			return CompareResult.create(Difference.CLASSES, 
					referenceElement, actualElement);
		}
		
		CompareResult result = compareElementAttributes(referenceElement, actualElement);
		if (!hasDifferences(result) && testInformational(referenceElement)) {
			if (referenceElement instanceof Value) {
				result = compareValueAttributes(
						(Value) referenceElement,
						(Value) actualElement); 
			}
			
			if (referenceElement instanceof Reference) {
				result = compareReferenceAttributes(
						(Reference) referenceElement,
						(Reference) actualElement);
			}
			
			if (referenceElement instanceof Node) {
				result = compareNodes(
						(Node) referenceElement,
						(Node) actualElement);					
			}
		}
		
		return result;
	}
	
	private boolean hasDifferences(CompareResult result) {
		return result != null && result.hasDifferences();
	}
	
	private boolean testInformational(Element element) {
		return !element.isInformational() || testInformational;
	}
	
	private CompareResult compareElementAttributes(Element referenceElement, Element actualElement) {
		if (!referenceElement.getGeneratorName().equals(actualElement.getGeneratorName())) {
			return CompareResult.create(Difference.NAME, 
					referenceElement.getGeneratorName(), actualElement.getGeneratorName());
		}
		
		if (!referenceElement.getGeneratorType().equals(actualElement.getGeneratorType())) {
			return CompareResult.create(Difference.TYPE, 
					referenceElement.getGeneratorType().name(), 
					actualElement.getGeneratorType().name());
		}
		
		if (referenceElement.isInformational() != actualElement.isInformational()) {
			return CompareResult.create(Difference.INFORMATIONAL, 
					referenceElement.isInformational(), actualElement.isInformational());
		}
		
		return null;
	}
	
	private CompareResult compareValueAttributes(Value referenceValue, Value actualValue) {
		String valueOfRefer = String.valueOf(referenceValue.getValue());
		String valueOfActual = String.valueOf(actualValue.getValue());			
		
		if (!valueOfRefer.equals(valueOfActual)) {
			return CompareResult.create(Difference.VALUE, valueOfRefer, valueOfActual);
		}
		
		return null;
	}
	
	private CompareResult compareReferenceAttributes(Reference referenceValue, Reference actualValue) {
		String valueOfRefer = String.valueOf(referenceValue.getTo());
		String valueOfActual = String.valueOf(actualValue.getTo());			
		
		if (!valueOfRefer.equals(valueOfActual)) {
			return CompareResult.create(Difference.REFERENCE, valueOfRefer, valueOfActual);
		}
		
		return null;
	}
	
	private CompareResult compareNodes(Node referenceNode, Node actualNode) {
		
		CompareResult result = compareNodeAttributes(referenceNode, actualNode);
		if (!hasDifferences(result)) {
			List<Element> referenceCompareElements = referenceNode.getElement();
			List<Element> actualCompareElements = actualNode.getElement();
			
			if (testInformational == false) {
				referenceCompareElements = getCompareElements(referenceCompareElements);
				actualCompareElements = getCompareElements(actualCompareElements);
			}
			
			result = compareChildElements(referenceCompareElements, actualCompareElements);
		}
		
		return result;	
	}
	
	private CompareResult compareNodeAttributes(Node referenceNode, Node actualNode) {
		if (!referenceNode.getSimpleName().equals(actualNode.getSimpleName())) {
			return CompareResult.create(Difference.SIMPLENAME,
					referenceNode.getSimpleName(), actualNode.getSimpleName());
		}
		
		if (!referenceNode.getFullName().equals(actualNode.getFullName())) {
			return CompareResult.create(Difference.FULLNAME,
					referenceNode.getFullName(), actualNode.getFullName());
		}
		
		return null;
	}	

	private List<Element> getCompareElements(List<Element> refValues) {
		List<Element> compareElements = new ArrayList<>();
		for (Element element : refValues) {
			if (!element.isInformational()) {
				compareElements.add(element);
			}
		}
		
		return compareElements;
	}
	
	private CompareResult compareChildElements(
			List<Element> refElements, List<Element> actualElements) {				
		
		if (refElements.size() != actualElements.size()) {
			return CompareResult.create(Difference.CHILD_COUNT,
					refElements.size(), actualElements.size());
		}
		
		int childCount = refElements.size();
		for (int index = 0; index < childCount; index++) {
			Element referenceElement = refElements.get(index);
			Element actualElement = actualElements.get(index);		
			
			CompareResult result = compareElements(
					referenceElement, actualElement);
			
			if (hasDifferences(result)) {
				return result;
			}
		}
		
		return null;
	}	
}
