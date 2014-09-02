package rtt.core.testing.generation;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Reference;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;

public class GeneratorUtil {	
	
	private static <E extends Element> E copyElement(final Element source, 
			final E destination) {
		
		destination.setAddress(source.getAddress());
		destination.setName(source.getName());
		destination.setElementType(source.getElementType());
		destination.setInformational(source.isInformational());
		
		return destination;
	}
	
	public static Node createNode(final Object object, Element prototype) {
		Class<?> objectType = object.getClass();
		
		Node node = copyElement(prototype, new Node());		
		node.setObjectType(objectType.getName());
		
		return node;
	}
	
	public static Value createValue(Object value, Element prototype) {
		Value valueElement = copyElement(prototype, new Value());
		
		if (value != null) {
			valueElement.setValue(value.toString());
		}
		
		return valueElement;
	}
	
	public static Reference createReference(String address, Element prototype) {
		if (address == null) {
			throw new IllegalArgumentException("The given address was null.");
		}
		
		Reference reference = copyElement(prototype, new Reference());		
		reference.setTo(address);
		
		return reference;
	}
	
	public static Element createChildElement(final Element prototype, final int index) {
		int childAddress = index + 1;
		
		Element itemPrototype = new Element();
		itemPrototype.setAddress(prototype.getAddress() + "." + childAddress);
		itemPrototype.setName(prototype.getName() + "[" + index + "]");
		itemPrototype.setElementType(Type.OBJECT);	
		itemPrototype.setInformational(prototype.isInformational());	
		
		return itemPrototype;
	}
}
