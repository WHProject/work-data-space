package app.testing;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;

import util.FileUtil;
import util.GsonUtil;
import util.HttpUtil;
import util.LoginDesUtils;
import util.MD5Util;

public class AppTesting {

	public static void main(String[] args) throws Exception {
		String url = "/draw/exchangeIntegral.htm";

		Map<String, String> params = Maps.newHashMap();
		params.put("id", "405");

		testAPI(url, params, Method.POST);
	}
	
	private static Logger LOGGER = Logger.getLogger(AppTesting.class);
	private static final String HOST = "http://127.0.0.1:8090/ZNTG-APP";
	private static final String API_APP_SECURITYKEY = "345ccf1d2f23ad6b6a5bac3771564app";
	private static final String USER_LOGIN_RSA_KEY = "com.jiuxian.tuan";
	private static final LoginDesUtils DES_UTIL = new LoginDesUtils(USER_LOGIN_RSA_KEY);

	private static final String USERNAME = "yushaohua";
	private static final String PASSWORD = "ysh123456";

	private static SessionInfo sessionInfo = null;

	public static String testAPI(String url, Map<String, String> requestParams, Method method) throws Exception {
		String result = null;
		if (login(USERNAME, PASSWORD)) {
			Map<String, String> params = Maps.newHashMap();
			params.put("json", "");
			if (requestParams != null) {
				params.putAll(requestParams);
			}
			params.put("PTOKEN_ZNTG", sessionInfo.data.PTOKEN_ZNTG);
			params.put("sign", getSign(params));
			if (method == Method.POST) {
				result = HttpUtil.post(HOST + url, params, null);
			} else {
				result = HttpUtil.get(HOST + url, params, null);
			}
			LOGGER.info(url + " response:" + result);
			FileUtil.writeFileContent(result, "src/main/java/app/testing/AppTesting.json");
		}
		return result;
	}

	private static boolean login(String username, String password) {
		try {
			Map<String, String> params = Maps.newHashMap();
			params.put("producer_ model", "vivo X5Max+");
			params.put("pwd", DES_UTIL.encrypt(password));
			params.put("uname", DES_UTIL.encrypt(USERNAME));
			params.put("producer", "vivo");
			params.put("android_system_version", "4.4.4");
			params.put("version", "3.2.0");
			params.put("sign", getSign(params));
			String content = HttpUtil.post(HOST + "/login_v3.htm", params, null);
			LOGGER.info("/login_v3.htm response:" + content);
			sessionInfo = GsonUtil.getEntityFromJson(content, new TypeToken<SessionInfo>() {
			});
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	private static String getSign(Map<String, String> requestParams) throws UnsupportedEncodingException {
		Map<String, String> requestParamsNew = Maps.newHashMap();
		for (String key : requestParams.keySet()) {
			requestParamsNew.put(key.toLowerCase().replaceAll("_", ""), requestParams.get(key).toString());
		}

		List<String> params = new ArrayList<String>();
		for (String key : requestParamsNew.keySet()) {
			if ("sign".equals(key)) {
				continue;
			}
			params.add(key);
		}

		Collections.sort(params);

		String str = "";
		for (String paramKey : params) {
			str += paramKey + "=" + requestParamsNew.get(paramKey) + "&";
		}
		str = str.substring(0, str.length() - 1);

		if ("Yes".equals(requestParams.get("IsIosApp"))) {
			str = str.toLowerCase();
		} else {
			str = URLEncoder.encode(str.replaceAll("\\(", "").replaceAll("\\)", ""), "UTF-8").toLowerCase();
		}

		String requestParamsSign = MD5Util.getMD5String(API_APP_SECURITYKEY + MD5Util.getMD5String(str));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("参数签名:" + requestParamsSign);
		}
		return requestParamsSign;
	}

	public enum Method {
		POST, GET;
	}

	public static class SessionInfo {
		public String msg;
		public Data data;
		public String error_code;

		public static class Data {
			public String xjAgent;
			public String headImage;
			public String mobile;
			public String PTOKEN_ZNTG;
			public String userName;
			public String userId;

			public static class PostSuccAd {
				public String isShow;
			}
		}
	}

}
