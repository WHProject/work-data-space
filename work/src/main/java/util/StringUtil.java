package util;

import org.apache.log4j.Logger;

import com.google.common.base.MoreObjects;

public class StringUtil {

	private static Logger LOGGER = Logger.getLogger(StringUtil.class);

	public static Integer stringToInteger(String in) {
		if (in == null) {
			return null;
		}
		in = in.trim();
		if (in.length() == 0) {
			return null;
		}

		try {
			return Integer.parseInt(in);
		} catch (NumberFormatException e) {
			LOGGER.warn("stringToInteger fail,string=" + in, e);
			return null;
		}
	}

	public static boolean equals(String a, String b) {
		if (a == null) {
			return b == null;
		}
		return a.equals(b);
	}

	public static boolean isEmpty(String value) {
		if (value == null || value.length() == 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String o = null;
		String s = MoreObjects.<String>firstNonNull(o,"ddd");
		System.out.println(s);
	}

}
