package com.pengjun.ka.tools;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.pengjun.keepaccounts.R;

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

	public static String getCurDateStr() {

		final Calendar mCalendar = Calendar.getInstance();
		long time = System.currentTimeMillis();
		mCalendar.setTimeInMillis(time);
		int year = mCalendar.get(Calendar.YEAR);
		int mouth = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);

		String curTimeStr = String.format(DATE_FORMT, year, mouth + 1, day);

		return curTimeStr;
	}

	public static String[] String2DateArr(String dateStr) {
		String[] date = dateStr.split(TIME_SEPARATOR);
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

	public static int getPosFromList(List<String> strList, String str) {

		for (int i = 0; i < strList.size(); i++) {
			if (str.equals(strList.get(i))) {
				return i;
			}
		}
		return 0;
	}

	public static Dialog createAlertDialog(Context context, String warnInfo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.title_warning);
		builder.setTitle("警告");
		builder.setMessage(warnInfo);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

}
