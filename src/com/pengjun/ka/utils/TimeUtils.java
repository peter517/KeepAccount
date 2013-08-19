package com.pengjun.ka.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.widget.DatePicker;

public class TimeUtils {

	public static final String TIME_FORMT = "%d-%02d-%02d-%02d-%02d-%02d";
	public static final String DATE_FORMT = "%d-%02d-%02d";
	public static final String TO_DATE_FORMT = "yyyy-MM-dd";
	public static final String TO_YERA_MONTH_FORMT = "yyyy-MM";
	public static final String TO_YERA_FORMT = "yyyy";
	public static final String TIME_SEPARATOR = "-";

	private static Random random = new Random();
	static {
		random.setSeed(System.currentTimeMillis());
	}

	// public static Date string2Date(String dateStr) {
	// return Date.valueOf(dateStr);
	// }

	public static Date string2Date(String dateStr) {
		DateFormat format = new SimpleDateFormat(TO_DATE_FORMT);
		try {
			return format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date string2YearMonth(String dateStr) {
		DateFormat format = new SimpleDateFormat(TO_YERA_MONTH_FORMT);
		try {
			return format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date string2Year(String dateStr) {
		DateFormat format = new SimpleDateFormat(TO_YERA_FORMT);
		try {
			return format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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

		String curTimeStr = String.format(TIME_FORMT, year, mouth + 1, day, hour, minute, second);

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

	public static String getRandomDateStr() {
		return String.format(DATE_FORMT, 2000 + random.nextInt(13), 1 + random.nextInt(12),
				1 + random.nextInt(30));
	}

	public static float getRandomFloat() {
		return random.nextFloat();
	}

	public static int getRandomInt(int max) {
		return random.nextInt(max);
	}

	public static String getRandomTimeStr() {

		return String.format(TIME_FORMT, 2000 + random.nextInt(13), 1 + random.nextInt(12),
				1 + random.nextInt(30), 1 + random.nextInt(24), 1 + random.nextInt(60),
				1 + random.nextInt(60));
	}

	public static String[] String2DateStrArr(String dateStr) {
		String[] date = dateStr.split(TIME_SEPARATOR);
		return date;
	}

	public static String getYearMouthFromDateStr(String dateStr) {
		String[] date = String2DateStrArr(dateStr);
		return date[0] + "-" + date[1];
	}

	public static String getYearFromDateStr(String dateStr) {
		String[] date = String2DateStrArr(dateStr);
		return date[0];
	}

	public static String DatePicker2FormatStr(DatePicker dp) {
		return String.format(DATE_FORMT, dp.getYear(), dp.getMonth() + 1, dp.getDayOfMonth());
	}

}
