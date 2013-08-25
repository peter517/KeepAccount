package com.pengjun.ka.utils;

import java.text.DecimalFormat;

public class MathUtils {

	// Keep a decimal places
	private static DecimalFormat df = new DecimalFormat("#.0");

	public static float formatFloat(Float d) {
		return Float.valueOf(df.format(d));
	}

	public static double formatDouble(Double d) {
		return Double.valueOf(df.format(d));
	}

	public static String double2String(Double d) {
		return String.format("%.1f", d);
	}

}
