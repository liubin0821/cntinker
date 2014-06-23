package com.cntinker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author bin_liu
 * 
 */
public class MD5Encoder {
	static MessageDigest messageDigest = null;

	/**
	 * MD5 digest.
	 * 
	 * @param originalStr
	 *            the string before MD5 encode
	 * @return digest result after MD5 encode
	 */
	public static String encode32(String originalStr) {
		if (originalStr == null) {
			return null;
		}
		String newstr = encode(originalStr);
		/*
		 * if (newstr != null && newstr.length()==32){ BASE64Encoder base64en =
		 * new BASE64Encoder(); return base64en.encode(newstr.getBytes()); }else
		 */
		return newstr;
	}

	public static String encode16(String originalStr) {
		String newstr = null;
		if (originalStr == null) {
			return null;
		}
		newstr = encode(originalStr);
		if (newstr != null && newstr.length() == 32) {
			newstr = newstr.substring(8, 24);
			// BASE64Encoder base64en = new BASE64Encoder();
			// return base64en.encode(newstr.getBytes());
			return newstr;
		} else
			return newstr;
	}

	private static String encode(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(plainText.getBytes());

			byte b[] = md.digest();

			int i;

			for (int offset = 0; offset < b.length; offset++) {

				i = b[offset];

				if (i < 0)
					i += 256;

				if (i < 16)

					buf.append("0");

				buf.append(Integer.toHexString(i));

			}

		} catch (NoSuchAlgorithmException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}
		return buf.toString();

	}
}
