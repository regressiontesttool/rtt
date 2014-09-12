package annotation;

public @interface MyAnnotation {
	
	String name() default "eins";
	boolean informational() default false;
	int index() default 100;

}
