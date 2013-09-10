package com.pengjun.ka.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {

	public static final String NULL_STRING = "";

	public static <T extends Enum<T>> String[] getEnumStrArr(Class<T> enumClass) {
		String[] enumStrArr = new String[enumClass.getEnumConstants().length];
		int i = 0;
		for (Enum<T> e : enumClass.getEnumConstants()) {
			enumStrArr[i++] = e.name();
		}
		return enumStrArr;
	}

	public static String createMd5(String str) {

		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();

	}
}
