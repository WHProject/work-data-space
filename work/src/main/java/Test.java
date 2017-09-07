import java.util.Map;
import java.util.TreeMap;

public class Test {
	public static void main(String[] args) {
		Map<NoComparable,String> treeMap=new TreeMap<NoComparable,String>();
		treeMap.put(new NoComparable(), "NoComparable");
		System.out.println(treeMap);
	}
	
	public static class NoComparable{
	}
}
