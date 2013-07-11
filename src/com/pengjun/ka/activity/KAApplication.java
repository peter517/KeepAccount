package com.pengjun.ka.activity;

import android.app.Application;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.pengjun.ka.db.DbHelper;

public final class KAApplication extends Application {
	private final static String TAG = KAApplication.class.getSimpleName();

	private static KAApplication instance = null;

	public static KAApplication instance() {
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

	}

	@Override
	public void onTerminate() {
		instance = null;
		super.onTerminate();
	}

	/*
	 * public Handler getMessageHandler() { return mHandler; }
	 */
}
