package com.pengjun.ka.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeUtils {

	public static final String TIME_STRING_FORMT = "%d-%02d-%02d-%02d-%02d-%02d";
	public static final String DATE_STRING_FORMT = "%d-%02d-%02d";
	public static final String DATE_FORMT = "yyyy-MM-dd";
	public static final String YERA_MONTH_FORMT = "yyyy-MM";
	public static final String YERA_FORMT = "yyyy";
	public static final String TIME_SEPARATOR = "-";

	private static DateFormat dateFormat = new SimpleDateFormat(DATE_FORMT);
	private static DateFormat yearMonthFormat = new SimpleDateFormat(YERA_MONTH_FORMT);
	private static DateFormat yearFormat = new SimpleDateFormat(YERA_FORMT);

	private static Random random = new Random();
	static {
		random.setSeed(System.currentTimeMillis());
	}

	public static Date string2Date(String dateStr) {
		try {
			return dateFormat.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date string2YearMonthDate(String dateStr) {
		try {
			return yearMonthFormat.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date string2YearDate(String dateStr) {
		try {
			return yearFormat.parse(dateStr);
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

		String curTimeStr = String.format(TIME_STRING_FORMT, year, mouth + 1, day, hour, minute, second);

		return curTimeStr;
	}

	public static String getCurDateStr() {

		final Calendar mCalendar = Calendar.getInstance();
		long time = System.currentTimeMillis();
		mCalendar.setTimeInMillis(time);
		int year = mCalendar.get(Calendar.YEAR);
		int mouth = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);

		String curTimeStr = String.format(DATE_STRING_FORMT, year, mouth + 1, day);

		return curTimeStr;
	}

	public static String getRandomDateStr() {
		return String.format(DATE_STRING_FORMT, 2000 + random.nextInt(13), 1 + random.nextInt(12),
				1 + random.nextInt(30));
	}

	public static float getRandomFloat() {
		return random.nextFloat();
	}

	public static int getRandomInt(int max) {
		return random.nextInt(max);
	}

	public static String getRandomTimeStr() {

		return String.format(TIME_STRING_FORMT, 2000 + random.nextInt(13), 1 + random.nextInt(12),
				1 + random.nextInt(30), 1 + random.nextInt(24), 1 + random.nextInt(60),
				1 + random.nextInt(60));
	}

	public static String[] String2DateStrArr(String dateStr) {
		String[] date = dateStr.split(TIME_SEPARATOR);
		return date;
	}

	public static String String2YearMonthDateStr(String dateStr) {
		String[] date = String2DateStrArr(dateStr);
		return date[0] + "-" + date[1];
	}

	public static String String2YearDateStr(String dateStr) {
		String[] date = String2DateStrArr(dateStr);
		return date[0];
	}

}
