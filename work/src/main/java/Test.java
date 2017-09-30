import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(1, 10);
        System.out.println(StatementStatusType.valueOf("FINANCE_REJECT")==StatementStatusType.FINANCE_REJECT);  
	}
}
