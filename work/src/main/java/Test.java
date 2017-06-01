import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) throws Exception {
		Map<String,String> m=new HashMap<String,String>();
		m.put("ddd", null);
		m.put("ccc", null);
		System.out.println(m.values()==null);
	}
}
