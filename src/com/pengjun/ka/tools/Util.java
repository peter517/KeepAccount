package com.pengjun.ka.tools;

import java.util.Calendar;

import android.content.Context;

public class Util {
	public static String getCurTimeStr() {

		final Calendar mCalendar = Calendar.getInstance();
		long time = System.currentTimeMillis();
		mCalendar.setTimeInMillis(time);
		int year = mCalendar.get(Calendar.YEAR);
		int mouth = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		int hour = mCalendar.get(Calendar.HOUR);
		int minute = mCalendar.get(Calendar.MINUTE);
		int second = mCalendar.get(Calendar.SECOND);

		String curTimeStr = String.format("%d-%02d-%02d-%02d-%02d-%02d", year,
				mouth + 1, day, hour, minute, second);
		return curTimeStr;
	}

	public static void printCurDir(Context context) {
		Context cont = context.getApplicationContext();
		MyDebug.printFromPJ("cont.getCacheDir() = " + cont.getCacheDir());
		MyDebug.printFromPJ("cont.getDatabasePath = "
				+ cont.getDatabasePath("temp"));
		MyDebug.printFromPJ("cont.getFilesDir() = " + cont.getFilesDir());
	}

}
