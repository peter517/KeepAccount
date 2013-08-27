package com.pengjun.ka.android.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.pengjun.ka.db.service.ReportNotificationService;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.TimeUtils;

public class ReportService extends Service {
	private ReportServiceBinder binder = new ReportServiceBinder();
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	private Timer timer = new Timer(true);
	private long checkDayInterval = 1000;
	private TimerTask task = new TimerTask() {
		public void run() {
			MyDebug.printFromPJ((TimeUtils.getCurDayStr()));
			if (TimeUtils.getCurDayStr().equals("1")) {
				ReportNotificationService.startReportNotification(ReportService.this);
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
		timer.schedule(task, TimeUtils.getCurDate(), checkDayInterval);
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
