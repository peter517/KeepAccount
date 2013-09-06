package com.pengjun.ka.utils;

public class StringUtils {

	public static final String STRING_NULL_VALUE = "";

	public static <T extends Enum<T>> String[] getEnumStrArr(Class<T> enumClass) {
		String[] enumStrArr = new String[enumClass.getEnumConstants().length];
		int i = 0;
		for (Enum<T> e : enumClass.getEnumConstants()) {
			enumStrArr[i++] = e.name();
		}
		return enumStrArr;
	}
}
