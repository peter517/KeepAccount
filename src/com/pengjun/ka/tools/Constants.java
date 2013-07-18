package com.pengjun.ka.tools;

import java.util.List;

import com.pengjun.keepaccounts.R;

public class Constants {

	// Ar Type
	public static final String[] TYPE_STR_ARR = { "吃", "玩", "衣", "行", "其他" };
	public static final int[] TYPE_IMAGE_RES_ID_ARR = { R.drawable.type_eat,
			R.drawable.type_play, R.drawable.type_dress, R.drawable.type_car,
			R.drawable.type_other };

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
	public static final int CB_ADD_AR = 01;
	public static final int CB_ADD_AR_TYPE = 02;
	public static final int CB_ADD_AR_TYPE_NAME_LIST = 03;

	// activity intent translate data tag
	public static final String INTENT_AR_BEAN = "ar_bean";
	public static final String INTENT_AR_TYPE_BEAN = "ar_type_bean";
	public static final String INTENT_AR_TYPE_NAME_LIST_BEAN = "ar_type_name_list_bean";
}
