package concurrent.testing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.HttpUtil;

public class Testing {
	public static void main(String[] args) throws Exception {
		testRecieveCoupon();
	}

	public static void testRecieveCoupon() {
		String url = "http://www.zhongniang.com/coupon/receiveCoupon.htm?actId=3458&couponId=21217";
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie",
				"UM_distinctid=15ac5c7c77a6c4-0d75d6003bbc21-314b7b5f-1fa400-15ac5c7c77b61c; znuser=; td_cookie=18446744072006666064; CNZZDATA1253460316=144156186-1472031159-http%253A%252F%252Fwww.zhongniang.com%252F%7C1493030202; PTOKEN_ZNTG=D09D1A66BB7AD4278714C5ABBADD41A0; user_province=32; OZ_1U_2159=vid=v7bd734ee11d77.0&ctime=1495607855&ltime=1495606265; OZ_1Y_2159=erefer=-&eurl=http%3A//www.zhongniang.com/login.htm&etime=1495508077&ctime=1495607855&ltime=1495606265&compid=2159; nTalk_CACHE_DATA={uid:jt_1000_ISME9754_guestTEMPD2D7-26DD-8A,tid:1495525281701142}; NTKF_T2D_CLIENTID=guestTEMPD2D7-26DD-8AA2-9B11-829AB64AA6A1");
		concurrentHttpTest(100, url, params, headers);
	}

	public static void concurrentHttpTest(int scheduleCount, String url, Map<String, String> params,
			Map<String, String> headers) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < scheduleCount; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					String content;
					try {
						content = HttpUtil.post(url, params, headers);
						System.out.println(content);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		}
		executorService.shutdown();
	}
}
