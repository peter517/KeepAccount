package com.pengjun.ka.tools;

import java.util.List;

import com.pengjun.keepaccounts.R;

public class Constants {

	// Ar Type
	public static final String[] TYPE_STR_ARR = { "吃", "玩", "衣", "行", "其他" };
	public static final int[] TYPE_IMAGE_RES_ID_ARR = { R.drawable.type_eat,
			R.drawable.type_play, R.drawable.type_dress, R.drawable.type_car,
			R.drawable.type_other };
	public static final String TYPE_EAT = "吃";
	public static final String TYPE_PLAY = "玩";
	public static final String TYPE_DRESS = "衣";
	public static final String TYPE_CAR = "行";
	public static final String TYPE_OTHER = "其他";

	public static int getPosByCategroyStr(String str) {

		for (int i = 0; i < TYPE_STR_ARR.length; i++) {
			if (str.equals(TYPE_STR_ARR[i])) {
				return i;
			}
		}
		return 0;
	}

	public static int TOAST_EXSIT_TIME = 2000;

	// DB search error
	public static int DB_SEARCH_INT_NOT_FOUND = -1;
	public static String DB_SEARCH_STRING_NOT_FOUND = null;
	public static float DB_SEARCH_FLOAT_NOT_FOUND = -1.0f;
	public static List DB_SEARCH_LIST_NOT_FOUND = null;

	// use SharedPreferences to check first install
	public static String SP_TAG_INSTALL = "intall_tag";
	public static String SP_KEY_FIRST_START_APP = "frist_start_app";
	public static String SP_VALUE_FIRST_START_APP = "frist_start_app";

	// activity call-back tag
	public static final int ADD_AR = 01;
	public static final int ADD_AR_TYPE = 02;
}
