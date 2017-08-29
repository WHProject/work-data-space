package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

public class HttpUtil {

	private static Logger LOGGER = Logger.getLogger(HttpUtil.class);

	private static final int CONNECT_TIMEOUT = 5000;

	private static final int READ_TIMEOUT = 5000;

	private static final String CHARSET = "UTF-8";

	private static enum HttpMethod {
		POST, DELETE, GET, PUT, HEAD;
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String invokeUrl(String url, Map params, Map<String, String> headers, int connectTimeout,
			int readTimeout, String encoding, HttpMethod method) {
		StringBuilder paramsStr = null;
		if (params != null) {
			paramsStr = new StringBuilder();
			Set<Map.Entry> entries = params.entrySet();
			for (Map.Entry entry : entries) {
				String value = (entry.getValue() != null) ? (String.valueOf(entry.getValue())) : "";
				paramsStr.append(entry.getKey() + "=" + value + "&");
			}
			if (method != HttpMethod.POST) {
				url += "?" + paramsStr.toString();
			}
		}

		URL uUrl = null;
		HttpURLConnection conn = null;
		BufferedWriter out = null;
		BufferedReader in = null;
		try {
			uUrl = new URL(url);
			conn = (HttpURLConnection) uUrl.openConnection();
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			conn.setRequestMethod(method.toString());
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);

			if (headers != null && headers.size() > 0) {
				Set<String> headerSet = headers.keySet();
				for (String key : headerSet) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}

			if (paramsStr != null && method == HttpMethod.POST) {
				out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), encoding));
				out.write(paramsStr.toString());
				out.flush();
			}

			StringBuilder result = new StringBuilder();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			if (in != null) {
				String line = "";
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			}
			return result.toString();
		} catch (Exception e) {
			LOGGER.error("Request failed,request URL:" + url + ",params:" + params, e);
			try {
				byte[] buf = new byte[100];
				InputStream es = conn.getErrorStream();
				if (es != null) {
					while (es.read(buf) > 0) {
						;
					}
					es.close();
				}
			} catch (Exception e1) {
				LOGGER.error("Retry request failed,request URL:" + url + ",params:" + params, e1);
			}
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				LOGGER.error("BufferedWriter close failed", e);
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				LOGGER.error("BufferedReader close failed", e);
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static String post(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout, charset, HttpMethod.POST);
	}

	@SuppressWarnings("rawtypes")
	public static String post(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout, charset, HttpMethod.POST);
	}

	@SuppressWarnings("rawtypes")
	public static String post(String url, Map params, Map<String, String> headers) {
		return invokeUrl(url, params, headers, CONNECT_TIMEOUT, READ_TIMEOUT, CHARSET, HttpMethod.POST);
	}

	@SuppressWarnings("rawtypes")
	public static String get(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout, charset, HttpMethod.GET);
	}

	@SuppressWarnings("rawtypes")
	public static String get(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout, charset, HttpMethod.GET);
	}

	@SuppressWarnings("rawtypes")
	public static String get(String url, Map params, Map<String, String> headers) {
		return invokeUrl(url, params, headers, CONNECT_TIMEOUT, READ_TIMEOUT, CHARSET, HttpMethod.GET);
	}

	@SuppressWarnings("rawtypes")
	public static String put(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout, charset, HttpMethod.PUT);
	}

	@SuppressWarnings("rawtypes")
	public static String put(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout, charset, HttpMethod.PUT);
	}

	@SuppressWarnings("rawtypes")
	public static String delete(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout, charset, HttpMethod.DELETE);
	}

	@SuppressWarnings("rawtypes")
	public static String delete(String url, Map params, Map<String, String> headers, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout, charset, HttpMethod.DELETE);
	}

	@SuppressWarnings("rawtypes")
	public static String head(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout, charset, HttpMethod.HEAD);
	}

	@SuppressWarnings("rawtypes")
	public static String head(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout, charset, HttpMethod.HEAD);
	}

	public static void main(String[] args) {
		String getAccessTokenUrl = "http://apijx.jxwmanage.com/OauthCredentials/oauth/client_id/8bb83014/client_secret/71710280d7/code/308e1360f4af23add16b6b1399017945/grant_type/code";
		String accessTokenData = get(getAccessTokenUrl, null, 5000, 5000, "UTF-8");
		System.out.println(accessTokenData);
		Map<String, Map<String, String>> accessToken = GsonUtil.getEntityFromJson(accessTokenData, new TypeToken<Map<String, Map<String, String>>>() {
		});
		Map<String, String> accessTokenHeader = new HashMap<String, String>();
		accessTokenHeader.put("access-token", accessToken.get("OauthInfo").get("access_token"));

		String getSellerClassifyUrl = "http://apijx.jxwmanage.com/SellerClassify/SellerClassify?siteid=jt_1000";
		String sellerClassify = get(getSellerClassifyUrl, null, accessTokenHeader, 5000, 5000, "UTF-8");
		System.out.println(sellerClassify);
		List<Map<String, String>> sellerClassifyList = GsonUtil.getEntityFromJson(sellerClassify,
				new TypeToken<List<Map<String, String>>>() {
				});
		
		String addSellerUrl = "http://apijx.jxwmanage.com/Sellers/Sellers?siteid=jt_1000";
		Map<String, String> seller = new HashMap<String, String>();
		seller.put("seller_id", "8888");
		seller.put("classify_id", sellerClassifyList.get(0).get("id"));
		seller.put("name", "创建商户测试");
		seller.put("administrator", "jt_8888");
		seller.put("password", "jt123456");
		seller.put("level", "1");
		//String addSellerResult = post(addSellerUrl, seller, accessTokenHeader, 5000, 5000, "UTF-8");
		//System.out.println(addSellerResult);
	}

}
