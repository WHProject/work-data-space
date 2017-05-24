package splitwords;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class Pullword2 {
	private static Logger log = Logger.getLogger(Pullword2.class);

	public static void main(String[] args) {

		/*
		 * Map<String, String> params = new LinkedHashMap<>();
		 * params.put("source", "五粮浓香，名门臻品，至尊至享！"); params.put("param1", "0.5");
		 * params.put("param2", "0"); String body =
		 * post("http://www.pullword.com/process.php", params);
		 * System.out.println(body);
		 */
		// getNumberStr("45°汾酒集团百家老根玻璃烤花瓶特制十年礼盒整箱装225mL*2*6");

		List<String> content = getFileContent("src/words.txt");
		begin(content);

	}

	private static List<String> content = new ArrayList<>();

	static {
		content.add(" 紫妞");
		content.add(" 红妞");
		content.add(" 金妞");
		content.add(" 蓝狗");
		content.add(" 蓝猪");
	}

	private static String getRandomContent() {
		String msg = "";
		if (content != null && content.size() > 0) {
			int index = (int) (Math.random() * content.size());
			msg = content.get(index);
		}
		return msg;
	}

	public static void begin(List<String> content) {
		int i = 1;
		for (String s : content) {
			Map<String, String> params = new LinkedHashMap<>();
			params.put("source", s);
			i++;
			params.put("param1", "0.5");
			params.put("param2", "0");
			String body = post("http://www.pullword.com/process.php", params);
			while (!body.contains("HTTP")) {
				sleep(100);
				params.put("source", s + getRandomContent());
				body = post("http://www.pullword.com/process.php", params);
			}
			sleep(350);
			System.out.println(getNumberStr(s) + " " + getFormateStr(body));
		}
	}

	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getNumberStr(String body) {
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("\\d{2}°");
		Matcher m = p.matcher(body);
		while (m.find()) {
			sb.append(m.group()).append(" ");
		}

		// Pattern p1 = Pattern.compile("(?i)\\d*ml\\*\\d*(\\*\\d*)*");
		Pattern p1 = Pattern.compile("(?i)\\d*ml");
		Matcher m1 = p1.matcher(body);
		while (m1.find()) {
			sb.append(m1.group());
		}
		return sb.toString();
	}

	public static String getFormateStr(String body) {
		StringBuilder sb = new StringBuilder();
		for(String c:content){
			body=body.replace(c.trim(), "");
		}
		body = body.replaceAll("(?s)抽词.*UTF-8", "").trim();
		String[] ss = body.split("\n");
		for (String s : ss) {
			sb.append(s.replaceAll("[\\t\\n\\r]", " "));
		}
		return sb.toString();
	}

	private static List<String> getFileContent(String filePath) {
		String line;
		List<String> content = new ArrayList<>();
		try {
			InputStreamReader streamReader = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
			BufferedReader reader = new BufferedReader(streamReader);
			while ((line = reader.readLine()) != null) {
				content.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		log.info("create httppost:" + url);
		HttpPost post = postForm(url, params);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String get(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		log.info("create httppost:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {

		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);

		return body;
	}

	private static String paseResponse(HttpResponse response) {
		log.info("get response from http server..");
		HttpEntity entity = response.getEntity();

		log.info("response status: " + response.getStatusLine());
		String charset = EntityUtils.getContentCharSet(entity);
		log.info(charset);

		String body = null;
		try {
			body = EntityUtils.toString(entity);
			log.info(body);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		log.info("execute post...");
		HttpResponse response = null;

		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params) {

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			log.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}
}