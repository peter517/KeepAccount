package com.pengjun.ka.utils;

import java.util.List;

public class KaConstants {

	public static int TOAST_EXSIT_TIME = 2000;

	// init Ar Type
	public enum InitArType {
		type_eat("餐饮"), type_play("文化娱乐"), type_dress("服饰美容"), type_car("交通"), type_house("住宿"), type_money(
				"人情来往"), type_shopping("生活用品"), type_other("其他");

		String typename;

		private InitArType(String typename) {
			this.typename = typename;
		}

		public String getTypeName() {
			return typename;
		}
	}

	// DB search error
	public final static int DB_SEARCH_INT_NOT_FOUND = -1;
	public final static String DB_SEARCH_STRING_NOT_FOUND = null;
	public final static float DB_SEARCH_FLOAT_NOT_FOUND = -1.0f;
	public final static List DB_SEARCH_LIST_NOT_FOUND = null;

	// activity intent callback tag
	public static final int CB_ADD_AR = 01;
	public static final int CB_ADD_AR_TYPE = 02;
	public static final int CB_ADD_AR_TYPE_NAME = 03;
	public static final int CB_AR_CHART = 4;

	// use SharedPreferences to check first install
	public final static String SP_KEY_FIRST_START_APP = "frist_start_app";
	public final static String SP_VALUE_FIRST_START_APP = "frist_start_app";

	// activity intent translate data tag
	public static final String INTENT_AR_BEAN = "ar_bean";
	public static final String INTENT_AR_TYPE_BEAN = "ar_type_bean";
	public static final String INTENT_AR_TYPE_NAME_LIST_BEAN = "ar_type_name_list_bean";
	public static final String INTENT_DISABLE_AR_TYPE_MANAGE = "disable_ar_type_manage";
	public static final String INTENT_AR_SEARCH_CONDITION = "ar_search_condition";
	public static final String INTENT_AR_LIST = "ar_list";
	public static final String INTENT_AR_CHART_TYPE = "ar_chart_type";
	public static final String INTENT_TOP_TITLE = "current_month_year";

	// backup file path
	public static final String BACK_UP_ROOT = "/sdcard/ka";
	public static final String BACK_AR_FILE_NAME = "ar.dat";
	public static final String BACK_AR_TYPE_FILE_NAME = "arType.dat";

	// chart type
	public enum ChartType {
		Pie, LineDay, LineMonth, LineYear, Bar;

		public static String[] names() {
			String[] names = new String[ChartType.values().length];
			int i = 0;
			for (ChartType mode : ChartType.values())
				names[i++] = mode.name();
			return names;
		}

		public static ChartType getTypeFromString(String typeStr) {
			for (ChartType type : ChartType.values()) {
				if (type.name().equals(typeStr))
					return type;
			}
			return null;
		}

	}

}