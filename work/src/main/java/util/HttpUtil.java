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
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

	public static void main(String[] args) throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		/*
		 * headers.put("Content-Type", "application/x-www-form-urlencoded");
		 * headers.put("Content-Length", "223"); headers.put("Host",
		 * "app-test.zhongniang.com"); headers.put("Connection", "Keep-Alive");
		 * headers.put("Accept-Encoding", "gzip"); headers.put("User-Agent",
		 * "okhttp/3.9.0");
		 */

		Map<String, String> params = new HashMap<String, String>();
		params.put("producer_ model", "vivo X5Max+");
		params.put("pwd", "2a6cb500f435ddc90e8acc09db52b182");
		params.put("uname", "44952b1c11092ab8716dd1b8ee93a112");
		params.put("producer", "vivo");
		params.put("android_system_version", "4.4.4");
		params.put("version", "3.2.0");
		params.put("sign", "f2bf06244850a64375cd5b0fb2ba4330");
		String content = HttpUtil.post("http://127.0.0.1:8090/ZNTG-APP/login_v3.htm", params, headers);
		System.out.println(content);
	}
}