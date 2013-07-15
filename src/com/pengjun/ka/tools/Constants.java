package com.pengjun.ka.tools;

public class Constants {

	public static final String[] CATEGORY_ARR = { "吃", "玩", "衣", "行", "其他" };

	public static final String TYPE_EAT = "吃";
	public static final String TYPE_PLAY = "玩";
	public static final String TYPE_DRESS = "衣";
	public static final String TYPE_CAR = "行";
	public static final String TYPE_OTHER = "其他";

	public static int getPosByCategroyStr(String str) {

		for (int i = 0; i < CATEGORY_ARR.length; i++) {
			if (str.equals(CATEGORY_ARR[i])) {
				return i;
			}
		}
		return -1;
	}

	public static int TOAST_EXSIT_TIME = 2000;
}
