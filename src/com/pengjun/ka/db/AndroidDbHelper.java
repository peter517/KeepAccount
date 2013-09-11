package com.pengjun.ka.db;

import android.content.Context;

import com.pengjun.db.BaseAndroidDbHelper;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;

public class AndroidDbHelper extends BaseAndroidDbHelper {

	@SuppressWarnings("rawtypes")
	private static final Class[] DATACLASSES = { AccountRecord.class, ArType.class };
	private static final String DBNAME = "ka.db";

	private static AndroidDbHelper androidDbHelper = null;

	public AndroidDbHelper(Context context) {
		super(context, DBNAME, DATACLASSES);
	}

	public static AndroidDbHelper getSingleInstance(Context context) {
		if (androidDbHelper == null) {
			androidDbHelper = new AndroidDbHelper(context);
		}
		return androidDbHelper;
	}
}
