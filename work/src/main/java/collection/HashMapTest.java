package collection;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {

	public static void main(String[] args) {
		hashCodeTest();
	}
	
	public static void hashCodeTest(){
		Integer a = new Integer("2");
		Integer b = new Integer("2");
		System.out.println(a.hashCode()==b.hashCode());
		
		String c = new String("2");
		String d = new String("2");
		System.out.println(c.hashCode()==d.hashCode());
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(a, "test");
		System.out.println(map.get(b));
	}

}
