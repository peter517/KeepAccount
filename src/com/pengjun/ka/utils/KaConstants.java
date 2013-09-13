package com.pengjun.ka.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pengjun.ka.R;

public class KaConstants {

	public static boolean isDebug = true;
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
	public static final String KA_ROOT = "/sdcard/ka";
	public static final String BACKUP_ROOT = KA_ROOT + File.separator + "backup";
	public static final String LOG_ROOT = KA_ROOT + File.separator + "log";
	public static final String BACKUP_FILE_POSTFIX = ".bk";

	// chart type
	public enum ChartType {
		Pie, LineDay, LineMonth, LineYear, Bar;
	}

	// type image res
	public static final String RES_IMAGE_PREFIX = "type";
	private static Map<String, Integer> imgResName2ResId = new HashMap<String, Integer>();

	static {
		Field[] fields = R.drawable.class.getDeclaredFields();
		for (Field field : fields) {
			// get all image from res which name start with type
			if (field.getName().startsWith(RES_IMAGE_PREFIX)) {
				try {
					imgResName2ResId.put(field.getName(), field.getInt(R.drawable.class));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static int getImgResIdByResName(String imgResName) {
		return imgResName2ResId.get(imgResName);
	}

	// log
	public static final Logger dbLogger = Logger.getLogger("db");
	public static final Logger serviceLogger = Logger.getLogger("service");
	public static final Logger kaLogger = Logger.getLogger("ka");
	public static final Logger clientLogger = Logger.getLogger("clientLogger");

}
