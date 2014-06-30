package testing;

import rtt.annotations.Parser.Node;

@Node
public class NotAnnotatedClass {
	
	public String getGreeting() {
		return "Hello World";
	}
	
	public void sayHello() {
		System.out.println(getGreeting());
	}

}
