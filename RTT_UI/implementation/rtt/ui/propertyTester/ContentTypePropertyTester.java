package rtt.ui.propertyTester;

import java.util.Arrays;

import org.eclipse.core.expressions.PropertyTester;

import rtt.ui.content.IContent;
import rtt.ui.content.configuration.ClasspathContent;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;

public class ContentTypePropertyTester extends PropertyTester {
	
	protected enum ParentProperty {
		CONFIGURATION(ConfigurationContent.class), CLASSPATH(
				ClasspathContent.class);

		private Class<? extends IContent> contentClass;

		private ParentProperty(Class<? extends IContent> contentClass) {
			this.contentClass = contentClass;
		}

		public Class<? extends IContent> getContentClass() {
			return contentClass;
		}
	}

	public ContentTypePropertyTester() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		System.out.println("Receiver: "  + receiver);
		System.out.println("Property: " + property);
		System.out.println("Args: " + Arrays.toString(args));
		System.out.println("ExpectedValue: " + expectedValue);
		System.out.println("-------------------------------");
		
		if (property.equals("hasType")) {
			return hasType(receiver, args, expectedValue);
		} else if (property.equals("hasParent")) {
			return hasParent(receiver, args, expectedValue);
		} else if (property.equals("hasChilds")) {
			return hasChildren(receiver, args, expectedValue);
		}
		
		return false;		
	}

	private boolean hasChildren(Object receiver, Object[] args, Object expectedValue) {
		if (receiver instanceof IContent) {
			IContent content = (IContent) receiver;
			return content.hasChildren();
		}
		
		return false;
	}

	private boolean hasParent(Object receiver, Object[] args,
			Object expectedValue) {

		if (receiver instanceof IContent) {
			IContent content = (IContent) receiver;

			ParentProperty parentProp = findParentProperty(args);
			if (parentProp != null && content.getParent() != null) {
				return content.getParent().getClass() == parentProp
						.getContentClass();
			}
		}

		return false;
	}

	private boolean hasType(Object receiver, Object[] args, Object expectedValue) {
		if (receiver instanceof SimpleTypedContent) {
			SimpleTypedContent content = (SimpleTypedContent) receiver;

			ContentType type = findContentType(args);
			if (type != null) {
				return content.getType() == type;
			}
		}

		return false;
	}

	public ParentProperty findParentProperty(Object[] args) {
		for (Object object : args) {
			if (object instanceof String) {
				String argument = (String) object;
				try {
					ParentProperty parent = ParentProperty.valueOf(argument
							.toUpperCase());
					return parent;
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		return null;
	}

	public ContentType findContentType(Object[] args) {
		for (Object object : args) {
			if (object instanceof String) {
				String argument = (String) object;
				try {
					ContentType type = ContentType.valueOf(argument
							.toUpperCase());
					return type;
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		return null;
	}

}
