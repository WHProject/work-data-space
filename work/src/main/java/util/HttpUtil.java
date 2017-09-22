package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.reflect.TypeToken;

/**
 * http、https 请求工具类， 微信为https的请求
 * 
 * @author yehx
 *
 */
public class HttpUtil {

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final String _GET = "GET"; // GET
	private static final String _POST = "POST";// POST
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;

	/**
	 * 初始化http请求参数
	 * 
	 * @param url
	 * @param method
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	private static HttpURLConnection initHttp(String url, String method, Map<String, String> headers) throws Exception {
		URL _url = new URL(url);
		HttpURLConnection http = (HttpURLConnection) _url.openConnection();
		// 连接超时
		http.setConnectTimeout(DEF_CONN_TIMEOUT);
		// 读取超时 --服务器响应比较慢，增大时间
		http.setReadTimeout(DEF_READ_TIMEOUT);
		http.setUseCaches(false);
		http.setRequestMethod(method);
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		if (null != headers && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				http.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		http.setDoOutput(true);
		http.setDoInput(true);
		http.connect();
		return http;
	}

	/**
	 * 初始化http请求参数
	 * 
	 * @param url
	 * @param method
	 * @return
	 * @throws Exception
	 */
	private static HttpsURLConnection initHttps(String url, String method, Map<String, String> headers)
			throws Exception {
		TrustManager[] tm = { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		System.setProperty("https.protocols", "TLSv1");
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		URL _url = new URL(url);
		HttpsURLConnection http = (HttpsURLConnection) _url.openConnection();
		// 设置域名校验
		http.setHostnameVerifier(new HttpUtil().new TrustAnyHostnameVerifier());
		// 连接超时
		http.setConnectTimeout(DEF_CONN_TIMEOUT);
		// 读取超时 --服务器响应比较慢，增大时间
		http.setReadTimeout(DEF_READ_TIMEOUT);
		http.setUseCaches(false);
		http.setRequestMethod(method);
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		if (null != headers && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				http.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		http.setSSLSocketFactory(ssf);
		http.setDoOutput(true);
		http.setDoInput(true);
		http.connect();
		return http;
	}

	/**
	 * 
	 * @description 功能描述: get 请求
	 * @return 返回类型:
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		HttpURLConnection http = null;
		if (isHttps(url)) {
			http = initHttps(initParams(url, params), _GET, headers);
		} else {
			http = initHttp(initParams(url, params), _GET, headers);
		}
		InputStream in = http.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
		String valueString = null;
		StringBuffer bufferRes = new StringBuffer();
		while ((valueString = read.readLine()) != null) {
			bufferRes.append(valueString);
		}
		in.close();
		if (http != null) {
			http.disconnect();// 关闭连接
		}
		return bufferRes.toString();
	}

	public static String get(String url) throws Exception {
		return get(url, null);
	}

	public static String get(String url, Map<String, String> params) throws Exception {
		return get(url, params, null);
	}

	public static String post(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		HttpURLConnection http = null;
		if (isHttps(url)) {
			http = initHttps(url, _POST, headers);
		} else {
			http = initHttp(url, _POST, headers);
		}
		OutputStream out = http.getOutputStream();
		String paramsStr = map2Url(params);
		if (paramsStr != null) {
			out.write(paramsStr.getBytes(DEFAULT_CHARSET));
		}
		out.flush();
		out.close();

		InputStream in = http.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
		String valueString = null;
		StringBuffer bufferRes = new StringBuffer();
		while ((valueString = read.readLine()) != null) {
			bufferRes.append(valueString);
		}
		in.close();
		if (http != null) {
			http.disconnect();// 关闭连接
		}
		return bufferRes.toString();
	}

	/**
	 * 功能描述: 构造请求参数
	 * 
	 * @return 返回类型:
	 * @throws Exception
	 */
	public static String initParams(String url, Map<String, String> params) throws Exception {
		if (null == params || params.isEmpty()) {
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		if (url.indexOf("?") == -1) {
			sb.append("?");
		}
		sb.append(map2Url(params));
		return sb.toString();
	}

	/**
	 * map构造url
	 * 
	 * @return 返回类型:
	 * @throws Exception
	 */
	public static String map2Url(Map<String, String> paramToMap) throws Exception {
		if (null == paramToMap || paramToMap.isEmpty()) {
			return null;
		}
		StringBuffer url = new StringBuffer();
		boolean isfist = true;
		for (Entry<String, String> entry : paramToMap.entrySet()) {
			if (isfist) {
				isfist = false;
			} else {
				url.append("&");
			}
			url.append(entry.getKey()).append("=");
			String value = entry.getValue();
			if (!StringUtil.isEmpty(value)) {
				url.append(URLEncoder.encode(value, DEFAULT_CHARSET));
			}
		}
		return url.toString();
	}

	/**
	 * 检测是否https
	 * 
	 * @param url
	 */
	private static boolean isHttps(String url) {
		return url.startsWith("https");
	}

	/**
	 * https 域名校验
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;// 直接返回true
		}
	}
	
	private static final String SITE_ID = "jt_1000";
	private static final String SELLER_PASSWORD = "jt123456";
	private static final String SELLER_LEVEL = "1";
	private static final String GET_ACCESSTOKEN_URL = "http://58.83.215.29/OauthCredentials/oauth/client_id/8bb83014/client_secret/71710280d7/code/308e1360f4af23add16b6b1399017945/grant_type/code";
	private static final String GET_SELLERCLASSIFY_URL = "http://58.83.215.29/SellerClassify/SellerClassify?siteid="
			+ SITE_ID;
	private static final String ADD_SELLER_URL = "http://58.83.215.29/Sellers/Sellers?siteid="+ SITE_ID;

	private static boolean synchronizeXiaoneng() {
		try {

			String accessTokenData = get(GET_ACCESSTOKEN_URL);

			Map<String, Map<String, String>> accessToken = GsonUtil.getEntityFromJson(accessTokenData, new TypeToken<Map<String, Map<String, String>>>() {
			});
			Map<String, String> accessTokenHeader = new HashMap<String, String>();
			accessTokenHeader.put("access-token", accessToken.get("OauthInfo").get("access_token"));

			Map<String, String> params = new HashMap<String, String>();
			
			String sellerClassifyData = get(GET_SELLERCLASSIFY_URL,params, accessTokenHeader);
			List<Map<String, String>> sellerClassifyList = GsonUtil.getEntityFromJson(sellerClassifyData,
					new TypeToken<List<Map<String, String>>>() {
					});

			Map<String, String> seller = new HashMap<String, String>();
			seller.put("seller_id", "jt_121");
			seller.put("classify_id", sellerClassifyList.get(0).get("id"));
			seller.put("name", "测试");
			seller.put("administrator", "admin");
			seller.put("password", SELLER_PASSWORD);
			seller.put("level", SELLER_LEVEL);
			String addSellerData = post(ADD_SELLER_URL, seller, accessTokenHeader);
			Map<String, String> addSeller = GsonUtil.getEntityFromJson(addSellerData,
					new TypeToken<Map<String, String>>() {
					});

			String settingId = addSeller.get("setting_id");
			System.out.println(settingId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		synchronizeXiaoneng();
	}
}