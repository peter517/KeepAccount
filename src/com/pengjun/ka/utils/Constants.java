package com.pengjun.ka.utils;

import java.util.List;

public class Constants {

	public static int TOAST_EXSIT_TIME = 2000;

	// init Ar Type
	public static final String[] TYPE_STR_ARR = { "吃饭", "娱乐", "衣服", "交通", "其他" };
	public static final String[] TYPE_IMAGE_RES_ID_ARR = { "type_eat",
			"type_play", "type_dress", "type_car", "type_other" };

	// DB search error
	public static int DB_SEARCH_INT_NOT_FOUND = -1;
	public static String DB_SEARCH_STRING_NOT_FOUND = null;
	public static float DB_SEARCH_FLOAT_NOT_FOUND = -1.0f;
	public static List DB_SEARCH_LIST_NOT_FOUND = null;

	// use SharedPreferences to check first install
	public static String SP_TAG_INSTALL = "intall_tag";
	public static String SP_KEY_FIRST_START_APP = "frist_start_app";
	public static String SP_VALUE_FIRST_START_APP = "frist_start_app";

	// activity intent callback tag
	public static final int CB_ADD_AR = 01;
	public static final int CB_ADD_AR_TYPE = 02;
	public static final int CB_ADD_AR_TYPE_NAME = 03;

	// activity intent translate data tag
	public static final String INTENT_AR_BEAN = "ar_bean";
	public static final String INTENT_AR_TYPE_BEAN = "ar_type_bean";
	public static final String INTENT_AR_TYPE_NAME_LIST_BEAN = "ar_type_name_list_bean";
	public static final String INTENT_DISABLE_AR_TYPE_MANAGE = "disable_ar_type_manage";
	public static final String INTENT_AR_SEARCH_CONDITION = "ar_search_condition";

}
