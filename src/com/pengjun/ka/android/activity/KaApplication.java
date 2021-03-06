package com.pengjun.ka.android.activity;

import android.app.Application;
import android.content.Intent;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.pengjun.android.utils.AdResourceUtils;
import com.pengjun.ka.android.service.ReportNotificationService;
import com.pengjun.ka.db.AndroidDbHelper;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.utils.KaConstants;
import com.pengjun.utils.StringUtils;
import com.pengjun.utils.TimeUtils;

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

		KaConstants.kaLogger.info("ka start");

		// need too many memory, stop first
		// load IKSegmenter files, about ten seconds
		// new Thread() {
		// public void run() {
		// IKAnalyzerUtils.initSegmentationTool(instance);
		// }
		// }.start();

		androidConnectionSource = AndroidDbHelper.getSingleInstance(instance).getAndroidConnectionSource();

		if (AdResourceUtils.isServiceRunning(this, ReportNotificationService.class.getName()) == false) {
			Intent newIntent = new Intent(this, ReportNotificationService.class);
			this.startService(newIntent);
		}

		// if first start of the app
		if (AdResourceUtils.getSharedPreferencesString(this, KaConstants.SP_KEY_FIRST_START_APP).equals(
				StringUtils.NULL_STRING)) {

			AdResourceUtils.putSharedPreferencesString(this, KaConstants.SP_KEY_FIRST_START_APP,
					KaConstants.SP_VALUE_FIRST_START_APP);

			// mark the week and month report has sent at first start app
			String curMonthYear = TimeUtils.getCurMonthYearStr();
			AdResourceUtils.putSharedPreferencesString(this, curMonthYear, curMonthYear);

			String curWeekYear = TimeUtils.getCurWeekYearStr();
			AdResourceUtils.putSharedPreferencesString(this, curWeekYear, curWeekYear);

			ArTypeDao.getSingleInstance().initTable();

		}
	}

	@Override
	public void onTerminate() {
		instance = null;
		super.onTerminate();
	}

}
