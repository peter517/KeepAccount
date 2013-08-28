package com.pengjun.ka.android.activity;

import android.app.Application;
import android.content.Intent;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.pengjun.ka.android.service.ReportService;
import com.pengjun.ka.db.DbHelper;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.ResourceUtils;
import com.pengjun.ka.utils.StringUtils;
import com.pengjun.ka.utils.TimeUtils;

public final class KaApplication extends Application {

	private static KaApplication instance = null;

	public static KaApplication instance() {
		return instance;
	}

	public static AndroidConnectionSource androidConnectionSource;

	public static AndroidConnectionSource getAndroidConnectionSource() {
		return androidConnectionSource;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		androidConnectionSource = DbHelper.getAndroidConnectionSource(this);

		if (ResourceUtils.isServiceRunning(this, ReportService.class.getName()) == false) {
			Intent newIntent = new Intent(this, ReportService.class);
			this.startService(newIntent);
		}

		// if first start of the app
		if (ResourceUtils.getSharedPreferencesString(this, Constants.SP_KEY_FIRST_START_APP).equals(
				StringUtils.STRING_NULL_VALUE)) {
			ResourceUtils.putSharedPreferencesString(this, Constants.SP_KEY_FIRST_START_APP,
					Constants.SP_VALUE_FIRST_START_APP);
			ResourceUtils.putSharedPreferencesString(this, TimeUtils.getCurMonthYearStr(),
					TimeUtils.getCurMonthYearStr());
			ArTypeDao.initTable();

		}

	}

	@Override
	public void onTerminate() {
		instance = null;
		super.onTerminate();
	}

}
