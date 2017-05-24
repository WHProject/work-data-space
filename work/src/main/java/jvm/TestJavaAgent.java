package jvm;

import java.lang.reflect.Field;
import java.util.Vector;

public class TestJavaAgent {

	static {
		printClassList("static method");
	}

	public static void main(String[] args) throws Exception {
		printClassList("before import");
		TestClass p = new TestClass();
		printClassList("after import");
		System.in.read();
	}

	@SuppressWarnings("rawtypes")
	public static void printClassList(String tag) {
		try {
			Field f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);
			Vector classes = (Vector) f.get(ClassLoader.getSystemClassLoader());
			System.out.println(tag + ":" + classes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
