package com.pengjun.ka.android.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.pengjun.ka.db.service.ReportNotificationService;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.ResourceUtils;
import com.pengjun.ka.utils.StringUtils;
import com.pengjun.ka.utils.TimeUtils;

public class ReportService extends Service {
	private ReportServiceBinder binder = new ReportServiceBinder();

	private static final long HOUR_PERIOD = 60 * 60 * 1000;
	private Timer timer = new Timer(true);
	private final String MORNING_TEN_COLOCK = "10";
	private final String LIGHT_TEN_COLOCK = "22";

	private TimerTask task = new TimerTask() {
		public void run() {

			String curMonthYearStr = TimeUtils.getCurMonthYearStr();
			boolean isCurMonthReportExist = !ResourceUtils.getSharedPreferencesString(ReportService.this,
					curMonthYearStr).equals(StringUtils.STRING_NULL_VALUE);

			String curHour = TimeUtils.getCurHour();
			boolean isProperTimeToPushReport = curHour.compareTo(MORNING_TEN_COLOCK) >= 0
					&& curHour.compareTo(LIGHT_TEN_COLOCK) <= 0;

			if (!isCurMonthReportExist && isProperTimeToPushReport) {
				ReportNotificationService.startReportNotification(ReportService.this);
				ResourceUtils
						.putSharedPreferencesString(ReportService.this, curMonthYearStr, curMonthYearStr);
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		timer.schedule(task, TimeUtils.getCurDate(), HOUR_PERIOD);
		MyDebug.printFromPJ("ReportService onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		MyDebug.printFromPJ("ReportService onStart");
	}

	@Override
	public void onDestroy() {
		MyDebug.printFromPJ("ReportService onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		MyDebug.printFromPJ("ReportService onUnbind");
		return super.onUnbind(intent);
	}

	public class ReportServiceBinder extends Binder {
		public ReportService getService() {
			return ReportService.this;
		}
	}

}
