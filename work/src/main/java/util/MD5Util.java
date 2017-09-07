package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class MD5Util {

	private static final Logger loger = Logger.getLogger(MD5Util.class);

	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * 生成字符串的md5校验值
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	/**
	 * 判断字符串的md5校验码是否与一个已知的md5码相匹配
	 * 
	 * @param password
	 *            要校验的字符串
	 * @param md5PwdStr
	 *            已知的md5校验码
	 * @return
	 */
	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException {
		InputStream fis = null;
		ByteArrayOutputStream out = null;

		try {
			fis = new FileInputStream(file);
			out = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = fis.read(temp)) > 0) {
				out.write(temp, 0, size);
			}

			return getMD5String(out.toByteArray());
		} finally {
			fis.close();
			out.close();
		}
	}

	public static String getMD5String(byte[] bytes) {

		String md5 = null;

		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(bytes);
			md5 = bufferToHex(messagedigest.digest());
		} catch (NoSuchAlgorithmException e) {
			loger.error(e);
		}

		return md5;
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
												// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static String getJywBy17stirng(String s17) {
		if (17 != s17.length()) {
			return "0";
		}
		String[] ID_ICCID_array = new String[17];
		for (int i = 0; i < s17.length(); i++) {
			ID_ICCID_array[i] = String.valueOf(s17.charAt(i));
		}
		;
		int s = (Integer.parseInt(ID_ICCID_array[0]) + Integer.parseInt(ID_ICCID_array[10])) * 7
				+ (Integer.parseInt(ID_ICCID_array[1]) + Integer.parseInt(ID_ICCID_array[11])) * 9
				+ (Integer.parseInt(ID_ICCID_array[2]) + Integer.parseInt(ID_ICCID_array[12])) * 10
				+ (Integer.parseInt(ID_ICCID_array[3]) + Integer.parseInt(ID_ICCID_array[13])) * 5
				+ (Integer.parseInt(ID_ICCID_array[4]) + Integer.parseInt(ID_ICCID_array[14])) * 8
				+ (Integer.parseInt(ID_ICCID_array[5]) + Integer.parseInt(ID_ICCID_array[15])) * 4
				+ (Integer.parseInt(ID_ICCID_array[6]) + Integer.parseInt(ID_ICCID_array[16])) * 2
				+ Integer.parseInt(ID_ICCID_array[7]) * 1 + Integer.parseInt(ID_ICCID_array[8]) * 6
				+ Integer.parseInt(ID_ICCID_array[9]) * 3;
		int y = s % 11;
		String M = "F";
		String JYM = "10X98765432";
		M = JYM.substring(y, y + 1);
		return M;
	}

}
