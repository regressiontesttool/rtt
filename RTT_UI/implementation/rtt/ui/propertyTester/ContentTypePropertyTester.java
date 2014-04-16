package rtt.ui.propertyTester;

import org.eclipse.core.expressions.PropertyTester;

import rtt.ui.content.IContent;
import rtt.ui.content.configuration.ClasspathContent;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.testsuite.TestcaseContent;

public class ContentTypePropertyTester extends PropertyTester {
	
	protected enum ParentProperty {
		CONFIGURATION(ConfigurationContent.class), 
		CLASSPATH(ClasspathContent.class),
		TESTCASE(TestcaseContent.class);

		private Class<? extends IContent> contentClass;

		private ParentProperty(Class<? extends IContent> contentClass) {
			this.contentClass = contentClass;
		}

		public Class<? extends IContent> getContentClass() {
			return contentClass;
		}
	}

	public ContentTypePropertyTester() { }

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
//		System.out.println("Receiver: "  + receiver);
//		System.out.println("Property: " + property);
//		System.out.println("Args: " + Arrays.toString(args));
//		System.out.println("ExpectedValue: " + expectedValue);
//		System.out.println("-------------------------------");
		
		if (property.equals("hasParent")) {
			return hasParent(receiver, args, expectedValue);
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

	private ParentProperty findParentProperty(Object[] args) {
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
}
