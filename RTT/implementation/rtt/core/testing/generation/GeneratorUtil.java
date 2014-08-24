package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import rtt.annotations.Node.Address;
import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Reference;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.utils.RTTLogging;

public class GeneratorUtil {	

	private static final Class<? extends Annotation> ADDRESS_ANNOTATION = Address.class;
	
	private static Field getAddressFieldFromObject(Class<?> objectType) {
		for (Field field : objectType.getDeclaredFields()) {
			if (field.isAnnotationPresent(ADDRESS_ANNOTATION) 
					&& field.getType() == String.class) {
				
				return field;
			}
		}
		
		return null;
	}
	
	public static String getObjectAddress(Object object) {
		Field addressField = getAddressFieldFromObject(object.getClass());		
		if (addressField != null) {
			try {
				addressField.setAccessible(true);
				return (String) addressField.get(object);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				RTTLogging.error("Can not access address field", e);
			}
		}
		
		return null;
	}
	
	public static void setObjectAddress(Object object, String address) {
		Field addressField = getAddressFieldFromObject(object.getClass());
		if (addressField != null) {
			try {
				addressField.setAccessible(true);
				addressField.set(object, address);
			} catch (Exception e) {
				RTTLogging.error("Could not access address field", e);
			}
		}
	}
	
	// --------------------------------------------------------------------------
	
	private static <E extends Element> E copyElement(final Element source, final E destination) {
		destination.setAddress(source.getAddress());
		destination.setGeneratorName(source.getGeneratorName());
		destination.setGeneratorType(source.getGeneratorType());
		destination.setInformational(source.isInformational());
		
		return destination;
	}
	
	public static Node createNode(final Object object, Element prototype) {
		Class<?> objectType = object.getClass();
		
		Node node = copyElement(prototype, new Node());		
		node.setFullName(objectType.getName());
		node.setSimpleName(objectType.getSimpleName());
		
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
		itemPrototype.setGeneratorName(prototype.getGeneratorName() + "[" + index + "]");
		itemPrototype.setGeneratorType(Type.OBJECT);	
		itemPrototype.setInformational(prototype.isInformational());	
		
		return itemPrototype;
	}
}
