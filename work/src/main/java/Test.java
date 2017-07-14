import java.math.BigDecimal;

public class Test {
	public static void main(String[] args) {
		String message = "295999757@qq.com";
		Byte s = null;
		BigDecimal rateValue = new BigDecimal("-0.0000001");
		System.out.println((new BigDecimal(0)).compareTo(rateValue) == 1 || (new BigDecimal(49)).compareTo(rateValue) == -1);
	}
}
