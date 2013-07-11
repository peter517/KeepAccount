package com.pengjun.ka.tools;

import java.util.Calendar;

import android.content.Context;
import android.widget.DatePicker;

import com.pengjun.ka.db.model.AccountRecord;

public class Util {

	public static final String TIME_FORMT = "%d-%02d-%02d-%02d-%02d-%02d";
	public static final String DATE_FORMT = "%d-%02d-%02d";
	public static final String TIME_SEPARATOR = "-";

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

		String curTimeStr = String.format(TIME_FORMT, year, mouth + 1, day,
				hour, minute, second);

		return curTimeStr;
	}

	public static String[] String2DateArr(AccountRecord ar) {
		String[] date = ar.getDate().split(TIME_SEPARATOR);
		return date;
	}

	public static String DatePicker2FormatStr(DatePicker dp) {
		return String.format(DATE_FORMT, dp.getYear(), dp.getMonth() + 1,
				dp.getDayOfMonth());
	}

	public static void printCurDir(Context context) {
		Context cont = context.getApplicationContext();
		MyDebug.printFromPJ("cont.getCacheDir() = " + cont.getCacheDir());
		MyDebug.printFromPJ("cont.getDatabasePath = "
				+ cont.getDatabasePath("temp"));
		MyDebug.printFromPJ("cont.getFilesDir() = " + cont.getFilesDir());
	}

}
